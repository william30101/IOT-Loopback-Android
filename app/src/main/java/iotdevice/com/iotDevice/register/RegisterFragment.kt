package iotdevice.com.iotDevice.register


import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iot_device.R
import iotdevice.com.iot_device.databinding.FragmentRegisterBinding
import java.util.*
import kotlin.concurrent.schedule

class RegisterFragment: Fragment() {

    lateinit var registerViewModel: RegisterViewModel

    private var _binding: FragmentRegisterBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        registerViewModel.registerSuccess.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(requireContext(), resources.getString(R.string.register_success), Toast.LENGTH_SHORT).show()
            Timer().schedule(1000){
                //do something
                activity?.finish()
            }
        })

        registerViewModel.registerFail.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            DialogUtils.createAlertDialog(requireContext(), getString(R.string.register_title), getString(R.string.register_fail))
        })

            binding.registerBtn?.setOnClickListener({ _ ->
                if (checkField()) {
                    registerViewModel.registerUser(
                        binding.emailEditText.text.toString(),
                        binding.usernameEditText.text.toString(),
                        binding.passwordEditText.text.toString())
                }
        })
    }

    private fun checkField(): Boolean {
        val email = binding.emailEditText.text.toString()
        if (!email.contains("@")) {
            DialogUtils.createAlertDialog(requireContext(), resources.getString(R.string.register_title), resources.getString(R.string.email_not_filled), {
                Log.i (tag, "clicked" )
            })
            return false
        }
        val username = binding.usernameEditText.text.toString()
        if (username.isEmpty()) {
            DialogUtils.createAlertDialog(requireContext(), resources.getString(R.string.register_title), resources.getString(R.string.username_not_filled), {
                Log.i (tag, "clicked" )
            })
            return false
        }
        val firstPassword = binding.passwordEditText.text.toString()
        val secondPassword = binding.passwordAgainEditText.text.toString()
        if (firstPassword != secondPassword) {

            DialogUtils.createAlertDialog(requireContext(), resources.getString(R.string.register_title), resources.getString(R.string.password_not_match), {
                Log.i ( tag, "clicked" )
            })
            return false
        }

        return true
    }
}