package com.ardritkrasniqi.prenotimi.ui.noInternet



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.utils.DrawerLocker

/**
 * A simple [Fragment] subclass.
 */
class NoInternetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as DrawerLocker?)?.setDrawerLocked(true)
        return inflater.inflate(R.layout.fragment_no_internet, container, false)
    }

}
