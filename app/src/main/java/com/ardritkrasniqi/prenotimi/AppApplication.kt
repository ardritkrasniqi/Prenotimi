package com.ardritkrasniqi.prenotimi

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class AppApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        // initialises the androidthreeten lib to core app
        AndroidThreeTen.init(this)
    }

}