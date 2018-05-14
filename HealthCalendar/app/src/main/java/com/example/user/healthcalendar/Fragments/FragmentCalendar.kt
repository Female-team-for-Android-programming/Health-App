package com.example.user.healthcalendar.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.utils.DateUtils.getCalendar
import com.example.user.healthcalendar.Database.DatabaseContract
import com.example.user.healthcalendar.Database.DbHelper
import com.example.user.healthcalendar.EditEventActivity
//import com.example.user.healthcalendar.EventsListActivity

import com.example.user.healthcalendar.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentCalendar.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentCalendar.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentCalendar : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    var calendarView : CalendarView? = null
    var calendarTextView : TextView? = null
    var calendarButton : Button? = null

    private var cursor : Cursor? = null
    private var dbHelper : DbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
        dbHelper = DbHelper(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        cursor?.close()
        dbHelper?.close()
    }

    override fun onResume() {
        super.onResume()
        val fab: FloatingActionButton = view!!.findViewById(R.id.fab_calendar)
        fab.setOnClickListener({
            goToEditEventActivity()
        })

        calendarView = view!!.findViewById(R.id.calendarView)
        calendarView?.setDate(getCalendar())
        previewNote(EventDay(getCalendar()))

        calendarTextView = view!!.findViewById(R.id.calendarTextView)
        //calendarTextView?.movementMethod = ScrollingMovementMethod()

        calendarButton = view!!.findViewById(R.id.calendarSeeEventsButton)

        calendarView?.setOnDayClickListener({ eventDay ->
            previewNote(eventDay)
            calendarButton?.setOnClickListener({
                showEventsAlert(parserCalendarTime(eventDay.calendar.time.toString()))
                //goToEventsListActivity(parserCalendarTime(eventDay.calendar.time.toString()))
            })
        })

        showEvents()
    }

    private fun showEventsAlert(date: String) {
        val message = getDBdata(date)
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.setTitle("Записи")
        alertDialog.setMessage(message)

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete)

        alertDialog.setNeutralButton("OK", { dialog, _ ->
            dialog.cancel()
        })
        alertDialog.show()
    }

    /*private fun goToEventsListActivity(date: String) {
        val intent = Intent(activity, EventsListActivity::class.java)
        intent.putExtra("date", date)
        startActivity(intent)
    }*/

    private fun showEvents() {
        val eventDays: MutableList<EventDay>? = mutableListOf()

        val database = dbHelper!!.readableDatabase
        val cursor = database.query(DatabaseContract.EventsColumns.TABLE_NAME, null, null,
                null, null, null, null)
        if (cursor.count == 0) {
            //there is no events yet
            return
        }
        val datesList : MutableList<String>? = mutableListOf()
        val datePattern : Pattern = Pattern.compile("\\d{2}/\\d{2}/\\d{4}")
        if (cursor!!.moveToFirst()) {
            val dateIndex : Int = cursor.getColumnIndex(DatabaseContract.EventsColumns.DATE)

            do {
                val date = cursor.getString(dateIndex)
                if (datePattern.matcher(date).matches()) {
                    datesList?.add(date)
                }
            } while (cursor.moveToNext())
        }

        val pattern = "dd/MM/yyyy"
        val format = SimpleDateFormat(pattern, Locale.US)
        for (i in 0 until datesList!!.size) {
            val calendar: Calendar = Calendar.getInstance()
            val date: Date = format.parse(datesList[i])
            calendar.time = date
            eventDays?.add(EventDay(calendar, R.drawable.ic_event_icon))
        }
        calendarView?.setEvents(eventDays)

    }

    private fun monthNumber(m: String): String{

        if (m == "Jan") return "01"
        if (m == "Feb") return "02"
        if (m == "Mar") return "03"
        if (m == "Apr") return "04"
        if (m == "May") return "05"
        if (m == "Jun") return "06"
        if (m == "Jul") return "07"
        if (m == "Aug") return "08"
        if (m == "Sep") return "09"
        if (m == "Oct") return "10"
        if (m == "Nov") return "11"
        if (m == "Dec") return "12"

        return "0"

    }

    private fun parserCalendarTime(t: String): String {

        var k = 0

        var day = ""
        var month = ""
        var year = ""

        var st = ""

        val time = "$t "

        for(s in time) {

            if (s == ' ') {
                k++

                when (k) {
                    2 -> month = monthNumber(st)
                    3 -> day = st
                    6 -> year = st
                }

                st = ""
            } else {
                st += s
            }
        }
        return "$day/$month/$year"
    }

    private fun previewNote(eventDay: EventDay) {

        val dateString : String = eventDay.calendar.time.toString()
        val s = parserCalendarTime(dateString)

        var messageToShow = s + "\n"

        val database = dbHelper!!.readableDatabase
        val query = "SELECT * FROM " + DatabaseContract.EventsColumns.TABLE_NAME +
                " WHERE " + DatabaseContract.EventsColumns.DATE + "='" + s + "'"
        val cursor : Cursor = database.rawQuery(query, null)
        if (cursor.count == 0) {
            messageToShow += "Нет записей"
            calendarButton?.visibility = View.GONE
        } else {
            messageToShow += "Количество записей: " + cursor.count
            calendarButton?.visibility = View.VISIBLE
        }
        //Log.i("mmmmmm",s)
        //val data = getDBdata(s)
        //Log.i("DBDBDB",data)

        calendarTextView?.text = messageToShow
    }

    private fun getDoctor(id: Int): String{

        val database = dbHelper!!.readableDatabase
        var ans = ""

        val query = "SELECT * FROM " + DatabaseContract.DoctorsColumns.TABLE_NAME +
                " WHERE " + DatabaseContract.DoctorsColumns._ID + "='" + id + "'"

        val cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            val idIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns._ID)
            val specialityIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.SPECIALITY)
            val nameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.NAME)
            val surnameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.SURNAME)
            val fathersnameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.FATHERSNAME)
            val addressIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.ADDRESS)
            val contactsIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.CONTACTS)
            val commentIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.COMMENT)

            do{

                val docId = cursor.getInt(idIndex)
                val speciality = cursor.getString(specialityIndex)
                val name = cursor.getString(nameIndex)
                val surname = cursor.getString(surnameIndex)
                val fathersname = cursor.getString(fathersnameIndex)
                val address = cursor.getString(addressIndex)
                val contacts = cursor.getString(contactsIndex)
                val comment = cursor.getString(commentIndex)

                ans = ans + speciality + " " +
                        surname + " " +
                        name + " " +
                        fathersname + " "
                if (address != null && address != "") ans = "$ans\nАдрес: $address"
                if (contacts != null && contacts != "") ans = "$ans\nКонтакты: $contacts"
                if (comment != null && comment != "") ans= "$ans\nКомментарий: $comment"

                Log.i("mLog", "ID = " + docId
                        + ", speciality = " + speciality
                        + ", name = " + name
                        + ", surname = " + surname
                        + ", fathersname = " + fathersname
                        + ", address = " + address
                        + ", contacts = " + contacts
                        + ", comment = " + comment)

            } while (cursor.moveToNext())

        } else {
            Log.i("mLog", "0 rows")
        }

        return ans
    }

    private fun getDBdata(selectedDay: String): String{

        val database = dbHelper!!.readableDatabase
        var ans = ""

        val query = "SELECT * FROM " + DatabaseContract.EventsColumns.TABLE_NAME +
                " WHERE " + DatabaseContract.EventsColumns.DATE + "='" + selectedDay + "'"

        val cursor : Cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            val idIndex: Int = cursor.getColumnIndex(DatabaseContract.EventsColumns._ID)
            val doctorIdIndex: Int = cursor.getColumnIndex(DatabaseContract.EventsColumns.DOCTOR_ID)
            val dateIndex: Int = cursor.getColumnIndex(DatabaseContract.EventsColumns.DATE)
            val timeIndex: Int = cursor.getColumnIndex(DatabaseContract.EventsColumns.TIME)
            val visitedIndex: Int = cursor.getColumnIndex(DatabaseContract.EventsColumns.VISITED)
            val commentIndex: Int = cursor.getColumnIndex(DatabaseContract.EventsColumns.COMMENT)

            do{

                val id = cursor.getInt(idIndex)
                val doctorId = cursor.getString(doctorIdIndex)
                val doctor = getDoctor(doctorId.toInt())
                val date = cursor.getString(dateIndex)
                val time = cursor.getString(timeIndex)
                val visited = cursor.getInt(visitedIndex)
                val comment = cursor.getString(commentIndex)

                ans = ans + "ID = " + id + "\n" +
                        "Врач = " + doctor + "\n" +
                        "Дата = " + date + "\n"

                if (time != null && time != "") {
                    ans = ans + "Время: " + time + "\n"
                }
                if (comment != null && comment != "") {
                    ans = ans + "Комментарий: " + comment + "\n"
                }

                if (visited == 0)
                    ans = ans + "Не посещен\n"
                else
                    ans = ans + "Посещен\n"

                ans += "\n"

                Log.i("mLog", "ID = " + id
                        + ", doctorId = " + doctorId
                        + ", date = " + date
                        + ", time = " + time
                        + ", visited = " + visited
                        + ", comment = " + comment)

            } while (cursor.moveToNext())

        } else {
            Log.i("mLog", "0 rows")
        }

        return ans
    }

    private fun goToEditEventActivity() {
        val intent = Intent(activity, EditEventActivity::class.java)
        startActivity(intent)
    }

    fun goToEditEventActivity(id: Long) {
        val intent = Intent(activity, EditEventActivity::class.java)
        intent.putExtra("eventId", id)
        startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_calendar, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    /*override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }*/

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentCalendar.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): FragmentCalendar {
            val fragment = FragmentCalendar()
            val args = Bundle()
            args.putString(this.ARG_PARAM1, param1)
            args.putString(this.ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
