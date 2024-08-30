package uz.iskandarbek.password_input

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class ConfirmPasswordActivity : AppCompatActivity() {

    private var enteredPassword = ""
    private lateinit var tvPassword: TextView
    private lateinit var tvForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_password)

        tvPassword = findViewById(R.id.tv_password)
        tvForgotPassword = findViewById(R.id.tv_forgot_password)

        // Listener qo'shish
        tvForgotPassword.setOnClickListener {
            onForgotPasswordClick()
        }
    }

    fun onNumberClick(view: View) {
        val button = view as Button
        if (enteredPassword.length < 4) {
            enteredPassword += button.text
            tvPassword.text = enteredPassword
        }
    }

    fun onDeleteClick(view: View) {
        if (enteredPassword.isNotEmpty()) {
            enteredPassword = enteredPassword.dropLast(1)
            tvPassword.text = enteredPassword
        }
    }

    fun onConfirmClick(view: View) {
        val sharedPref = getSharedPreferences("APP_PREF", MODE_PRIVATE)
        val savedPassword = sharedPref.getString("password", "")
        if (enteredPassword == savedPassword) {
            // Keyingi oynaga o'tish
            startActivity(Intent(this, NextActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
        }
    }

    fun onFingerprintClick(view: View) {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
            val executor = ContextCompat.getMainExecutor(this)
            val biometricPrompt =
                BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        startActivity(
                            Intent(
                                this@ConfirmPasswordActivity,
                                NextActivity::class.java
                            )
                        )
                        finish()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(
                            this@ConfirmPasswordActivity,
                            "Barmoq izi skaneri bekor qilindi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(this@ConfirmPasswordActivity, "Xatolik", Toast.LENGTH_SHORT)
                            .show()
                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Barmoq izi skaneri")
                .setDescription("Parolni barmoq izi bilan oching")
                .setNegativeButtonText("Bekor qilish")
                .build()

            biometricPrompt.authenticate(promptInfo)
        } else {
            Toast.makeText(this, "Barmoq izi topilmadi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onForgotPasswordClick() {
        val sharedPref = getSharedPreferences("APP_PREF", MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("password")
            apply()
        }
        startActivity(Intent(this, SetPasswordActivity::class.java))
        finish()
    }
}
