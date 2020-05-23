package com.ardritkrasniqi.prenotimi.ui.listAppointments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.ardritkrasniqi.prenotimi.R

class ListAppointments : Fragment() {

    companion object {
        fun newInstance() = ListAppointments()
    }

    private lateinit var viewModel: ListAppointmentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_appointments_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListAppointmentsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
