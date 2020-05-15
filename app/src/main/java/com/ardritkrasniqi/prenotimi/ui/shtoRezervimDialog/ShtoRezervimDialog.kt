package com.ardritkrasniqi.prenotimi.ui.shtoRezervimDialog

import android.app.Activity
import android.app.DatePickerDialog

import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface


import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.selection.OperationMonitor
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.FragmentBottomSheetDialogBinding
import com.ardritkrasniqi.prenotimi.model.CreateEvent
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.preferences.PreferenceProvider
import com.ardritkrasniqi.prenotimi.ui.mainPage.MainFragmentDirections
import com.ardritkrasniqi.prenotimi.utils.formatDateForEdits
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*


/*
Proudly developed by Ardrit Krasniqi 2020, first Project ever done
 */

class ShtoRezervimDialog : BottomSheetDialogFragment() {


    var callback: CallSnackbar? = null

    interface CallSnackbar {
        fun dialogIsClosed()
    }

    fun setDialogClosedListener(callback: CallSnackbar) {
        this.callback = callback
    }


    private lateinit var calendar: Calendar
    lateinit var prejEdit: EditText
    lateinit var deriEdit: EditText
    lateinit var shtoRezerviminButton: Button
    private var index = 0
    var checkedItem = 0
    private var switcherValue = 0
    private var selectedItemDialog: Int? = null
    private var event: Event? = null


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


        val tokenAuth = "Bearer ${sharedP.getToken()}"
        viewModel.token.value = tokenAuth

        // instantiating stuff
        prejEdit = binding.prejEdit
        deriEdit = binding.deriEdit
        shtoRezerviminButton = binding.shtoRezerviminButton

        calendar = Calendar.getInstance()

        // merr argumentet nga dayFragment si argument i llojit Event
        val args = arguments?.let { ShtoRezervimDialogArgs.fromBundle(it) }
        event = args?.event

        val dateFromDayView = args?.time

        viewModel.appointmentId.value = args?.event?.id
        Log.i("ora", dateFromDayView.toString())

        if (dateFromDayView != null) {
            binding.prejEdit.setText(formatDateForEdits(dateFromDayView))
            binding.deriEdit.setText(formatDateForEdits(dateFromDayView))
        }



        if (event != null) {
            binding.shtoRezerviminButton.text = getString(R.string.ndrysho_text)
            binding.shtoRezerviminButton.setCompoundDrawables(
                resources.getDrawable(R.drawable.ic_ndrysho, null), null, null, null
            )
            binding.fshiRezerviminButton.visibility = View.VISIBLE
            binding.shtoRezervimText.setText("Ndrysho Rezervimin")
            binding.emriMbiemri.setText(event?.client_name)
            binding.telefoniEdit.setText(event?.phone)
            binding.prejEdit.setText(formatDateForEdits(event!!.start_date))
            binding.deriEdit.setText(formatDateForEdits(event!!.end_date))
            binding.switcher.isChecked = trueOrFalse(event!!)
            checkedItem = nullOrNot(event!!)
            binding.komentiEdit.setText(event?.comment)
        }




        viewModel.status.observe(viewLifecycleOwner, Observer { newStatus ->
            if (newStatus != "200") {
                Toasty.error(this.requireContext(), newStatus).show()
            } else {
                dialog?.let { it1 -> onDismiss(it1) }
                // nese callbacku nuk eshte null, ateher eshte thirr nga MainFragment, else thirret nga dayFragment
                //i nuk ben call dialogisclosed dhe navigon ne mainFragment
//                if (callback != null) callback?.dialogIsClosed() else
                activity?.let {
                    val isAppointmentAdded = true
                    val arguments = Bundle()
                    arguments.putBoolean("isAppointmentAdded", isAppointmentAdded)
                    Navigation.findNavController(it, R.id.myNavHostFragment)
                        .navigate(R.id.mainFragment, arguments)
                }
            }
        })


        // merr daten e dhene ne datepicker dhe i ben update EditTextit

            val date = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }


        // merr kohen e dhene ne timepicker dhe i ben update EditTextit
        val time = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minuteOfHour ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minuteOfHour)
                updateLabel()

        }


        // Datepicker dialog
        val datePickerDialog = DatePickerDialog(
            context!!, R.style.CustomDatePickerDialog, date, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )


        // Timepicker dialog
        val timePickerDialog = TimePickerDialog(
            context, R.style.CustomDatePickerDialog, time, (dateFromDayView?.substring(11,13)
                ?.toInt() ?: Calendar.HOUR_OF_DAY),
            (dateFromDayView?.substring(14,15)
                ?.toInt() ?: Calendar.HOUR_OF_DAY), true
        )

        prejEdit.setOnClickListener {
            index = 1
            when(dateFromDayView == null){
                true ->{
                    datePickerDialog.show()
                    datePickerDialog.setOnDismissListener() {
                        timePickerDialog.show()
                    }
                }
                else ->{
                    timePickerDialog.show()
                }

            }
        }
        index = 0

        deriEdit.setOnClickListener {
            index = 2
            when(dateFromDayView == null){
                true ->{
                    datePickerDialog.show()
                    datePickerDialog.setOnDismissListener() {
                        timePickerDialog.show()
                    }
                }
                else ->{
                    timePickerDialog.show()
                }

            }
        }
        index = 0


        // Shto rezervimin button
        shtoRezerviminButton.setOnClickListener {
            if(binding.emriMbiemri.text.isNullOrEmpty() || binding.telefoniEdit.text.isNullOrEmpty() || binding.komentiEdit.text.isNullOrEmpty()
                || binding.prejEdit.text.isNullOrEmpty() || binding.deriEdit.text.isNullOrEmpty()){
                Toasty.warning(this.requireContext(), "Plotesoji te gjitha fushat").show()
            }else {
            viewModel.addAppointmentRequest.value = CreateEvent(
                binding.emriMbiemri.text.toString(),
                binding.telefoniEdit.text.toString(),
                formatDate(binding.prejEdit.text.toString()),
                formatDate(binding.deriEdit.text.toString()),
                switcherValue,
                selectedItemDialog,
                binding.komentiEdit.text.toString()
            )


                if (event == null) viewModel.addEvent() else viewModel.editoRezervimin()
            }
        }


        binding.fshiRezerviminButton.setOnClickListener {


            val dialog = MaterialAlertDialogBuilder(this.requireContext())
            dialog.setMessage("Po e fshini rezervimin, a jeni i sigurte?")
            dialog.setPositiveButton(getString(R.string.fshije_text_button)){ dialog, which ->
                viewModel.deleteAppointment(event?.id!!)
                dialog.dismiss()
            }
            dialog.setNegativeButton("Mos e fshij"){dialog, which ->
                dialog.dismiss()
            }
            dialog.setCancelable(false).show()
            }



        binding.switcher.setOnCheckedChangeListener { a: CompoundButton, b: Boolean ->
            switcherValue = (if (b) {
                1
            } else {
                0
            })

            if (b){
                val items = arrayOf("","Ditore", "Javore", "Mujore")
                var internalSelectedItem: Int? = null
                val dialog = MaterialAlertDialogBuilder(this.requireContext())
                dialog.apply {
                    setTitle("Frekuenca e rezervimit")
                    setNegativeButton("Cancel"){dialog, which ->
                        binding.switcher.isChecked = false
                        selectedItemDialog = null
                        dialog.dismiss()
                    }
                    setPositiveButton("OK"){dialog, which ->
                        selectedItemDialog = internalSelectedItem
                        if(selectedItemDialog == null){
                            binding.switcher.isChecked = false
                        }
                        dialog.dismiss()
                    }
                    setSingleChoiceItems(items, checkedItem){ dialog, which ->
                        internalSelectedItem = which
                    }

                    setCancelable(false)

                }.show()


            } else{
                selectedItemDialog = null
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



    fun trueOrFalse(event: Event): Boolean{
        return event.recurring == 1
    }

    fun nullOrNot(event: Event): Int{
        if(event.recurring_frequency != null){
            return event.recurring_frequency
        }
        return 0
    }





}
