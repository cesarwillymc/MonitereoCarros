package com.consorciosm.sanmiguel.ui.main.admin.ui.ordenes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.consorciosm.sanmiguel.R


/**
 * A simple [Fragment] subclass.
 */
class OrdernesProgramadas : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ordernes_programadas, container, false)
    }

}
