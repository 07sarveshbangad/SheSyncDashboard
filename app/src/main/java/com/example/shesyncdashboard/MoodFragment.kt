package com.example.shesyncdashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

data class Mood(val name: String, val iconRes: Int)

class MoodFragment : Fragment() {
    private lateinit var moodContainer: GridLayout

    private val moodList = listOf(
        Mood("Happy", R.drawable.ic_mood_happy),
        Mood("Sad", R.drawable.ic_mood_sad),
        Mood("Stressed", R.drawable.ic_mood_stressed),
        Mood("Angry", R.drawable.ic_mood_angry),
        Mood("Energetic", R.drawable.ic_mood_energy)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mood, container, false)
        moodContainer = view.findViewById(R.id.moodContainer)

        moodList.forEach { mood ->
            val moodView = inflater.inflate(R.layout.item_mood_icon, moodContainer, false)
            val iconImageView = moodView.findViewById<ImageView>(R.id.moodIcon)
            val labelTextView = moodView.findViewById<TextView>(R.id.moodLabel)

            iconImageView.setImageResource(mood.iconRes)
            labelTextView.text = mood.name

            moodView.setOnClickListener {
                saveMood(mood.name)
                showMoodAnalysis()
            }

            moodContainer.addView(moodView)
        }

        return view
    }

    private fun saveMood(moodName: String) {
        val prefs = requireActivity().getSharedPreferences("MoodPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("latest_mood", moodName).apply()
    }

    private fun showMoodAnalysis() {
        val prefs = requireActivity().getSharedPreferences("MoodPrefs", Context.MODE_PRIVATE)
        val recentMood = prefs.getString("latest_mood", "No mood logged")
        Toast.makeText(requireContext(), "Your recent mood: $recentMood", Toast.LENGTH_SHORT).show()
    }
}
