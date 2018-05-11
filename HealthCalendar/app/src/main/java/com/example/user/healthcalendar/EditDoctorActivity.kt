package com.example.user.healthcalendar

import android.content.ContentValues
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.user.healthcalendar.Database.DatabaseContract
import com.example.user.healthcalendar.Database.DbHelper

class EditDoctorActivity : AppCompatActivity() {

    private var submit: Button? = null

    private var etSpeciality: Spinner? = null
    private var etName: EditText? = null
    private var etSurname: EditText? = null
    private var etFathersName: EditText? = null
    private var etAddress: EditText? = null
    private var etContacts: EditText? = null
    private var etComment: EditText? = null

    private var dbHelper: DbHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_doctor)

        val spinner : Spinner = findViewById(R.id.speciality)
        val adapter = ArrayAdapter
                .createFromResource(this, R.array.doctors_specialties, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        etSpeciality = findViewById(R.id.speciality)
        etName = findViewById(R.id.name)
        etSurname = findViewById(R.id.surname)
        etFathersName = findViewById(R.id.fathersName)
        etAddress = findViewById(R.id.address)
        etContacts = findViewById(R.id.contacts)
        etComment = findViewById(R.id.comment)

        val intent = intent

        val id = intent.getLongExtra("doctorId",0)
        Log.i("this id = ", id.toString())

        if (id == 0.toLong()) {
            createDoctor()
        }
        else
        {
            Log.i("this id = ", id.toString())
            editDoctor(id)
        }

    }

    private fun setSpinnerSelection(spinner: Spinner, array: Array<String>, text: String) {
        for (i in array.indices) {
            if (array[i] == text) {
                spinner.setSelection(i)
            }
        }
    }

    private fun editDoctor(id:  Long){

        dbHelper = DbHelper(this)
        val database = dbHelper!!.writableDatabase
        val query = "SELECT * FROM " + DatabaseContract.DoctorsColumns.TABLE_NAME +
                " WHERE " + DatabaseContract.DoctorsColumns._ID + "='" + id + "'"
        val cursor : Cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val idIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns._ID)
            val specialityIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.SPECIALITY)
            val nameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.NAME)
            val surnameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.SURNAME)
            val fathersnameIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.FATHERSNAME)
            val addressIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.ADDRESS)
            val contactsIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.CONTACTS)
            val commentIndex : Int = cursor.getColumnIndex(DatabaseContract.DoctorsColumns.COMMENT)

            do {
                setSpinnerSelection(etSpeciality!!, resources.getStringArray(R.array.doctors_specialties), cursor.getString(specialityIndex))
                etName!!.setText(cursor.getString(nameIndex))
                etSurname!!.setText(cursor.getString(surnameIndex))
                etFathersName!!.setText(cursor.getString(fathersnameIndex))
                etAddress!!.setText(cursor.getString(addressIndex))
                etContacts!!.setText(cursor.getString(contactsIndex))
                etComment!!.setText(cursor.getString(commentIndex))

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

        submit = findViewById(R.id.submit_doctor)
        submit!!.setOnClickListener({
            submitDoctorChanges(id)
        })

    }

    private fun createDoctor(){

        submit = findViewById(R.id.submit_doctor)
        submit!!.setOnClickListener({
            submitNewDoctor()
        })

        dbHelper = DbHelper(this)

    }

    private fun submitDoctorChanges(id: Long){

        val speciality = etSpeciality!!.selectedItem.toString()
        val name = etName!!.text.toString()
        val surname = etSurname!!.text.toString()
        val fathersName = etFathersName!!.text.toString()
        val address = etAddress!!.text.toString()
        val contacts = etContacts!!.text.toString()
        val comment = etComment!!.text.toString()

        val database = dbHelper!!.writableDatabase

        val contentValues = ContentValues()

        contentValues.put(DatabaseContract.DoctorsColumns.SPECIALITY, speciality)
        contentValues.put(DatabaseContract.DoctorsColumns.NAME, name)
        contentValues.put(DatabaseContract.DoctorsColumns.SURNAME, surname)
        contentValues.put(DatabaseContract.DoctorsColumns.FATHERSNAME, fathersName)
        contentValues.put(DatabaseContract.DoctorsColumns.ADDRESS, address)
        contentValues.put(DatabaseContract.DoctorsColumns.CONTACTS, contacts)
        contentValues.put(DatabaseContract.DoctorsColumns.COMMENT, comment)

        database.update(DatabaseContract.DoctorsColumns.TABLE_NAME, contentValues,
                DatabaseContract.DoctorsColumns._ID + "=" + id, null)

        dbHelper?.close()

        super.onBackPressed()

    }

    private fun submitNewDoctor() {

        val speciality = etSpeciality!!.selectedItem.toString()
        val name = etName!!.text.toString()
        val surname = etSurname!!.text.toString()
        val fathersName = etFathersName!!.text.toString()
        val address = etAddress!!.text.toString()
        val contacts = etContacts!!.text.toString()
        val comment = etComment!!.text.toString()

        val database = dbHelper!!.writableDatabase

        val contentValues = ContentValues()

        contentValues.put(DatabaseContract.DoctorsColumns.SPECIALITY, speciality)
        contentValues.put(DatabaseContract.DoctorsColumns.NAME, name)
        contentValues.put(DatabaseContract.DoctorsColumns.SURNAME, surname)
        contentValues.put(DatabaseContract.DoctorsColumns.FATHERSNAME, fathersName)
        contentValues.put(DatabaseContract.DoctorsColumns.ADDRESS, address)
        contentValues.put(DatabaseContract.DoctorsColumns.CONTACTS, contacts)
        contentValues.put(DatabaseContract.DoctorsColumns.COMMENT, comment)

        database.insert(DatabaseContract.DoctorsColumns.TABLE_NAME, null, contentValues)

        dbHelper?.close()

        super.onBackPressed()

    }

}
