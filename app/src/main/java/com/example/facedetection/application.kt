package com.example.facedetection

import android.app.Application

class application : Application() {
    override fun onCreate() {
        super.onCreate()

//        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
//            Log.e("applicationException", "Exception:$exception ")
//
//            android.os.Process.killProcess(android.os.Process.myPid())
//            exitProcess(10)
//        }

        CrashReporter.init(applicationContext)
    }
}