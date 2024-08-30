package uz.iskandarbek.password_input

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sharedPref = getSharedPreferences("APP_PREF", MODE_PRIVATE)
        val savedPassword = sharedPref.getString("password", "")

        if (savedPassword.isNullOrEmpty()) {

            startActivity(Intent(this, SetPasswordActivity::class.java))
        } else {

            startActivity(Intent(this, ConfirmPasswordActivity::class.java))
        }

        finish()
    }
}
