package com.ardritkrasniqi.prenotimi.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager

private const val CURRENT_TOKEN = "current_token"
private const val APPOINTMENT_ADDED = "appointment_added"

class PreferenceProvider(context: Context?) {

    private val appContext: Context? = context?.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)



    fun saveToken(token: String){
        preference.edit().putString(
            CURRENT_TOKEN,
            token
        ).apply()
    }

    fun getToken() : String?{
        return preference.getString(CURRENT_TOKEN, "")
    }

    fun saveIsAppointmentAdded(trueOrFalse: Boolean){
        return preference.edit().putBoolean(APPOINTMENT_ADDED, trueOrFalse).apply()
    }

    fun getIsappointmentAdded(): Boolean{
        return preference.getBoolean(APPOINTMENT_ADDED, false)
    }



}