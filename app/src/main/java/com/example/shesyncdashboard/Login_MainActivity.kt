package com.example.shesyncdashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var cycleLengthEditText: EditText
    private lateinit var loginButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)  // Your renamed layout file

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        cycleLengthEditText = findViewById(R.id.cycleLengthEditText)
        loginButton = findViewById(R.id.loginButton)  // Rename appropriately

        loginButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val cycleLengthStr = cycleLengthEditText.text.toString().trim()
            var hasError = false

            if (name.isBlank()) {
                nameEditText.error = "Name is required"
                hasError = true
            }
            if (email.isBlank()) {
                emailEditText.error = "Email is required"
                hasError = true
            }
            if (cycleLengthStr.isBlank()) {
                cycleLengthEditText.error = "Cycle length is required"
                hasError = true
            }

            if (!hasError) {
                val sharedPreferences = getSharedPreferences("PeriodTrackerPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val averageCycleLength = cycleLengthStr.toInt()
                editor.putInt("AVERAGE_CYCLE_LENGTH", averageCycleLength)
                val cycleStartDateMillis = System.currentTimeMillis()
                editor.putLong("CYCLE_START_DATE", cycleStartDateMillis)
                editor.apply()

                val nowMillis = System.currentTimeMillis()
                val diffMillis = nowMillis - cycleStartDateMillis
                val daysPassed = TimeUnit.MILLISECONDS.toDays(diffMillis)
                val currentCycleDay = (daysPassed % averageCycleLength) + 1

                // Navigate to new MainActivity hosting fragments
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("USER_NAME", name)
                    putExtra("USER_EMAIL", email)
                    putExtra("CYCLE_DAY", currentCycleDay)
                    putExtra("CYCLE_LENGTH", averageCycleLength)
                }
                startActivity(intent)
                finish()  // Prevent user going back to login
            }
        }
    }
}
