package com.traductorcodigomorse;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView codigoMorse;
    private SensorManager sensorManager;
    private Sensor light;
    private boolean esTextEnMorse;
    private float ultimaMedicion;
    final int PUNTO_DE_TRADUCCION = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        codigoMorse = findViewById(R.id.codigoMorse);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager != null ? sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) : null;
        esTextEnMorse = true;
        ultimaMedicion = 0;
    }

    public void agregarRaya(View view) {
        codigoMorse.append("-");
    }

    public void agregarPunto(View view) {
        codigoMorse.append(".");
    }

    public void agregarEspacio(View view) {
        codigoMorse.append("|");
    }

    public void enviar(View view) {
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