package com.ardritkrasniqi.prenotimi.ui.shtoRezervimDialog

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.FragmentBottomSheetDialogBinding
import com.ardritkrasniqi.prenotimi.model.CreateEvent
import com.ardritkrasniqi.prenotimi.preferences.PreferenceProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*


class ShtoRezervimDialog : BottomSheetDialogFragment() {


    private lateinit var calendar: Calendar
    lateinit var prejEdit: EditText
    lateinit var deriEdit: EditText
    lateinit var shtoRezerviminButton: Button
    var token: String? = ""
    private var index = 0


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

        token = PreferenceProvider(context).getToken() // gets token and sends it to livedata in viewmodel
        viewModel.token.value = token // gets token and sends it to livedata in viewmodel
        prejEdit = binding.prejEdit
        deriEdit = binding.deriEdit
        shtoRezerviminButton = binding.shtoRezerviminButton

        calendar = Calendar.getInstance()


        // merr daten e dhene ne datepicker dhe i ben update EditTextit
        val date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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


        shtoRezerviminButton.setOnClickListener {
            Log.i("EMRI", formatDate(binding.prejEdit.text.toString()))
//            viewModel.addAppointmentRequest.value = CreateEvent(binding.emriMbiemri.toString(),
//                binding.telefoniEdit.toString(), formatDate(binding.prejEdit.text.toString()))

            Log.i("TAG", formatDate(binding.prejEdit.text.toString()))
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

    private fun formatDate(string: String) :String{
        val format = "yyyy-MM-dd hh:mm:ss"
        val simpleDateFormat: Date = SimpleDateFormat("EEEE, MMMM dd, yyyy   HH:mm").parse(string)
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(simpleDateFormat)
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
