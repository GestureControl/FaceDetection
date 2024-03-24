package com.example.facedetection

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

class BiometricUtils(private val activity: FragmentActivity) {

    fun isBiometricSupported(): Boolean {
        val biometricManager = BiometricManager.from(activity)
        return when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun showBiometricPrompt(
        title: String,
        subtitle: String,
        description: String,
    ) {
        val executor: Executor = activity.mainExecutor
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        activity.applicationContext,
                        "login successful",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(activity.applicationContext, "login Failed", Toast.LENGTH_LONG)
                        .show()

                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == 10)
                        Toast.makeText(
                            activity.applicationContext,
                            "Authentication cancelled",
                            Toast.LENGTH_SHORT
                        ).show()
                    else
                        Toast.makeText(
                            activity.applicationContext,
                            "Authentication error: $errString ",
                            Toast.LENGTH_SHORT
                        ).show()

                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setConfirmationRequired(true)
            .setDeviceCredentialAllowed(true)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    interface BiometricCallback {
        fun onAuthenticationSuccessful()
        fun onAuthenticationFailed()
        fun onAuthenticationError(errorCode: Int, errorMessage: String)
    }
}
