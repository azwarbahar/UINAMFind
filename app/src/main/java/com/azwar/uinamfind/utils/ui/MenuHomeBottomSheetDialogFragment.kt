package com.azwar.uinamfind.utils.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.azwar.uinamfind.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_menu_home.*

class MenuHomeBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_menu_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ll_mahasiswa_bottom_sheet_menu_home.setOnClickListener {
            Toast.makeText(context, "Menu Mahasiswa", Toast.LENGTH_SHORT).show()
        }

        ll_lembaga_bottom_sheet_menu_home.setOnClickListener {
            Toast.makeText(context, "Menu Lembaga", Toast.LENGTH_SHORT).show()
        }

    }

}