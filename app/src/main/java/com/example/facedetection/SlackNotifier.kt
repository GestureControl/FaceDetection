package com.example.facedetection


import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object SlackNotifier {
    private const val SLACK_WEBHOOK_URL = "https://hooks.slack.com/services/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"

    fun sendExceptionMessage(message: String) {
        val client = OkHttpClient()
        val requestBody = "{\"text\": \"$message\"}".toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(SLACK_WEBHOOK_URL)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.i("slackFail", "onFailure: $e.printStackTrace()")
                e.printStackTrace()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                // Handle response if needed
            }
        })
    }
}

/*
class SlackService : Service() {

    override fun onCreate() {
        super.onCreate()
        // Initialize any resources needed by the service
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Perform the network call in a background thread
        val message = intent?.getStringExtra("message")
        if (message != null) {
            sendExceptionMessage(message)
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun sendExceptionMessage(message: String) {
        val client = OkHttpClient()
        val requestBody = "{\"text\": \"$message\"}".toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(SLACK_WEBHOOK_URL)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.i("slackFail", "onFailure: $e.printStackTrace()")
                e.printStackTrace()
                stopSelf() // Stop the service when the network call fails
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                // Handle response if needed
                stopSelf() // Stop the service when the network call is completed
            }
        })
    }

    companion object {
        const val SLACK_WEBHOOK_URL = "https://hooks.slack.com/services/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"
    }
}
*/
