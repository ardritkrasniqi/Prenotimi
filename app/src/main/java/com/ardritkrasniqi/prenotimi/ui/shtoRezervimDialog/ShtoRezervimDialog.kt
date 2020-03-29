package com.ardritkrasniqi.prenotimi.ui.shtoRezervimDialog

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.FragmentBottomSheetDialogBinding
import com.ardritkrasniqi.prenotimi.model.CreateEvent
import com.ardritkrasniqi.prenotimi.preferences.PreferenceProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*


class ShtoRezervimDialog : BottomSheetDialogFragment() {


    private lateinit var calendar: Calendar
    lateinit var prejEdit: EditText
    lateinit var deriEdit: EditText
    lateinit var shtoRezerviminButton: Button
    private var index = 0
    private var switcherValue = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding =
            DataBindingUtil.inflate<FragmentBottomSheetDialogBinding>(
                inflater, R.layout.fragment_bottom_sheet_dialog, container, false
            )
        val viewModel = ViewModelProvider(this).get(ShtoRezervimViewModel::class.java)

        val sharedP = PreferenceProvider(this.requireContext())


//        viewModel.token.value = sharedP.getToken() // gets token and sends it to livedata in viewmodel

        val tokenAuth = "Bearer ${sharedP.getToken()}"
        viewModel.token.value = tokenAuth

        prejEdit = binding.prejEdit
        deriEdit = binding.deriEdit
        shtoRezerviminButton = binding.shtoRezerviminButton

        calendar = Calendar.getInstance()

        viewModel.status.observe(viewLifecycleOwner, Observer { newStatus ->
            Toasty.error(this.requireContext(), newStatus).show()
        })


        // merr daten e dhene ne datepicker dhe i ben update EditTextit
        val date = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()

        }

        // merr kohen e dhene ne timepicker dhe i ben update EditTextit
        val time = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minuteOfHour ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minuteOfHour)
            updateLabel()
        }


        // Datepicker dialog
        val datePickerDialog = DatePickerDialog(
            this.context as Context, date, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Timepicker dialog
        val timePickerDialog = TimePickerDialog(
            context, R.style.CustomDatePickerDialog, time, calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), true
        )





        prejEdit.setOnClickListener {
            index = 1
            datePickerDialog.show()
            datePickerDialog.setOnDismissListener {
                timePickerDialog.show()
            }

        }
        index = 0

        deriEdit.setOnClickListener {
            index = 2
            datePickerDialog.show()
            datePickerDialog.setOnDismissListener {
                timePickerDialog.show()
            }

        }
        index = 0


        // Shto rezervimin button
        shtoRezerviminButton.setOnClickListener {
            viewModel.addAppointmentRequest.value = CreateEvent(
                binding.emriMbiemri.text.toString(),
                binding.telefoniEdit.text.toString(),
                formatDate(binding.prejEdit.text.toString()),
                formatDate(binding.deriEdit.text.toString()),
                switcherValue,
                binding.komentiEdit.text.toString()
            )


            viewModel.addEvent()

//            Toasty.error(this.requireContext(), viewModel.status.value.toString()).show()
        }


        binding.switcher.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            switcherValue = if (b) {
                1
            } else {
                0
            }
        }

        return binding.root
    }

    private fun updateLabel() {
        val format = "EEEE, MMMM dd, yyyy   HH:mm"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())

        if (index == 1) {
            prejEdit.setText(simpleDateFormat.format(calendar.time))
        } else if (index == 2) {
            deriEdit.setText(simpleDateFormat.format(calendar.time))
        }
    }

    private fun formatDate(string: String): String {
        val format = "yyyy-MM-dd HH:mm:ss"
        val simpleDateFormat: Date =
            SimpleDateFormat("EEEE, MMMM dd, yyyy   HH:mm").parse(string)
        return SimpleDateFormat(format).format(simpleDateFormat)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog =
                dialogInterface as BottomSheetDialog
            bottomSheetDialog.behavior.setPeekHeight(getWindowHeight(), false)
            setupFullHeight(bottomSheetDialog)

        }
        return dialog


    }


    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<ConstraintLayout>(R.id.bottom_sheet_layout) as ConstraintLayout
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams


        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int { // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay
            .getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }


}
