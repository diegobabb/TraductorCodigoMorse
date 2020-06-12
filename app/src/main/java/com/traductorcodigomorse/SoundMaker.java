package com.traductorcodigomorse;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;


public class SoundMaker {

    private Context context;

    public SoundMaker(Context context) {
        this.context = context;
    }

    private void play(boolean sound) {
        AsyncPlayer asyncPlayer = new AsyncPlayer("BeepPlayer");
        Uri uri = (sound) ? this.getUriToResource(context, R.raw.dot) : this.getUriToResource(context, R.raw.dash);
        asyncPlayer.play(context, uri, false, AudioManager.STREAM_RING);
    }

    public void playDot() {
        this.play(true);
    }

    public void playDash() {
        this.play(false);
    }

    // Obtenido de https://stackoverflow.com/questions/45722227/how-to-get-an-uri-from-a-raw-resource
    private Uri getUriToResource(@NonNull Context context, @AnyRes int resId) throws Resources.NotFoundException {
        Resources res = context.getResources();

        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));
        return resUri;
    }


}
