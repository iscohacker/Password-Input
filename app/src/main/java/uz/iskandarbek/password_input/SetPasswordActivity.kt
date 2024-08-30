package uz.iskandarbek.password_input

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SetPasswordActivity : AppCompatActivity() {

    private var password = ""
    private lateinit var tvPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_password)

        tvPassword = findViewById(R.id.tv_password)
    }

    fun onNumberClick(view: View) {
        val button = view as Button
        if (password.length < 4) {
            password += button.text
            tvPassword.text = password
        }
    }

    fun onDeleteClick(view: View) {
        if (password.isNotEmpty()) {
            password = password.dropLast(1)
            tvPassword.text = password
        }
    }

    fun onConfirmClick(view: View) {
        if (password.length == 4) {
            val sharedPref = getSharedPreferences("APP_PREF", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("password", password)
                apply()
            }
            // Ikkinchi oynaga o'tish
            startActivity(Intent(this, ConfirmPasswordActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Please enter a 4-digit password", Toast.LENGTH_SHORT).show()
        }
    }
}
