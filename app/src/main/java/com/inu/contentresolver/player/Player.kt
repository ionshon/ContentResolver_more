package com.inu.contentresolver.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log

object Player {

    var mediaPlayer: MediaPlayer? = null

    fun musicPlayer(musicUri: Uri?, context: Context) {
        if (mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
        Log.d("클릭 : ", "${musicUri}")
        mediaPlayer = MediaPlayer.create(context, musicUri)
        mediaPlayer?.start()
    }
}