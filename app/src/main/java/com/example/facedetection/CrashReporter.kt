package com.example.facedetection

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

object CrashReporter {
    private const val TAG = "CrashReporter"

    fun init() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleException(thread,throwable)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun handleException(thread: Thread, throwable: Throwable) {
        val processInfo = "Process: com.example.FaceDetection, PID: ${android.os.Process.myPid()}"
        val threadInfo = "Thread: ${thread.name}"
        val fatalInfo =  if (isFatal(throwable))  "FATAL EXCEPTION:main" else "NON-FATAL EXCEPTION:main"
        val stackTraceString = getStackTraceString(throwable.fillInStackTrace())
        val errorMessage = "$fatalInfo\n$processInfo\n$stackTraceString"
        Log.e(TAG, errorMessage)

        SlackNotifier.sendExceptionMessage(errorMessage)
// Here you can implement code to log the exception to a file, database, or external service
    }

    private fun getStackTraceString(throwable: Throwable): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        throwable.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
    private fun isFatal(throwable: Throwable): Boolean {
        // Add your logic here to determine if the exception is fatal
        // Example criteria:
        return throwable is OutOfMemoryError || throwable is VirtualMachineError || throwable is RuntimeException
    }
}