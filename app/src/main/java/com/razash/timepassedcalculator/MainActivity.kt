package com.razash.timepassedcalculator

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var factor = 1
    private var lastSelectedDay = 14
    private var lastSelectedMonth = 4
    private var lastSelectedYear = 1948

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dateButton.setOnClickListener { view -> clickDatePicker(view) }

        val options = arrayOf("שניות", "דקות", "שעות", "ימים", "שבועות", "חודשים", "שנים")

        spinner.adapter =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                sp_result.text = "ימים עברו מהתאריך הנבחר"
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var opt: String = options[p2]

                when(opt) {
                    "שניות" -> factor = 1
                    "דקות" -> factor = 60
                    "שעות" -> factor = 1440
                    "ימים" -> factor = 86400
                    "שבועות" -> factor = 604800
                    "חודשים" -> factor = 2638000
                    "שנים" -> factor = 2
                }

                opt += " עברו מהתאריך הנבחר"
                sp_result.text = opt

                calculateAndShowDate()
            }

        }
    }

    private fun clickDatePicker(view: View) {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                    lastSelectedDay = selectedDay
                    lastSelectedMonth = selectedMonth
                    lastSelectedYear = selectedYear
                    calculateAndShowDate()
                },year, month, day)
        dpd.datePicker.maxDate = Date().time
        dpd.show()

    }

    fun calculateAndShowDate() {
        if(factor == 2) { // factor in Years
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val diffInYears = currentYear - lastSelectedYear
            tvSelectedDateInTime.text = diffInYears.toString()
        }
        else {
            val selectedDate = "$lastSelectedDay/${lastSelectedMonth+1}/$lastSelectedYear"
            tvSelectedDate.text = selectedDate
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val theDate = sdf.parse(selectedDate)
            val selectedDateInTime = (theDate!!.time / factor) / 1000
            val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
            val currentDateInTime = (currentDate!!.time / factor) / 1000
            val differenceInTime = currentDateInTime - selectedDateInTime
            tvSelectedDateInTime.text = differenceInTime.toString()
        }

    }
}
