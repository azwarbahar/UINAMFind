package com.azwar.uinamfind.ui.saya

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azwar.uinamfind.R
import com.azwar.uinamfind.databinding.ActivityEditProfilMahasiswaBinding
import kotlinx.android.synthetic.main.activity_edit_profil_mahasiswa.*
import java.util.*

class EditProfilMahasiswaActivity : AppCompatActivity() {

    private lateinit var editProfilMahasiswaBinding: ActivityEditProfilMahasiswaBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfilMahasiswaBinding = ActivityEditProfilMahasiswaBinding.inflate(layoutInflater)
        setContentView(editProfilMahasiswaBinding.root)

        editProfilMahasiswaBinding.etTanggalLahirEditProfil.setOnClickListener {
            showDatPickerDialig()
        }

        editProfilMahasiswaBinding.imgBackEditProfil.setOnClickListener {
            finish()
        }

        setupSpinner()

    }

    private fun setupSpinner() {
        val arrayJenisKelamin = arrayOf("Laki-laki", "Perempuan")
        val spinnerJenisKelamin = editProfilMahasiswaBinding.spJenisKelaminEditProfil
        val arrayAdapterJenisKelamin =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayJenisKelamin)
        spinnerJenisKelamin.adapter = arrayAdapterJenisKelamin


        spinnerJenisKelamin?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
//                Toast.makeText(
//                    applicationContext,
//                    getString(R.string.selected_item) + " " + arrayJenisKelamin[position],
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }

    private fun showDatPickerDialig() {
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                var dayfix = "" + dayOfMonth;
                var monthfix = "" + monthOfYear;

                if (monthOfYear < 10) {
                    monthfix = "0" + monthfix;
                }
                if (dayOfMonth < 10) {
                    dayfix = "0" + dayfix;
                }
                editProfilMahasiswaBinding.etTanggalLahirEditProfil.setText("" + dayfix + "-" + monthfix + "-" + year)

            }, year, month, day
        )
        dpd.show()
    }
}