package com.example.user.healthcalendar.Fragments

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.user.healthcalendar.Database.DbHelper
import com.example.user.healthcalendar.EditEventActivity

import com.example.user.healthcalendar.R
import kotlinx.android.synthetic.main.fragment_calendar.*

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
    var dateDisplay : TextView? = null

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
        val fab : FloatingActionButton = view!!.findViewById(R.id.fab_calendar)
        fab.setOnClickListener(View.OnClickListener {
            goToEditEventActivity()
        })

        calendarView = view!!.findViewById(R.id.calendarView)
        dateDisplay = view!!.findViewById(R.id.dateDisplay)
        //dateDisplay?.setText("Date: ")

        calendarView?.setOnDateChangeListener(CalendarView.OnDateChangeListener() {
            calendarView, year, month, day ->
            dateDisplay?.setText("Date: " + day + "/" + (month + 1) + "/" + year)
        })
    }

    fun goToEditEventActivity() {
        val intent : Intent = Intent(activity, EditEventActivity::class.java)
        startActivity(intent)
    }

    fun goToEditEventActivity(id: Long) {
        val intent : Intent = Intent(activity, EditEventActivity::class.java)
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
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

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
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
