package com.ardritkrasniqi.prenotimi.ui.authUi

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.FragmentAuthBinding
import com.ardritkrasniqi.prenotimi.model.LoginRequest
import com.ardritkrasniqi.prenotimi.preferences.PreferenceProvider
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_auth.*


/**
 * A simple [Fragment] subclass.
 */

class AuthFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAuthBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false)
        // Inflate the layout for this fragment
        val authViewModel = ViewModelProvider(this).get(AuthFragmentViewModel::class.java)


        binding.lifecycleOwner = this
        binding.authViewModel = authViewModel


        val sharedP = PreferenceProvider(this.requireContext())




        authViewModel.authToken.observe(viewLifecycleOwner, Observer { newToken ->
            sharedP.saveToken(newToken)
        })


        binding.apply {
            forgotpasswordText.paintFlags =
                forgotpasswordText.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            authViewModel.status.observe(viewLifecycleOwner, Observer { message ->
                if (message.isNotEmpty()) {
                    if (message == "token_generated") {
                        NavHostFragment.findNavController(this@AuthFragment)
                            .navigate(R.id.mainFragment)
                    } else {
                        makeToast(message)
                    }
                    authViewModel.clearStatus()
                }
            })






            loginButton.setOnClickListener {
                if (etUsername.text.contains("@") && etUsername.text.isNotBlank()) {
                    authViewModel.loginRequest.value = LoginRequest(
                        etUsername.text.toString().trim(),
                        et_password.text.toString().trim()
                    )
                    authViewModel.authenticate()
                } else {
                    makeToast("Please fill required fields with valid data")
                }
            }
        }




        return binding.root

    }


    private fun makeToast(message: String): Unit? {
        return if (message == "token_generated") {
            context?.let { Toasty.success(it, message, Toasty.LENGTH_SHORT).show() }
        } else
            context?.let { Toasty.error(it, message, Toasty.LENGTH_SHORT).show() }
    }


}
