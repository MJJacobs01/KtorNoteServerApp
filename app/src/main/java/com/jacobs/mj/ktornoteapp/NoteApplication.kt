package com.jacobs.mj.ktornoteapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by mj on 2021/03/06 at 9:09 AM
 */
@HiltAndroidApp
class NoteApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}