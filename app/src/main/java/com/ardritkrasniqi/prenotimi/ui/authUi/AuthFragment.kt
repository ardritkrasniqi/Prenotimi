package com.ardritkrasniqi.prenotimi.ui.authUi

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.FragmentAuthBinding
import com.ardritkrasniqi.prenotimi.model.LoginRequest
import com.ardritkrasniqi.prenotimi.preferences.PreferenceProvider
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


        saveTo


        binding.apply {
            forgotpasswordText.paintFlags = forgotpasswordText.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            loginButton.setOnClickListener{
                if(etUsername.text.contains("@") && etUsername.text.isNotBlank()) {
                    authViewModel.loginRequest.value = LoginRequest(
                        etUsername.text.toString().trim(),
                        et_password.text.toString().trim())
                    authViewModel.authenticate()
                } else
                {
                    Toast.makeText(context, "Please fill required fields with valid data", Toast.LENGTH_SHORT).show()
                }
            }




        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }



}
