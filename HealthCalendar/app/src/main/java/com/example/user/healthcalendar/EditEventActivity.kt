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
import java.text.SimpleDateFormat
import java.util.*


class EditEventActivity : AppCompatActivity() {
    var submit: Button? = null

    var etDoctor: Spinner? = null
    /*var etDate: EditText? = null
    var etTime: EditText? = null*/
    var etComment: EditText? = null

    var dbHelper: DbHelper? = null

    var DIALOG_TIME = 1
    var hourTextView = 3
    var minuteTextView = 33
    var timeTextView: TextView? = null


    var DIALOG_DATE = 2
    var yearTextView = 2018
    var monthTextView = 5
    var dayTextView = 4
    var dateTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        dbHelper = DbHelper(this)


        var now = Calendar.getInstance()
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


        timeTextView!!.setOnClickListener(View.OnClickListener {
            onClickTime()
        })


        dateTextView!!.setOnClickListener(View.OnClickListener {
            onClickDate()
        })

    }

    public fun onClickTime() {
         showDialog(DIALOG_TIME)
    }

    public fun onClickDate() {
        showDialog(DIALOG_DATE)
    }

    override fun onCreateDialog(id: Int): Dialog {

        if (id == DIALOG_TIME) {

            var tpd : TimePickerDialog
                    = TimePickerDialog(this,
                    timeCallBack,
                    hourTextView,
                    minuteTextView,
                    true)

            return tpd
        }

        if (id == DIALOG_DATE) {

            var datePickerDate : DatePickerDialog
                    = DatePickerDialog(this,
                    dateCallBack,
                    yearTextView,
                    monthTextView,
                    dayTextView)

            return datePickerDate
        }

        return super.onCreateDialog(id)

    }

    var timeCallBack : TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener(

            fun(view: TimePicker, hourOfDay: Int, minute: Int) {
                hourTextView = hourOfDay
                minuteTextView = minute
                var timeString = hourTextView.toString() + ":" + minuteTextView.toString()
                timeTextView!!.setText(timeString)
            }

    )


    var dateCallBack : DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener(

            fun(view: DatePicker, year : Int, month: Int, day: Int) {
                yearTextView = year
                //TODO : month + 1 because it counts from 0
                //TODO : maybe need to do something more sophisticated???
                monthTextView = month + 1
                dayTextView = day

                val yearString = yearTextView.toString()
                val monthString = if (monthTextView < 10) "0" + monthTextView.toString() else monthTextView.toString()
                val dayString = if (dayTextView < 10) "0" + dayTextView.toString() else dayTextView.toString()
                var dateString = dayString + "/" + monthString + "/" + yearString
                dateTextView!!.setText(dateString)
            }

    )

    //TODO : The code below was attempt to set selection of doctors in database to spinner
    //TODO : it does not work and the whole app restarts after clicking submit event button :)

    fun loadSpinnerData() {

        val list_of_ids = getAllDoctors()
        val dataAdapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_of_ids)

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        etDoctor?.setAdapter(dataAdapter);
    }

    fun SetSpinnerSelection(spinner: Spinner, array: Array<String>, text: String) {
        for (i in array.indices) {
            if (array[i] == text) {
                spinner.setSelection(i)
            }
        }
    }

    fun getAllDoctors(): ArrayList<String> {

        var doctorsList = ArrayList<String>();

        val database = dbHelper!!.readableDatabase
        var cursor = database.query(DatabaseContract.DoctorsColumns.TABLE_NAME, null, null,
                null, null, null, null)

        if (cursor.moveToFirst()) {

            var idIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns._ID)
            var specialityIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.SPECIALITY)
            var nameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.NAME)
            var surnameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.SURNAME)
            var fathersnameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.FATHERSNAME)
            var addressIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.ADDRESS)
            var contactsIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.CONTACTS)
            var commentIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.COMMENT)

            do {

                doctorsList.add(cursor.getString(specialityIndex)
                        + " "
                        + cursor.getString(surnameIndex)
                        + " "
                        + cursor.getString(nameIndex)
                        + " "
                        + cursor.getString(fathersnameIndex)
                        + " "
                        + cursor.getInt(idIndex)
                )

            } while (cursor.moveToNext())
        }

        cursor.close()
        database.close()

        return doctorsList
    }



    //TODO : and below everything is fine, I hope

    fun createEvent(){

        submit = findViewById<Button>(R.id.submit_event)
        submit!!.setOnClickListener(View.OnClickListener {
            submitNewEvent()
        })

        dbHelper = DbHelper(this)

    }

    fun parserDoctorSpinner(st: String):Int{

        var ans: String = ""
        var k: Int

        ((st.length-1) downTo 0)
                .asSequence()
                .takeWhile { st[it] != ' ' }
                .forEach { ans = ans + st[it] }

        ans = ans.reversed()

        Log.i("mmmmmmm", ans)

        return ans.toInt()
    }

    private fun submitNewEvent() {

        var doctorSpinnerString = etDoctor!!.selectedItem.toString()

        var date = dateTextView!!.getText().toString()
        var time = timeTextView!!.getText().toString()

        var comment = etComment!!.getText().toString()
        var doctor = parserDoctorSpinner(doctorSpinnerString)

        var database = dbHelper!!.getWritableDatabase()

        var contentValues = ContentValues()

        contentValues.put(DatabaseContract.EventsColumns.DOCTOR_ID, doctor)
        contentValues.put(DatabaseContract.EventsColumns.DATE, date)
        contentValues.put(DatabaseContract.EventsColumns.TIME, time)
        contentValues.put(DatabaseContract.EventsColumns.COMMENT, comment)

        database.insert(DatabaseContract.EventsColumns.TABLE_NAME, null, contentValues)


        //TODO : it crushes after the query to EventsColumns and I don't know why
        var cursor : Cursor = database.query(DatabaseContract.EventsColumns.TABLE_NAME, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            var idIndex: Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns._ID)
            var doctorIndex = cursor.getColumnIndex(DatabaseContract.EventsColumns.DOCTOR_ID)
            var dateIndex = cursor.getColumnIndex(DatabaseContract.EventsColumns.DATE)
            var timeIndex = cursor.getColumnIndex(DatabaseContract.EventsColumns.TIME)
            var commentIndex = cursor.getColumnIndex(DatabaseContract.EventsColumns.COMMENT)

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
