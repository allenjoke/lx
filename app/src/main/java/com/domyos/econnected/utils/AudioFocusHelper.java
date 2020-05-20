package com.domyos.econnected.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by HouWei on 16/8/23.
 */

public class AudioFocusHelper implements
        AudioManager.OnAudioFocusChangeListener {
    /**
     * Represents something that can react to audio focus events. We implement
     * this instead of just using AudioManager.OnAudioFocusChangeListener
     * because that interface is only available in SDK level 8 and above, and we
     * want our application to work on previous SDKs.
     */
    public interface MusicFocusable {
        /** Signals that audio focus was gained. */
        public void onGainedAudioFocus();

        /** Signals that audio focus was lost. */
        public void onLostAudioFocus();
    }

    AudioManager mAM;
    MusicFocusable mFocusable;

    // do we have audio focus?
    public static final int NoFocusNoDuck = 0; // we don't have audio focus, and
    // can't duck
    public static final int NoFocusCanDuck = 1; // we don't have focus, but can
    // play at a low volume
    // ("ducking")
    public static final int Focused = 2; // we have full audio focus

    private int mAudioFocus = NoFocusNoDuck;

    public AudioFocusHelper(Context ctx, MusicFocusable focusable) {
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            mAM = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
            mFocusable = focusable;
        } else {
            mAudioFocus = Focused; // no focus feature, so we always "have"
            // audio focus
        }
    }

    /** Requests audio focus. Returns whether request was successful or not. */
    public boolean requestFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAM
                .requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
    }

    /** Abandons audio focus. Returns whether request was successful or not. */
    public boolean abandonFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAM
                .abandonAudioFocus(this);
    }

    public void giveUpAudioFocus() {
        if (mAudioFocus == Focused && android.os.Build.VERSION.SDK_INT >= 8
                && abandonFocus())
            mAudioFocus = NoFocusNoDuck;
    }

    public void tryToGetAudioFocus() {
        if (mAudioFocus != Focused && android.os.Build.VERSION.SDK_INT >= 8
                && requestFocus())
            mAudioFocus = Focused;
    }

    /**
     * Called by AudioManager on audio focus changes. We implement this by
     * calling our MusicFocusable appropriately to relay the message.
     */
    public void onAudioFocusChange(int focusChange) {
        if (mFocusable == null)
            return;
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                mAudioFocus = Focused;
                mFocusable.onGainedAudioFocus();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                mAudioFocus = NoFocusNoDuck;
                mFocusable.onLostAudioFocus();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                mAudioFocus = NoFocusCanDuck;
                mFocusable.onLostAudioFocus();
                break;
            default:
        }
    }

    public int getAudioFocus() {
        return mAudioFocus;
    }
}