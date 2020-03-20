package com.ardritkrasniqi.prenotimi.ui.mainPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ardritkrasniqi.prenotimi.model.Event
import com.ardritkrasniqi.prenotimi.network.ApiService
import com.ardritkrasniqi.prenotimi.preferences.PreferenceProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    var events: MutableList<Event> = mutableListOf()


}
