package com.example.user.healthcalendar

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class EditDoctorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_doctor)

        val spinner : Spinner = findViewById<Spinner>(R.id.speciality)
        val adapter : ArrayAdapter<CharSequence> = ArrayAdapter
                .createFromResource(this, R.array.doctors_specialties, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(adapter)

        val submit : Button = findViewById<Button>(R.id.submit)
        submit.setOnClickListener(View.OnClickListener {
            submitDoctorChanges()
        })
    }

    private fun submitDoctorChanges() {

        //TODO("And here we need to update Database somehow")
        super.onBackPressed()
        //TODO("Now just return back to the Doctors fragment")

    }
}
