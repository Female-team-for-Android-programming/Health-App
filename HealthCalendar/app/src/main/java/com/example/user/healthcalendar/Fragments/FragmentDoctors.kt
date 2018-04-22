package com.example.user.healthcalendar.Fragments

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import com.example.user.healthcalendar.Database.DatabaseContract
import com.example.user.healthcalendar.Database.DbHelper
import com.example.user.healthcalendar.EditDoctorActivity
import com.example.user.healthcalendar.R
import android.widget.TextView




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
    //
    // TODO : need to show something if there is no doctors in list yet
    //

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
        val fab : FloatingActionButton = getView()!!.findViewById(R.id.fab_doctors)
        fab.setOnClickListener({
            goToEditDoctorActivity()
        })
        doctorsListView = getView()?.findViewById(R.id.doctors_listview)

        showDBdata()

        registerForContextMenu(doctorsListView)

    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = activity.getMenuInflater()
        inflater.inflate(R.menu.doctor_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val info = item?.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item.itemId) {
            R.id.cm_edit -> {
                //Toast.makeText(activity.applicationContext, "Вы хотите изменить: itemId = " + info.id, Toast.LENGTH_SHORT).show()
                goToEditDoctorActivity(info.id)
                return true
            }
            R.id.cm_delete -> {
                deleteDoctor(info.id)
                //Toast.makeText(activity.applicationContext, "Вы хотите удалить: itemId = " + info.id, Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    fun deleteDoctor(id: Long){

        var dbHelper: DbHelper? =  DbHelper(context)
        var database = dbHelper!!.getWritableDatabase()

        database.delete(DatabaseContract.DoctorsColumns.TABLE_NAME,
                DatabaseContract.DoctorsColumns._ID  +  "=" + id, null)
    }

    fun goToEditDoctorActivity() {
        val intent : Intent = Intent(activity, EditDoctorActivity::class.java)
        startActivity(intent)
    }

    fun goToEditDoctorActivity(id: Long) {
        val intent : Intent = Intent(activity, EditDoctorActivity::class.java)
        intent.putExtra("doctorId",id)
        startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater!!.inflate(R.layout.fragment_doctors, container, false)

    }

    fun showDBdata() {
        val database = dbHelper!!.readableDatabase
        cursor = database.query(DatabaseContract.DoctorsColumns.TABLE_NAME, null, null,
                null, null, null, null)

        val from = arrayOf(DatabaseContract.DoctorsColumns._ID,
                DatabaseContract.DoctorsColumns.SPECIALITY,
                DatabaseContract.DoctorsColumns.NAME,
                DatabaseContract.DoctorsColumns.SURNAME,
                DatabaseContract.DoctorsColumns.FATHERSNAME,
                DatabaseContract.DoctorsColumns.ADDRESS,
                DatabaseContract.DoctorsColumns.CONTACTS,
                DatabaseContract.DoctorsColumns.COMMENT)
        val to = intArrayOf(R.id.doctors_list_item_id,
                R.id.doctors_list_item_speciality,
                R.id.doctors_list_item_name,
                R.id.doctors_list_item_surname,
                R.id.doctors_list_item_fathersname,
                R.id.doctors_list_item_address,
                R.id.doctors_list_item_contacts,
                R.id.doctors_list_item_comment)

        simpleCursorAdapter = SimpleCursorAdapter(context, R.layout.fragment_doctors_list_view_item,
                cursor, from, to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        doctorsListView!!.adapter = simpleCursorAdapter
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