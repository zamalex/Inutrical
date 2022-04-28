package inutrical.com.inutrical.forgotpassword

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.gson.JsonObject
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import inutrical.com.inutrical.R
import inutrical.com.inutrical.api.LocalData
import inutrical.com.inutrical.main.MainActivity
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*
import kotlinx.android.synthetic.main.validate_code.*

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment : Fragment() {
    lateinit var mview: View
    lateinit var validation: Validator
    lateinit var viewModel: SendCodeViewModel
    lateinit var dialog: Dialog
    lateinit var validator: Validator

    lateinit var code_edit: EditText

    lateinit var ccode:String

    @Email(message = "Enter Valid Email")
    lateinit var forgot_email_et: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mview = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        validator = Validator(this)

        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(SendCodeViewModel::class.java)


        dialog = Dialog(requireActivity())
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.validate_code)
        code_edit = dialog.findViewById(R.id.code_et)

        forgot_email_et = mview.findViewById(R.id.forgot_email_et)


        dialog.validate_btn.setOnClickListener {
            if (!code_edit.text.isEmpty()) {
                val body: JsonObject = JsonObject()
                dialog.dismiss()
                body.addProperty("Mail", forgot_email_et.text.toString())
                body.addProperty("email", forgot_email_et.text.toString())
                body.addProperty("code",code_edit.text.toString() )
                ccode = code_edit.text.toString()
                (activity as MainActivity).showProgress(true)

                viewModel.verifyCode(body)
            }

        }




        validation = Validator(this)
        validation.setValidationListener(object : Validator.ValidationListener {
            override fun onValidationFailed(errors: MutableList<ValidationError>?) {
                for (error in errors!!) {
                    val view = error.view
                    val message = error.getCollatedErrorMessage(activity)
                    (view as EditText).error = message


                }
            }

            override fun onValidationSucceeded() {
                // callApi
                val body: JsonObject = JsonObject()

                body.addProperty("email", forgot_email_et.text.toString())


                (activity as MainActivity).showProgress(true)
                viewModel.getCode(body)


            }
        })

        mview.btn_forgot.setOnClickListener {
            validation.validate()

        }


        viewModel.result.observe(requireActivity(), Observer { res ->
            (activity as MainActivity).showProgress(false)

          //  dialog.show()
            val window: Window = dialog.getWindow()!!
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


            when (res.errorCode) {
                200 -> {
                    dialog.show()
                }
                1 -> Toast.makeText(requireActivity(), "User not exist", Toast.LENGTH_LONG).show()
                2 -> Toast.makeText(requireActivity(), "Failed to generate code", Toast.LENGTH_LONG)
                    .show()
                4 -> Toast.makeText(requireActivity(), "Technical error", Toast.LENGTH_LONG).show()
                555 -> Toast.makeText(requireActivity(), "request timeout", Toast.LENGTH_LONG).show()
                else->Toast.makeText(requireActivity(), "server error", Toast.LENGTH_LONG).show()

            }
        })


        viewModel.verifyResult.observe(requireActivity(), Observer {
            (activity as MainActivity).showProgress(false)
            if (dialog.isShowing)
                dialog.dismiss()
            if (it.errorCode != 200)
                Toast.makeText(requireActivity(), "invalid code", Toast.LENGTH_LONG).show()
            else {
                val action =
                    ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToResetPasswordFragment(
                        forgot_email_et.text.toString(),
                        ccode
                    )
                mview.findNavController().navigate(action)
            }


        })

        return mview
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).changeToolbar(false)
        (activity as MainActivity).showToggle(false)

    }


}
