package com.example.facedetection

import android.content.Context
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Thread.setDefaultUncaughtExceptionHandler

object CrashReporter {
    private const val TAG = "CrashReporter"

    fun init(context: Context) {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        setDefaultUncaughtExceptionHandler { thread, throwable ->
            thread.uncaughtExceptionHandler.uncaughtException(thread,throwable)
            handleException(throwable)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun handleException(throwable: Throwable) {
        val stackTraceString = getStackTraceString(throwable)
        Log.e(TAG, stackTraceString)

        // Here you can implement code to log the exception to a file, database, or external service
    }

    private fun getStackTraceString(throwable: Throwable): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        throwable.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}