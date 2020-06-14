package com.traductorcodigomorse;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView codigoMorse;
    EditText editTextPhone;
    private SensorManager sensorManager;
    private Sensor light;
    private boolean esTextEnMorse;
    private float ultimaMedicion;
    final int PUNTO_DE_TRADUCCION = 15000;
    private SoundMaker soundMaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        codigoMorse = findViewById(R.id.codigoMorse);
        soundMaker = new SoundMaker(getApplication().getApplicationContext());
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager != null ? sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) : null;
        esTextEnMorse = true;
        ultimaMedicion = 0;
        editTextPhone = findViewById(R.id.editTextPhone);
    }

    public void agregarRaya(View view) {
        codigoMorse.append("-");
        soundMaker.playDash();
    }

    public void agregarPunto(View view) {
        codigoMorse.append(".");
        soundMaker.playDot();
    }

    public void agregarEspacio(View view) {
        codigoMorse.append("|");
    }

    public void enviar(View view) {
        try {
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(editTextPhone.getText().toString(), null, codigoMorse.getText().toString(), null, null);
            Toast.makeText(MainActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    public void borrar(View view) {
        String str = codigoMorse.getText().toString();
        int length = str.length();
        if(0 < length) {
            str = str.substring(0, length - 1);
            codigoMorse.setText(str);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            if (event.values[0] < PUNTO_DE_TRADUCCION && ultimaMedicion != event.values[0] && esTextEnMorse) {
                codigoMorse.setText(MorseCode.morseToAlpha(codigoMorse.getText().toString().trim()));
                esTextEnMorse = false;
            } else if (event.values[0] >= PUNTO_DE_TRADUCCION && ultimaMedicion != event.values[0] && !esTextEnMorse) {
                codigoMorse.setText(MorseCode.alphaToMorse(codigoMorse.getText().toString()));
                esTextEnMorse = true;
            }
            ultimaMedicion = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}