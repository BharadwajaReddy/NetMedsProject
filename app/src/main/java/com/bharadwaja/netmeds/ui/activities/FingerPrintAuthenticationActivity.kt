package com.bharadwaja.netmeds.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.core.app.ActivityCompat
import com.bharadwaja.netmeds.R

class FingerPrintAuthenticationActivity : AppCompatActivity() {
    private var cancellationSignal: CancellationSignal? = null

    // create an authenticationCallback
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                notifyUser("Authentication Error : $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                notifyUser("Authentication Succeeded")
                //start a search pictures activity
                val startSearchActivityIntent = Intent(
                    this@FingerPrintAuthenticationActivity,
                    SearchInPixabayActivity::class.java
                )
                startActivity(startSearchActivityIntent)
                finish()
            }
        }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hasBiometricCapability(this) != BiometricManager.BIOMETRIC_SUCCESS) {
            //start a search pictures activity
            val startSearchActivityIntent = Intent(
                this@FingerPrintAuthenticationActivity,
                SearchInPixabayActivity::class.java
            )
            startActivity(startSearchActivityIntent)
            finish()
        } else {
            setContentView(R.layout.activity_biometric_authentication_layout)
            checkBiometricSupport()
            // create a biometric dialog on Click of button
            findViewById<Button>(R.id.start_authentication).setOnClickListener {
                val biometricPrompt = BiometricPrompt.Builder(this)
                    .setTitle("Biometric Login")
                    .setSubtitle("Login using finger print biomertic")
                    .setNegativeButton(
                        "Cancel",
                        this.mainExecutor,
                        DialogInterface.OnClickListener { dialog, which ->
                            notifyUser("Authentication Cancelled")
                        }).build()
                biometricPrompt.authenticate(
                    getCancellationSignal(),
                    mainExecutor,
                    authenticationCallback
                )
            }

        }
    }

    // it will be called when authentication is cancelled by the user
    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was Cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    // it checks whether the app the app has fingerprint permission
    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isDeviceSecure) {
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Fingerprint Authentication Permission is not enabled")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun hasBiometricCapability(context: Context): Int {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
    }
}