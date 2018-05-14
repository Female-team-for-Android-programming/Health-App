package com.example.user.healthcalendar.Fragments

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.*
import com.example.user.healthcalendar.Database.DatabaseContract
import com.example.user.healthcalendar.Database.DbHelper

import com.example.user.healthcalendar.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentHome.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentHome.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentHome : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null


    private var eventsListView : ListView? = null
    private var eventsEmpty : TextView? = null

    private var cursor : Cursor? = null
    private var dbHelper : DbHelper? = null
    private var simpleCursorAdapter : SimpleCursorAdapter? = null

    private var makeVisited: Button? = null

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

        eventsListView = view?.findViewById(R.id.home_listview)
        eventsEmpty = view?.findViewById(R.id.home_list_empty_textview)

        makeVisited = view?.findViewById(R.id.visited_event)



        showDBdata()

        registerForContextMenu(eventsListView)

    }


    //TODO: add R.menu.home_context_menu to R.java


    /*override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = activity.menuInflater
        inflater.inflate(R.menu.home_context_menu, menu)
    }*/


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view : View = inflater!!.inflate(R.layout.fragment_home, container, false)
        val fab : FloatingActionButton = view.findViewById(R.id.fab_home)
        fab.setOnClickListener({
            Toast.makeText(activity.applicationContext, "Action for Home Fragment", Toast.LENGTH_SHORT).show()
        })

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    private fun makeEventVisited(id: Int){


        val database = dbHelper!!.writableDatabase

        val contentValues = ContentValues()

        val query = "SELECT * FROM " + DatabaseContract.EventsColumns.TABLE_NAME +
                " WHERE " + DatabaseContract.EventsColumns._ID + "='" + id + "'"

        val cursor = database.rawQuery(query, null)

        if (cursor!!.moveToFirst()) {

                val idIndex: Int = cursor!!.getColumnIndex(DatabaseContract.EventsColumns._ID)
                val doctorIdIndex: Int = cursor!!.getColumnIndex(DatabaseContract.EventsColumns.DOCTOR_ID)
                val dateIndex: Int = cursor!!.getColumnIndex(DatabaseContract.EventsColumns.DATE)
                val timeIndex: Int = cursor!!.getColumnIndex(DatabaseContract.EventsColumns.TIME)
                val visitedIndex: Int = cursor!!.getColumnIndex(DatabaseContract.EventsColumns.VISITED)
                val commentIndex: Int = cursor!!.getColumnIndex(DatabaseContract.EventsColumns.COMMENT)

                do{

                    val id = cursor!!.getInt(idIndex)
                    val doctorId = cursor!!.getString(doctorIdIndex)
                    val date = cursor!!.getString(dateIndex)
                    val time = cursor!!.getString(timeIndex)
                    val visited = cursor!!.getInt(visitedIndex)
                    val comment = cursor!!.getString(commentIndex)

                    Log.i("LLLLLL", "ID = " + id
                            + ", doctorId = " + doctorId
                            + ", date = " + date
                            + ", time = " + time
                            + ", visited = " + visited
                            + ", comment = " + comment)


                    contentValues.put(DatabaseContract.EventsColumns._ID, id)
                    contentValues.put(DatabaseContract.EventsColumns.DOCTOR_ID, doctorId)
                    contentValues.put(DatabaseContract.EventsColumns.DATE, date)
                    contentValues.put(DatabaseContract.EventsColumns.TIME, time)
                    contentValues.put(DatabaseContract.EventsColumns.VISITED, 1)
                    contentValues.put(DatabaseContract.EventsColumns.COMMENT, comment)

                    database.update(DatabaseContract.DoctorsColumns.TABLE_NAME, contentValues,
                            DatabaseContract.DoctorsColumns._ID + "=" + id, null)
                } while (cursor!!.moveToNext())

            } else {
                Log.i("mLog", "0 rows")
            }

        showDBdata()
    }


    private fun getDoctor(id: Int): MutableList<String>{

        val database = dbHelper!!.readableDatabase
        var ans :MutableList<String> = mutableListOf()

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

                ans.add(docId.toString())
                ans.add(speciality)
                ans.add(name)
                ans.add(surname)
                ans.add(fathersname)
                ans.add(address)
                ans.add(contacts)
                ans.add(comment)

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


    private fun showDBdata() {

        val date : String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        Log.i("CURRENT DATE", date)

        val database = dbHelper!!.readableDatabase
        val doctorSpecialities : MutableList<String> = mutableListOf()
        val doctorNames : MutableList<String> = mutableListOf()
        val doctorSurnames : MutableList<String> = mutableListOf()
        val doctorFathersnames : MutableList<String> = mutableListOf()
        val doctorAddresses : MutableList<String> = mutableListOf()
        val doctorContacts : MutableList<String> = mutableListOf()
        val doctorComments : MutableList<String> = mutableListOf()

        //TODO: show only today's events

        val query = "SELECT * FROM " + DatabaseContract.EventsColumns.TABLE_NAME +
                " WHERE " + DatabaseContract.EventsColumns.DATE + "='" + date + "'"

        val cursor = database.rawQuery(query, null)

        val events = mutableListOf<Map<String, String>>()

        if (cursor?.count == 0) {

            eventsListView?.visibility = View.GONE
            eventsEmpty?.visibility = View.VISIBLE
        }
        else {
            if (cursor!!.moveToFirst()) {

                val idIndex: Int = cursor!!.getColumnIndex(DatabaseContract.EventsColumns._ID)
                val doctorIdIndex: Int = cursor!!.getColumnIndex(DatabaseContract.EventsColumns.DOCTOR_ID)
                val dateIndex: Int = cursor!!.getColumnIndex(DatabaseContract.EventsColumns.DATE)
                val timeIndex: Int = cursor!!.getColumnIndex(DatabaseContract.EventsColumns.TIME)
                val visitedIndex: Int = cursor!!.getColumnIndex(DatabaseContract.EventsColumns.VISITED)
                val commentIndex: Int = cursor!!.getColumnIndex(DatabaseContract.EventsColumns.COMMENT)

                do{

                    val id = cursor.getInt(idIndex)
                    val doctorId = cursor.getString(doctorIdIndex)
                    val doctor = getDoctor(doctorId.toInt())
                    val date = cursor.getString(dateIndex)
                    val time = cursor.getString(timeIndex)
                    val visited = cursor.getInt(visitedIndex)
                    val comment = cursor.getString(commentIndex)


                   /* val curEventId = mutableMapOf<String, String>()
                    val curEventTime = mutableMapOf<String, String>()
                    val curEventSpeciality = mutableMapOf<String, String>()
                    val curEventName = mutableMapOf<String, String>()
                    val curEventSurname = mutableMapOf<String, String>()
                    val curEventFathersname = mutableMapOf<String, String>()
                    val curEventAddress = mutableMapOf<String, String>()
                    val curEventContacts = mutableMapOf<String, String>()
                    val curEventCommentDoctor = mutableMapOf<String, String>()
                    val curEventCommentEvent = mutableMapOf<String, String>()


                    curEventId[DatabaseContract.EventsColumns._ID] = id.toString()
                    curEventTime[DatabaseContract.EventsColumns.TIME] = time.toString()
                    curEventSpeciality["speciality"] = doctor[1]
                    curEventName["name"] = doctor[2]
                    curEventSurname["surname"] = doctor[3]
                    curEventFathersname["fathersname"] = doctor[4]
                    curEventAddress["address"] = doctor[5]
                    curEventContacts["contacts"] = doctor[6]
                    curEventCommentDoctor["comment_doctor"] = doctor[7]
                    curEventCommentEvent[DatabaseContract.EventsColumns.COMMENT] = comment.toString()

                    events.add(curEventId)
                    events.add(curEventTime)
                    events.add(curEventSpeciality)
                    events.add(curEventName)
                    events.add(curEventSurname)
                    events.add(curEventFathersname)
                    events.add(curEventAddress)
                    events.add(curEventContacts)
                    events.add(curEventCommentDoctor)
                    events.add(curEventCommentEvent)
                    */

                    val currEvent  = mutableMapOf<String, String>()


                    currEvent[DatabaseContract.EventsColumns._ID] = id.toString()
                    currEvent[DatabaseContract.EventsColumns.TIME] = time.toString()
                    currEvent["speciality"] = doctor[1]
                    currEvent["name"] = doctor[2]
                    currEvent["surname"] = doctor[3]
                    currEvent["fathersname"] = doctor[4]
                    currEvent["address"] = doctor[5]
                    currEvent["contacts"] = doctor[6]
                    currEvent["comment_doctor"] = doctor[7]
                    currEvent[DatabaseContract.EventsColumns.COMMENT] = comment.toString()

                    events.add(currEvent)

                    makeVisited?.setOnClickListener({
                        makeEventVisited(id)
                    })


                    Log.i("LLLLLL", "ID = " + id
                            + ", doctorId = " + doctorId
                            + ", date = " + date
                            + ", time = " + time
                            + ", visited = " + visited
                            + ", comment = " + comment
                            + ", date = " + doctor[0]
                            + ", date = " + doctor[1]
                            + ", date = " + doctor[2]
                            + ", date = " + doctor[3]
                            + ", date = " + doctor[4]
                            + ", date = " + doctor[5]
                            + ", date = " + doctor[6]
                            + ", date = " + doctor[7])

                } while (cursor.moveToNext())

            } else {
                Log.i("mLog", "0 rows")
            }
            eventsListView?.visibility = View.VISIBLE
            eventsEmpty?.visibility = View.GONE

            //TODO: show doctor's information
            //TODO: from should be Array<String>

            val from = arrayOf<String>(DatabaseContract.EventsColumns._ID,
                    DatabaseContract.EventsColumns.TIME,
                    "speciality",
                    "name",
                    "surname",
                    "fathersname",
                    "address",
                    "contacts",
                    "comment_doctor",
                    DatabaseContract.EventsColumns.COMMENT)
            val to = intArrayOf(R.id.home_list_item_id,
                    R.id.home_list_item_time,
                    R.id.home_list_item_speciality,
                    R.id.home_list_item_name,
                    R.id.home_list_item_surname,
                    R.id.home_list_item_fathersname,
                    R.id.home_list_item_address,
                    R.id.home_list_item_contacts,
                    R.id.home_list_item_comment_doctor,
                    R.id.home_list_item_comment_event)

            val simpleAdapter = SimpleAdapter(context, events, R.layout.fragment_home_list_view_item,
                    from, to)
            eventsListView!!.adapter = simpleAdapter

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
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentHome.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): FragmentHome {
            val fragment = FragmentHome()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
