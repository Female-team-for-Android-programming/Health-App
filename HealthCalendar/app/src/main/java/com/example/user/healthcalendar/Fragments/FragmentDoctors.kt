package com.example.user.healthcalendar.Fragments

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.user.healthcalendar.Database.DatabaseContract
import com.example.user.healthcalendar.Database.DbHelper
import com.example.user.healthcalendar.EditDoctorActivity

import com.example.user.healthcalendar.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentDoctors.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentDoctors.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentDoctors : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    var doctorsListView : ListView? = null
    var cursor : Cursor? = null
    var dbHelper : DbHelper? = null
    var simpleCursorAdapter : SimpleCursorAdapter? = null
    //var doctorsEmpty : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view : View = inflater!!.inflate(R.layout.fragment_doctors, container, false)
        val fab : FloatingActionButton = view.findViewById<FloatingActionButton>(R.id.fab_doctors)
        fab.setOnClickListener(View.OnClickListener {
            goToEditDoctorActivity()
        })

        dbHelper = DbHelper(activity)

        //doctorsEmpty = getView()?.findViewById<TextView>(R.id.doctors_list_empty_textview)
        doctorsListView = view.findViewById<ListView>(R.id.doctors_listview)

        showDBdata()

        return view
    }

    fun showDBdata() {

        var database = dbHelper!!.getWritableDatabase()
        cursor = database.query(DatabaseContract.DoctorsColumns.TABLE_NAME, null, null, null, null, null, null)

        var from = arrayOf<String>(DatabaseContract.DoctorsColumns._ID,
                DatabaseContract.DoctorsColumns.SPECIALITY,
                DatabaseContract.DoctorsColumns.NAME,
                DatabaseContract.DoctorsColumns.SURNAME,
                DatabaseContract.DoctorsColumns.FATHERSNAME,
                DatabaseContract.DoctorsColumns.ADDRESS,
                DatabaseContract.DoctorsColumns.CONTACTS,
                DatabaseContract.DoctorsColumns.COMMENT)
        var to = intArrayOf(R.id.doctors_list_item_id,
                R.id.doctors_list_item_speciality,
                R.id.doctors_list_item_name,
                R.id.doctors_list_item_surname,
                R.id.doctors_list_item_fathersname,
                R.id.doctors_list_item_address,
                R.id.doctors_list_item_contacts,
                R.id.doctors_list_item_comment)

        simpleCursorAdapter = SimpleCursorAdapter(activity, R.layout.fragment_doctors_list_view_item, cursor, from, to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)

        doctorsListView?.setAdapter(simpleCursorAdapter)

        cursor?.close()

        dbHelper?.close()

    }

    fun goToEditDoctorActivity() {
        val intent : Intent = Intent(activity, EditDoctorActivity::class.java)
        startActivity(intent)
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
         * @return A new instance of fragment FragmentDoctors.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): FragmentDoctors {
            val fragment = FragmentDoctors()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor