package com.example.user.healthcalendar.Fragments

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.utils.DateUtils.getCalendar
import com.applandeo.materialcalendarview.utils.DateUtils
import com.example.user.healthcalendar.Database.DatabaseContract
import com.example.user.healthcalendar.Database.DbHelper
import com.example.user.healthcalendar.EditEventActivity

import com.example.user.healthcalendar.R
import kotlinx.android.synthetic.main.activity_edit_doctor.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.text.SimpleDateFormat
import java.util.*

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

    //var calendarView : CalendarView? = null
    //var dateDisplay : TextView? = null

    var cursor : Cursor? = null
    var dbHelper : DbHelper? = null
    //var simpleCursorAdapter : SimpleCursorAdapter? = null

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
        fab.setOnClickListener(View.OnClickListener {
            goToEditEventActivity()
        })

        calendarView = view!!.findViewById(R.id.calendarView)
        calendarView?.setDate(getCalendar())
        previewNote(EventDay(getCalendar()))

        calendarTextView = view!!.findViewById(R.id.calendarTextView)
        calendarTextView?.setMovementMethod(ScrollingMovementMethod())

        calendarView?.setOnDayClickListener(OnDayClickListener() { eventDay ->
            previewNote(eventDay)
        })

        var eventDays: MutableList<EventDay>? = mutableListOf<EventDay>()
        /*val calendar : Calendar = Calendar.getInstance()
        eventDays?.add(EventDay(calendar, R.drawable.ic_event_icon))
        val calendar1 : Calendar = Calendar.getInstance()
        calendar1.add(Calendar.DAY_OF_MONTH, 5)
        eventDays?.add(EventDay(calendar1, R.drawable.ic_event_icon))*/

        /*val calendar : Calendar = Calendar.getInstance()
        val pattern : String = "dd/MM/yyyy"
        val format : SimpleDateFormat = SimpleDateFormat(pattern)
        val date : Date = format.parse("18/05/2018")
        calendar.setTime(date)
        eventDays?.add(EventDay(calendar, R.drawable.ic_event_icon))
        val date2 : Date = format.parse("20/05/2018")
        calendar.setTime(date2)
        eventDays?.add(EventDay(calendar, R.drawable.ic_event_icon))
        calendarView?.setEvents(eventDays)*/

        val pattern: String = "dd/MM/yyyy"
        val format: SimpleDateFormat = SimpleDateFormat(pattern)
        val datesToSet = arrayOf<String>("10/05/2018", "11/05/2018", "11/05/2018", "12/05/2018", "09/05/2018")
        for (i in 0..datesToSet.size - 1) {
            var calendar: Calendar = Calendar.getInstance()
            val date: Date = format.parse(datesToSet[i])
            calendar.setTime(date)
            eventDays?.add(EventDay(calendar, R.drawable.ic_event_icon))
        }
        calendarView?.setEvents(eventDays)

        /*calendarView = view!!.findViewById(R.id.calendarView)
        dateDisplay = view!!.findViewById(R.id.dateDisplay)
        //dateDisplay?.setText("Date: ")

        calendarView?.setOnDateChangeListener(CalendarView.OnDateChangeListener() {
            calendarView, year, month, day ->
            dateDisplay?.setText("Date: " + day + "/" + (month + 1) + "/" + year)
        })*/
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

        var k: Int = 0

        var day: String = ""
        var month: String = ""
        var year: String = ""

        var ans : String = ""

        var st: String = ""

        var time = t + ' '

        for(s in time){

            if (s == ' '){
                k++

                if (k == 2){
                    month = monthNumber(st)
                }
                else if (k == 3){
                    day = st
                }
                else if (k == 6){
                    year = st
                }

                st = ""
            }
            else {

                st = st + s

            }
        }
        ans = day + '/' + month + '/' + year
        return ans
    }

    fun previewNote(eventDay: EventDay) {

        var dateString : String = eventDay.calendar.time.toString()
        var s = parserCalendarTime(dateString)

        Log.i("mmmmmm",s)

        var date = getDBdata(s)

        Log.i("DBDBDB",date)

        calendarTextView?.setText(date)
    }

    fun getDoctor(id: Int): String{

        var database = dbHelper!!.getWritableDatabase()
        var ans: String = ""

        val query = "SELECT * FROM " + DatabaseContract.DoctorsColumns.TABLE_NAME +
                " WHERE " + DatabaseContract.DoctorsColumns._ID + "='" + id + "'"

        val cursor : Cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            var idIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns._ID)
            var specialityIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.SPECIALITY)
            var nameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.NAME)
            var surnameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.SURNAME)
            var fathersnameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.FATHERSNAME)
            var addressIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.ADDRESS)
            var contactsIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.CONTACTS)
            var commentIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.COMMENT)

            do{

                val id = cursor.getInt(idIndex)
                val speciality = cursor.getString(specialityIndex)
                val surname = cursor.getString(surnameIndex)
                val name = cursor.getString(nameIndex)
                val fathersname = cursor.getString(fathersnameIndex)
                val comment = cursor.getString(commentIndex)
                val address = cursor.getString(addressIndex)
                val contact = cursor.getString(contactsIndex)

                ans = ans + speciality + " " +
                        surname + " " +
                        name + " " +
                        fathersname + " "
                if (address != null && address != "") ans =  ans + "\n" + "Адрес: " + address
                if (contact != null && contact != "") ans= ans + "\n" + "Контакты: " + contacts
                if (comment != null && comment != "") ans= ans + "\n" + "Комментарий: " + contacts

                Log.i("mLog", "ID = " + cursor.getInt(idIndex)
                        + ", speciality = " + cursor.getString(specialityIndex)
                        + ", name = " + cursor.getString(nameIndex)
                        + ", surname = " + cursor.getString(surnameIndex)
                        + ", fathersname = " + cursor.getString(fathersnameIndex)
                        + ", addressIndex = " + cursor.getString(addressIndex)
                        + ", contactsIndex = " + cursor.getString(contactsIndex)
                        + ", commentIndex = " + cursor.getString(commentIndex))

            } while (cursor.moveToNext())

        } else {
            Log.i("mLog", "0 rows")
        }

        return ans
    }

    fun getDBdata(selectedDay: String): String{

        var database = dbHelper!!.getWritableDatabase()
        var ans: String = ""

        val query = "SELECT * FROM " + DatabaseContract.EventsColumns.TABLE_NAME +
                " WHERE " + DatabaseContract.EventsColumns.DATE + "='" + selectedDay + "'"

        val cursor : Cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            val idIndex: Int = cursor.getColumnIndex(DatabaseContract.EventsColumns._ID)
            val doctorIdIndex: Int = cursor.getColumnIndex(DatabaseContract.EventsColumns.DOCTOR_ID)
            val dateIndex: Int = cursor.getColumnIndex(DatabaseContract.EventsColumns.DATE)
            val timeIndex: Int = cursor.getColumnIndex(DatabaseContract.EventsColumns.TIME)
            val commentIndex: Int = cursor.getColumnIndex(DatabaseContract.EventsColumns.COMMENT)

            do{

                val id = cursor.getInt(idIndex)
                val doctorId = cursor.getString(doctorIdIndex)
                val doctor = getDoctor(doctorId.toInt())
                val date = cursor.getString(dateIndex)
                val time = cursor.getString(timeIndex)
                val comment = cursor.getString(commentIndex)

                ans = ans + "ID = " + id + "\n" +
                        "Врач = " + doctor + "\n" +
                        "Дата = " + date + "\n"

                if (time != null && time != "") ans= ans  + "Время: "    + time + "\n"
                if (comment != null && comment != "") ans= ans  + "Комментарий: " + contacts + "\n"

                ans = ans + "\n"

                Log.i("mLog", "ID = " + cursor.getInt(idIndex)
                        + ", doctorId = " + cursor.getString(doctorIdIndex)
                        + ", date = " + cursor.getString(dateIndex)
                        + ", time = " + cursor.getString(timeIndex)
                        + ", commentIndex = " + cursor.getString(commentIndex))

            } while (cursor.moveToNext())

        } else {
            Log.i("mLog", "0 rows")
        }

        return ans
    }

    fun goToEditEventActivity() {
        val intent : Intent = Intent(activity, EditEventActivity::class.java)
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
