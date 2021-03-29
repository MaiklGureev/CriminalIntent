package ru.gureev.criminalintent.pickers

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_TIME = "time"

class TimePickerFragment : DialogFragment() {

    companion object {
        fun newInstance(time: Date): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, time)
            }
            return TimePickerFragment().apply {
                arguments = args
            }
        }
    }

    interface Callbacks {
        fun onTimeSelected(time: Date)
    }

    private val onTimeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        val resultTime: Date = GregorianCalendar(
            0,
            0,
            0,
            hourOfDay,
            minute,
            0
        ).time
        targetFragment?.let { fragment ->
            (fragment as Callbacks).onTimeSelected(resultTime)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val time = arguments?.getSerializable(ARG_TIME) as Date
        return TimePickerDialog(
            requireContext(),
            theme,
            onTimeListener,
            time.hours,
            time.minutes,
            true
        )
    }
}