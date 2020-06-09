package com.ardritkrasniqi.prenotimi.ui.shtoRezervimDialog


import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.FragmentBottomSheetDialogBinding
import com.ardritkrasniqi.prenotimi.model.CreateEvent
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.preferences.PreferenceProvider
import com.ardritkrasniqi.prenotimi.utils.dateToSimpleDate
import com.ardritkrasniqi.prenotimi.utils.dateToString
import com.ardritkrasniqi.prenotimi.utils.formatDateForEdits
import com.ardritkrasniqi.prenotimi.utils.stringToDate
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*


/*
Proudly developed by Ardrit Krasniqi 2020, first Project ever done
 */

class ShtoRezervimDialog : BottomSheetDialogFragment() {


    private lateinit var calendar: Calendar
    lateinit var prejEdit: TextInputEditText
    lateinit var deriEdit: TextInputEditText
    lateinit var shtoRezerviminButton: MaterialButton
    private var index = 0
    var checkedItem = 0
    private var switcherValue = 0
    private var selectedItemDialog: Int? = null
    private var event: Event? = null
    private val items = arrayOf("Nuk Përsëritet", "Çdo Dite", "Çdo Javë", "Çdo Muaj")
    private lateinit var timePickerDialog: TimePickerDialog


    @SuppressLint("SetTextI18n")
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
        shtoRezerviminButton = binding.shtoRezerviminButton as MaterialButton

        calendar = Calendar.getInstance()

        // merr argumentet nga dayFragment si argument i llojit Event
        val args = arguments?.let { ShtoRezervimDialogArgs.fromBundle(it) }
        event = args?.event

        val dateFromDayView = args?.time

        viewModel.appointmentId.value = args?.event?.id

        // checks if recurring freq is not null, if not than frekuenca text gets the value of the endpoint if its null, rezervimi nuk perseritet
        if (event?.recurring_frequency != null) {
            binding.frekuencaText.text =
                "Rezervimi perseritet ${items[(event?.recurring_frequency)!!].toLowerCase(Locale.getDefault())}"
        } else {
            binding.frekuencaText.text = "Rezervimi ${items[0].toLowerCase(Locale.getDefault())}"
        }


        // nese dateFromDayView nuk eshte null, i ploteson prej dhe deri me daten e ardhur nga DayViewArdrit
        if (dateFromDayView != null) {
            binding.prejEdit.setText(formatDateForEdits(dateFromDayView))
            val editedDate = stringToDate(dateFromDayView)
            val calendar = Calendar.getInstance()
            calendar.time = editedDate
            calendar.add(Calendar.MINUTE, 30)
            val datePlusMinutes: Date = calendar.time
            binding.deriEdit.setText(formatDateForEdits(dateToString(datePlusMinutes)))
        }




        if (event != null) {
            binding.shtoRezerviminButton.text = getString(R.string.ndrysho_text)
            binding.fshiRezerviminButton.visibility = View.VISIBLE
            binding.shtoRezervimText.text = "Ndrysho Rezervimin"
            binding.emriMbiemri.setText(event?.client_name)
            binding.telefoniEdit.setText(event?.phone)
            binding.prejEdit.setText(formatDateForEdits(event!!.start_date))
            binding.deriEdit.setText(formatDateForEdits(event!!.end_date))
            checkedItem = nullOrNot(event!!)
            binding.komentiEdit.setText(event?.comment)
        }




        viewModel.shtoRezerviminStatus.observe(viewLifecycleOwner, Observer { newStatus ->
            if (newStatus != "200") {
                Toasty.error(this.requireContext(), newStatus).show()
            } else {
                sharedP.saveIsAppointmentAdded(true)
                activity?.let {
                    Navigation.findNavController(it, R.id.myNavHostFragment)
                        .navigate(R.id.mainFragment)
                }
            }
        })


        viewModel.editoRezerviminStatus.observe(viewLifecycleOwner, Observer {
            if (it != "200") {
                Toasty.error(this.requireContext(), it).show()
            } else {
                Toasty.success(
                    requireContext(),
                    "Rezervimi per ${event?.client_name?.toUpperCase(Locale.getDefault())} u editua me sukses",
                    Toasty.LENGTH_LONG
                )
                    .show()
                findNavController().navigate(R.id.mainFragment)
            }
        })

        viewModel.fshijRezerviminStatus.observe(viewLifecycleOwner, Observer {
            if (it != "200") {
                Toasty.error(this.requireContext(), it).show()
            } else {
                Toasty.success(
                    requireContext(),
                    "Rezervimi per ${event?.client_name?.toUpperCase(Locale.getDefault())} u largua!",
                    Toasty.LENGTH_LONG
                ).show()
                findNavController().navigate(R.id.mainFragment)
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


//        yyyy-MM-dd HH:mm:ss

        // Datepicker dialog
        val datePickerDialog = DatePickerDialog(
            context!!,
            R.style.CustomDatePickerDialog,
            date,
            dateFromDayView?.substring(0, 4)?.toInt() ?: calendar.get(Calendar.YEAR),
            (dateFromDayView?.substring(5, 7)?.toInt()?.minus(1)) ?: calendar.get(Calendar.MONTH),
            dateFromDayView?.substring(8, 10)?.toInt() ?: calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK"
        ) { _, _ ->  timePickerDialog.show()}


        // Timepicker dialog
        timePickerDialog = TimePickerDialog(
            context, R.style.CustomDatePickerDialog, time,
            dateFromDayView?.substring(11, 13)
                ?.toInt() ?: Calendar.HOUR_OF_DAY,
            dateFromDayView?.substring(14, 15)
                ?.toInt() ?: Calendar.MINUTE, true
        )

        prejEdit.setOnClickListener {
            index = 1
            when (true) {
                true -> {
                    datePickerDialog.show()
                }
            }
        }
        index = 0

        deriEdit.setOnClickListener {
            index = 2
            when (true) {
                true -> {
                    datePickerDialog.show()
                }
            }
        }
        index = 0


        // Shto rezervimin button
        shtoRezerviminButton.setOnClickListener {
            if (binding.emriMbiemri.text.isNullOrEmpty() || binding.telefoniEdit.text.isNullOrEmpty() || binding.komentiEdit.text.isNullOrEmpty()
                || binding.prejEdit.text.isNullOrEmpty() || binding.deriEdit.text.isNullOrEmpty()
            ) {
                Toasty.warning(this.requireContext(), "Plotesoji te gjitha fushat").show()
            } else {
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

        binding.closeDialogImg?.setOnClickListener {
            dialog?.dismiss()
        }


        binding.fshiRezerviminButton.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this.requireContext())
            dialog.setMessage("Po e fshini rezervimin, a jeni i sigurte?")
            dialog.setPositiveButton(getString(R.string.fshije_text_button)) { dialogu, _ ->
                viewModel.deleteAppointment(event?.id!!)
                dialogu.dismiss()
            }
            dialog.setNegativeButton("Mos e fshij") { dialogu, _ ->
                dialogu.dismiss()
            }
            dialog.setCancelable(false).show()
        }


        // dialog for the recurring frequency
        var internalSelectedItem: Int? = null
        val dialog = MaterialAlertDialogBuilder(this.requireContext())
        dialog.apply {
            setTitle("Frekuenca e rezervimit")
            setNegativeButton("Cancel") { dialog, _ ->
                if (event?.recurring_frequency != null) {
                    when (event!!.recurring_frequency!!) {
                        0 -> binding.frekuencaText.text =
                            "Rezervimi ${items[event!!.recurring_frequency!!].toLowerCase(Locale.getDefault())}"
                        else -> binding.frekuencaText.text =
                            "Rezervimi përsëritet ${items[event!!.recurring_frequency!!].toLowerCase(Locale.getDefault())}"
                    }
                } else binding.frekuencaText.text = "Rezervimi ${items[0].toLowerCase(Locale.getDefault())}"
                selectedItemDialog = null
                dialog.dismiss()
            }
            setPositiveButton("OK") { dialog, which ->
                selectedItemDialog = internalSelectedItem
                if (selectedItemDialog == null) {
                    binding.frekuencaText.text = selectedItemDialog?.let { items.get(it) }
                }
                dialog.dismiss()
            }
            setSingleChoiceItems(items, checkedItem) { dialog, which ->
                internalSelectedItem = which
                when (which) {
                    0 -> binding.frekuencaText.text = "Rezervimi ${items[which].toLowerCase(Locale.getDefault())}"
                    else -> binding.frekuencaText.text =
                        "Rezervimi përsëritet ${items[which].toLowerCase(Locale.getDefault())}"
                }
            }
            setCancelable(false)
        }


        binding.frekuencaText.setOnClickListener {
            dialog.show()
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
        return SimpleDateFormat(format, Locale.getDefault()).format(simpleDateFormat)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.behavior.setPeekHeight(getWindowHeight(), false)

        }
        return dialog
    }


//    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
////        val bottomSheet = bottomSheetDialog.findViewById<LinearLayout>(R.id.bottom_sheet_layout) as LinearLayout
////        val behavior = BottomSheetBehavior.from(bottomSheet)
////        val layoutParams = bottomSheet.layoutParams
////
////
////        val windowHeight = getWindowHeight()
////        if (layoutParams != null) {
////            layoutParams.height = windowHeight
////        }
////        bottomSheet.layoutParams = layoutParams
////        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//    }

    private fun getWindowHeight(): Int { // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay
            .getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }


    private fun trueOrFalse(event: Event): Boolean {
        return event.recurring == 1
    }

    private fun nullOrNot(event: Event): Int {
        if (event.recurring_frequency != null) {
            return event.recurring_frequency
        }
        return 0
    }
}