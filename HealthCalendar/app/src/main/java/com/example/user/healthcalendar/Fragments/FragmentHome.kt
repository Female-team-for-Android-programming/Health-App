package com.example.user.healthcalendar.Fragments

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

    private fun makeEventVisited(id: Long){

        showDBdata()
    }

    private fun getTodayEvents(){

    }


    private fun showDBdata() {

        val database = dbHelper!!.readableDatabase

        //TODO: show only today's events

        cursor = database.query(DatabaseContract.EventsColumns.TABLE_NAME, null, null,
                null, null, null, null)

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

                    val id = cursor!!.getInt(idIndex)
                    val doctorId = cursor!!.getString(doctorIdIndex)
                    //val doctor = getDoctor(doctorId.toInt())
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

                } while (cursor!!.moveToNext())

            } else {
                Log.i("mLog", "0 rows")
            }
            eventsListView?.visibility = View.VISIBLE
            eventsEmpty?.visibility = View.GONE

            //TODO: show doctor's information

            val from = arrayOf(DatabaseContract.EventsColumns._ID,
                    DatabaseContract.EventsColumns.TIME,
                    DatabaseContract.EventsColumns.DOCTOR_ID,
                    DatabaseContract.EventsColumns.DOCTOR_ID,
                    DatabaseContract.EventsColumns.DOCTOR_ID,
                    DatabaseContract.EventsColumns.DOCTOR_ID,
                    DatabaseContract.EventsColumns.DOCTOR_ID,
                    DatabaseContract.EventsColumns.DOCTOR_ID,
                    DatabaseContract.EventsColumns.DOCTOR_ID,
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

            simpleCursorAdapter = SimpleCursorAdapter(context, R.layout.fragment_home_list_view_item,
                    cursor, from, to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
            eventsListView!!.adapter = simpleCursorAdapter
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
