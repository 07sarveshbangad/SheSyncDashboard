package com.example.shesyncdashboard

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var welcomeMessageTextView: TextView
    private lateinit var cycleDayNumberTextView: TextView

    private lateinit var iconHome: ImageButton
    private lateinit var iconCalendar: ImageButton
    private lateinit var iconSelfCare: ImageButton
    private lateinit var iconProfile: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        welcomeMessageTextView = findViewById(R.id.welcomeMessageTextView)
        cycleDayNumberTextView = findViewById(R.id.cycleDayNumber)

        iconHome = findViewById(R.id.iconHome)
        iconCalendar = findViewById(R.id.iconCalendar)
        iconSelfCare = findViewById(R.id.iconSelfCare)
        iconProfile = findViewById(R.id.iconProfile)

        val userName = intent.getStringExtra("USER_NAME")
        val cycleLength = intent.getIntExtra("CYCLE_LENGTH", 28)

        welcomeMessageTextView.text = "Hello, ${userName ?: "there"}"
        cycleDayNumberTextView.text = cycleLength.toString()

        iconHome.setOnClickListener {
            replaceFragment(HomeFragment())
        }
        iconCalendar.setOnClickListener {
            replaceFragment(CalendarFragment())
        }
        iconSelfCare.setOnClickListener {
            replaceFragment(SelfCareFragment())
        }
        iconProfile.setOnClickListener {
            replaceFragment(ProfileFragment())
        }

        // Load HomeFragment by default
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
