package com.inu.contentresolver.player

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class MusicService : Service() {

    private val mBinder = MyBinder()

    inner class MyBinder(): Binder() {
        val instance: MusicService
            get() = this@MusicService
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
      //   val action = intent?.action
        return Service.START_NOT_STICKY //super.onStartCommand(intent, flags, startId)
    }
}