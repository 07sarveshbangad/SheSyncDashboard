package com.example.shesyncdashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private lateinit var startPeriodButton: Button
    private lateinit var fertileDateText: TextView
    private lateinit var ovulationDateText: TextView
    private lateinit var periodDateText: TextView
    private lateinit var myCycles: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        startPeriodButton = view.findViewById(R.id.startPeriodButton)
        fertileDateText = view.findViewById(R.id.fertileDate)
        ovulationDateText = view.findViewById(R.id.ovulationDate)
        periodDateText = view.findViewById(R.id.periodDate)
        myCycles = view.findViewById(R.id.myCycles)

        startPeriodButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CalendarFragment())
                .addToBackStack(null)
                .commit()
        }

        // Initialization and displaying cycle info
        val prefs = requireActivity().getSharedPreferences("PeriodTrackerPrefs", Context.MODE_PRIVATE)
        val cycleLength = prefs.getInt("AVERAGE_CYCLE_LENGTH", 28)
        val cycleStart = prefs.getLong("CYCLE_START_DATE", System.currentTimeMillis())
        val cyclesList = prefs.getString("CYCLES_LIST", "")

        val phaseDates = generatePhaseInfo(cycleStart, cycleLength)
        fertileDateText.text = phaseDates.nextFertile
        ovulationDateText.text = phaseDates.ovulation
        periodDateText.text = phaseDates.nextPeriod

        myCycles.text = if (!cyclesList.isNullOrEmpty()) cyclesList else "No previous cycles"

        return view
    }

    data class PhaseDates(val nextFertile: String, val ovulation: String, val nextPeriod: String)

    private fun generatePhaseInfo(startMillis: Long, cycleLength: Int): PhaseDates {
        val msPerDay = 86400000L
        val fertileStart = startMillis + msPerDay * (cycleLength - 18)
        val fertileEnd = startMillis + msPerDay * (cycleLength - 11)
        val ovulationDay = startMillis + msPerDay * (cycleLength - 14)
        val nextPeriod = startMillis + msPerDay * cycleLength

        return PhaseDates(
            nextFertile = "${formatDate(fertileStart)} - ${formatDate(fertileEnd)}",
            ovulation = formatDate(ovulationDay),
            nextPeriod = formatDate(nextPeriod)
        )
    }

    private fun formatDate(ms: Long): String {
        val sdf = java.text.SimpleDateFormat("MMM d")
        return sdf.format(java.util.Date(ms))
    }
}
