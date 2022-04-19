package com.azwar.uinamfind.ui.bacaan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.azwar.uinamfind.R

class BacaanFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(
            R.layout.fragment_bacaan, container,
            false
        )

        return view
    }

}