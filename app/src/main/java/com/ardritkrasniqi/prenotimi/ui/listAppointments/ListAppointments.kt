package com.ardritkrasniqi.prenotimi.ui.listAppointments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.ListAppointmentsFragmentBinding
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.utils.DrawerLocker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class ListAppointments : Fragment() {

    companion object {
        fun newInstance() = ListAppointments()
    }

    private lateinit var viewModel: ListAppointmentsViewModel
    private lateinit var appointments: List<Event>
    private lateinit var bundle: Bundle
    private lateinit var nextMonthButton: ImageView
    private lateinit var previousMonthButton: ImageView
    private lateinit var currentMothText: TextView
    private lateinit var binding: ListAppointmentsFragmentBinding
    private lateinit var adapter: AppointmentAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bundle = arguments!!
        toolbarTweak()

        binding =
            DataBindingUtil.inflate(inflater, R.layout.list_appointments_fragment, container, false)


        // kontrollon displayin per densitet, nese perdoruesi eshte ne telefon paraqet nje rend ne gridview
        val display = activity?.windowManager?.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display?.getMetrics(displayMetrics)
        val density = resources.displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / density
        val columns = (dpWidth / 600).roundToInt()
        binding.appointListRv.layoutManager = GridLayoutManager(context, columns)

        (activity as DrawerLocker?)?.setDrawerLocked(true)


        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListAppointmentsViewModel::class.java)
        viewModel.appointments = bundle.get("appointments") as List<Event>
        updateUI()
        setHasOptionsMenu(true)

    }


    private inner class AppointmentHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private lateinit var appointment: Event
        val emriMbiemri = view.findViewById(R.id.emri_mbiemri) as TextView
        val cardContainer = view.findViewById(R.id.card_container) as CardView
        val prejData = view.findViewById(R.id.prej_data) as TextView
        val deriData = view.findViewById(R.id.deri_data) as TextView
        val kontaktiData = view.findViewById(R.id.kontakti_data) as TextView
        val detajetData = view.findViewById(R.id.detajet_data) as TextView

        fun bind(appointment: Event) {
            this.appointment = appointment
            emriMbiemri.text = appointment.client_name
            prejData.text = appointment.start_date
            deriData.text = appointment.end_date
            kontaktiData.text = appointment.phone
            detajetData.text = appointment.comment
        }
    }

    private inner class AppointmentAdapter(var appointments: MutableList<Event>) :
        RecyclerView.Adapter<AppointmentHolder>(), Filterable {
        private var appointmentsFilterList: MutableList<Event>

        init {
            appointmentsFilterList = appointments
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentHolder {
            val view = layoutInflater.inflate(R.layout.listed_appointment_item, parent, false)
            if(viewType == 0){
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.event_gone_color))
            }
            return AppointmentHolder(view)
        }

        override fun getItemCount(): Int {
            return appointmentsFilterList.size
        }

        override fun onBindViewHolder(holder: AppointmentHolder, position: Int) {
            val appointment = appointmentsFilterList[position]
            holder.bind(appointment)
        }


        override fun getFilter(): Filter {
            return object : Filter(){
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val charSearch = constraint.toString()
                    if(charSearch.isEmpty()){
                        appointmentsFilterList = appointments
                    } else {
                        val resultAppointments = mutableListOf<Event>()
                        for(event in appointments){
                            if(event.client_name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))){
                                resultAppointments.add(event)
                            }
                        }
                        appointmentsFilterList = resultAppointments
                    }
                        val filterResults = FilterResults()
                        filterResults.values = appointmentsFilterList
                        return filterResults
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    appointmentsFilterList = results?.values as MutableList<Event>
                    notifyDataSetChanged()
                }
            }
        }





        override fun getItemViewType(position: Int): Int {
            return if(stringToDateConverter(appointments[position].end_date, getCurrentDateTime())){
                1
            } else {
                0
            }
        }

    }


    private fun toolbarTweak() {
        nextMonthButton = activity?.findViewById(R.id.nextMonthButton)!!
        nextMonthButton.visibility = View.GONE
        previousMonthButton = activity?.findViewById(R.id.previousMonthButton)!!
        previousMonthButton.visibility = View.GONE
        currentMothText = activity?.findViewById(R.id.monthYear_text)!!
        currentMothText.visibility = View.GONE
    }

    private fun updateUI() {
        val appointments = viewModel.appointments as MutableList<Event>
        appointments.reverse()
        adapter = AppointmentAdapter(appointments)
        binding.appointListRv.adapter = adapter
    }


    fun getCurrentDateTime():String {
        val c: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(c.time)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        var searchView: SearchView? = null
        val queryTextListener: SearchView.OnQueryTextListener
        inflater.inflate(R.menu.toolbar_menu, menu)
        val searchItem = menu.findItem(R.id.search)

        val searchManager =
            activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    adapter.filter.filter(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }
            }
            searchView.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun stringToDateConverter(stringDate1: String, stringDate2: String): Boolean{
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date1 = format.parse(stringDate1)
        val date2 = format.parse(stringDate2)
        return date1.compareTo(date2) <= 0
    }
}
