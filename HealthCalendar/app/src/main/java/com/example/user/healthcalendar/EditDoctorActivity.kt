package com.example.user.healthcalendar

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.user.healthcalendar.Database.DatabaseContract
import com.example.user.healthcalendar.Database.DbHelper

class EditDoctorActivity : AppCompatActivity() {


    var submit: Button? = null

    var etSpeciality: Spinner? = null
    var etName: EditText? = null
    var etSurname: EditText? = null
    var etFathersName: EditText? = null
    var etAddress: EditText? = null
    var etContacts: EditText? = null
    var etComment: EditText? = null

    var dbHelper: DbHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_doctor)

        val spinner : Spinner = findViewById<Spinner>(R.id.speciality)
        val adapter : ArrayAdapter<CharSequence> = ArrayAdapter
                .createFromResource(this, R.array.doctors_specialties, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(adapter)

        submit = findViewById<Button>(R.id.submit)
        submit!!.setOnClickListener(View.OnClickListener {
            submitDoctorChanges()
        })

        etSpeciality = findViewById<Spinner>(R.id.speciality)
        etName = findViewById<EditText>(R.id.name)
        etSurname = findViewById<EditText>(R.id.surname)
        etFathersName = findViewById<EditText>(R.id.fathersName)
        etAddress = findViewById<EditText>(R.id.address)
        etContacts = findViewById<EditText>(R.id.contacts)
        etComment = findViewById<EditText>(R.id.comment)

        dbHelper = DbHelper(this)
    }

    private fun submitDoctorChanges() {

        //TODO("And here we need to update Database somehow")

        var speciality = etSpeciality!!.selectedItem.toString()
        var name = etName!!.getText().toString()
        var surname = etSurname!!.getText().toString()
        var fathersName = etFathersName!!.getText().toString()
        var address = etAddress!!.getText().toString()
        var contacts = etContacts!!.getText().toString()
        var comment = etComment!!.getText().toString()

        var database = dbHelper!!.getWritableDatabase()

        var contentValues = ContentValues()


        contentValues.put(DatabaseContract.DoctorsColumns.SPECIALITY, speciality)
        contentValues.put(DatabaseContract.DoctorsColumns.NAME, name)
        contentValues.put(DatabaseContract.DoctorsColumns.SURNAME, surname)
        contentValues.put(DatabaseContract.DoctorsColumns.FATHERSNAME, fathersName)
        contentValues.put(DatabaseContract.DoctorsColumns.ADDRESS, address)
        contentValues.put(DatabaseContract.DoctorsColumns.CONTACTS, contacts)
        contentValues.put(DatabaseContract.DoctorsColumns.COMMENT, comment)

        database.insert(DatabaseContract.DoctorsColumns.TABLE_NAME, null, contentValues)

        //write content of the database to the log

        var cursor : Cursor = database.query(DatabaseContract.DoctorsColumns.TABLE_NAME, null, null, null, null, null, null)

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
                Log.i("mLog", "ID = " + cursor.getInt(idIndex)
                        + ", speciality = " + cursor.getString(specialityIndex)
                        + ", name = " + cursor.getString(nameIndex)
                        + ", surname = " + cursor.getString(surnameIndex)
                        + ", fathersname = " + cursor.getString(fathersnameIndex)
                        + ", addressIndex = " + cursor.getString(addressIndex)
                        + ", contactsIndex = " + cursor.getString(contactsIndex)
                        + ", commentIndex = " + cursor.getString(commentIndex))
            } while (cursor.moveToNext())
        }
        else {
            Log.i("mLog", "0 rows")
        }
        cursor.close()

        dbHelper?.close()

        super.onBackPressed()
        //TODO("Now just return back to the Doctors fragment")

    }


}
