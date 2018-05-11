package com.example.user.healthcalendar

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.user.healthcalendar.Database.DatabaseContract
import com.example.user.healthcalendar.Database.DbHelper
import java.util.*


class EditEventActivity : AppCompatActivity() {
    private var submit: Button? = null

    private var etDoctor: Spinner? = null
    /*var etDate: EditText? = null
    var etTime: EditText? = null*/
    private var etComment: EditText? = null

    private var dbHelper: DbHelper? = null

    private var dialogTime = 1
    private var hourTextView = 3
    private var minuteTextView = 33
    private var timeTextView: TextView? = null


    private var dialogDate = 2
    private var yearTextView = 2018
    private var monthTextView = 5
    private var dayTextView = 4
    private var dateTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        dbHelper = DbHelper(this)


        val now = Calendar.getInstance()
        hourTextView = now.get(Calendar.HOUR_OF_DAY)
        minuteTextView = now.get(Calendar.MINUTE)

        yearTextView = now.get(Calendar.YEAR)
        monthTextView = now.get(Calendar.MONTH)
        dayTextView = now.get(Calendar.DAY_OF_MONTH)


        etDoctor = findViewById(R.id.event_doctor_spinner)
        /*etDate = findViewById(R.id.event_date)
        etTime = findViewById(R.id.event_time)*/
        etComment = findViewById(R.id.event_comment)
        timeTextView = findViewById(R.id.event_time)
        dateTextView = findViewById(R.id.event_date)


        loadSpinnerData()

        val intent = intent

        val id = intent.getLongExtra("eventId",0)
        Log.i("this id = ", id.toString())

        if (id == 0.toLong()) {
            createEvent()
        }
        else
        {
            Log.i("this id = ", id.toString())
            //editEvent(id)
        }


        timeTextView!!.setOnClickListener({
            onClickTime()
        })


        dateTextView!!.setOnClickListener({
            onClickDate()
        })

    }

    private fun onClickTime() {
         showDialog(dialogTime)
    }

    private fun onClickDate() {
        showDialog(dialogDate)
    }

    override fun onCreateDialog(id: Int): Dialog {

        if (id == dialogTime) {

            return TimePickerDialog(this,
                    timeCallBack,
                    hourTextView,
                    minuteTextView,
            true)
        }

        if (id == dialogDate) {

            return DatePickerDialog(this,
                    dateCallBack,
                    yearTextView,
                    monthTextView,
                    dayTextView)
        }

        return super.onCreateDialog(id)

    }

    private var timeCallBack : TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener(

            fun(_: TimePicker, hourOfDay: Int, minute: Int) {
                hourTextView = hourOfDay
                minuteTextView = minute

                val hourString = if (hourTextView < 10) "0" + hourTextView.toString() else hourTextView.toString()
                val minString = if (minuteTextView < 10) "0" + minuteTextView.toString() else minuteTextView.toString()
                val timeString = "$hourString:$minString"
                timeTextView!!.text = timeString
            }

    )


    private var dateCallBack : DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener(

            fun(_: DatePicker, year : Int, month: Int, day: Int) {

                yearTextView = year

                monthTextView = month + 1
                dayTextView = day

                val yearString = yearTextView.toString()
                val monthString = if (monthTextView < 10) "0" + monthTextView.toString() else monthTextView.toString()
                val dayString = if (dayTextView < 10) "0" + dayTextView.toString() else dayTextView.toString()
                val dateString = "$dayString/$monthString/$yearString"
                dateTextView!!.text = dateString
            }

    )


    private fun loadSpinnerData() {

        val listOfIds = getAllDoctors()
        val dataAdapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listOfIds)

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        this.etDoctor?.adapter = dataAdapter
    }

    fun setSpinnerSelection(spinner: Spinner, array: Array<String>, text: String) {
        for (i in array.indices) {
            if (array[i] == text) {
                spinner.setSelection(i)
            }
        }
    }

    private fun getAllDoctors(): MutableList<String> {

        val doctorsList : MutableList<String> = mutableListOf()

        val database = dbHelper!!.readableDatabase
        val cursor = database.query(DatabaseContract.DoctorsColumns.TABLE_NAME, null, null,
                null, null, null, null)

        if (cursor.moveToFirst()) {

            val idIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns._ID)
            val specialityIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.SPECIALITY)
            val nameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.NAME)
            val surnameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.SURNAME)
            val fathersNameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.FATHERSNAME)
            //var addressIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.ADDRESS)
            //var contactsIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.CONTACTS)
            //var commentIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.COMMENT)

            do {

                doctorsList.add(cursor.getString(specialityIndex)
                        + " "
                        + cursor.getString(surnameIndex)
                        + " "
                        + cursor.getString(nameIndex)
                        + " "
                        + cursor.getString(fathersNameIndex)
                        + " "
                        + cursor.getInt(idIndex)
                )

            } while (cursor.moveToNext())
        }

        cursor.close()
        database.close()

        return doctorsList
    }

    private fun createEvent(){

        submit = findViewById(R.id.submit_event)
        submit!!.setOnClickListener({
            submitNewEvent()
        })

        dbHelper = DbHelper(this)

    }

    private fun parserDoctorSpinner(st: String):Int{

        var ans = ""

        ((st.length-1) downTo 0)
                .asSequence()
                .takeWhile { st[it] != ' ' }
                .forEach { ans += st[it] }

        ans = ans.reversed()

        Log.i("mmmmmmm", ans)

        return ans.toInt()
    }

    private fun submitNewEvent() {

        val doctorSpinnerString = etDoctor!!.selectedItem.toString()

        val date = dateTextView!!.text.toString()
        val time = timeTextView!!.text.toString()

        val comment = etComment!!.text.toString()
        val doctor = parserDoctorSpinner(doctorSpinnerString)

        val database = dbHelper!!.writableDatabase

        val contentValues = ContentValues()

        contentValues.put(DatabaseContract.EventsColumns.DOCTOR_ID, doctor)
        contentValues.put(DatabaseContract.EventsColumns.DATE, date)
        contentValues.put(DatabaseContract.EventsColumns.TIME, time)
        contentValues.put(DatabaseContract.EventsColumns.COMMENT, comment)

        database.insert(DatabaseContract.EventsColumns.TABLE_NAME, null, contentValues)


        val cursor : Cursor = database.query(DatabaseContract.EventsColumns.TABLE_NAME, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            val idIndex: Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns._ID)
            val doctorIndex = cursor.getColumnIndex(DatabaseContract.EventsColumns.DOCTOR_ID)
            val dateIndex = cursor.getColumnIndex(DatabaseContract.EventsColumns.DATE)
            val timeIndex = cursor.getColumnIndex(DatabaseContract.EventsColumns.TIME)
            val commentIndex = cursor.getColumnIndex(DatabaseContract.EventsColumns.COMMENT)

            do {
                Log.i("mLog", "ID = " + cursor.getInt(idIndex)
                        + ", doctor = " + cursor.getString(doctorIndex)
                        + ", date = " + cursor.getString(dateIndex)
                        + ", time = " + cursor.getString(timeIndex)
                        + ", comment = " + cursor.getString(commentIndex))
            } while (cursor.moveToNext())
        } else {
            Log.i("mLog", "0 rows")
        }
        cursor.close()

        dbHelper?.close()

        super.onBackPressed()

    }
}
