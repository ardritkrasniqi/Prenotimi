package com.ardritkrasniqi.prenotimi.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager

private const val CURRENT_TOKEN = "current_token"

class PreferenceProvider(context: Context?) {

    private val appContext: Context? = context?.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)



    fun saveToken(token: LiveData<String>){
        preference.edit().putString(
            CURRENT_TOKEN,
            token.value
        ).apply()
    }

    fun getToken() : String?{
        return preference.getString(CURRENT_TOKEN, "")
    }



}