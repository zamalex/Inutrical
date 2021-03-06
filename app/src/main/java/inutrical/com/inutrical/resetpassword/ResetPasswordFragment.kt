package inutrical.com.inutrical.resetpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.gson.JsonObject
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.annotation.Password

import inutrical.com.inutrical.R
import inutrical.com.inutrical.main.MainActivity
import kotlinx.android.synthetic.main.fragment_reset_password.view.*
import javax.xml.validation.Validator
import kotlin.math.min

/**
 * A simple [Fragment] subclass.
 */
class ResetPasswordFragment : Fragment() {

    val args: ResetPasswordFragmentArgs by navArgs()
    lateinit var viewModel: ResetViewModel
    lateinit var validator: com.mobsandgeeks.saripaar.Validator
    lateinit var v: View

    @Password(min = 6, message = "enter minimum 6 chars")
    lateinit var pass_e: EditText

    @Password(min = 6, message = "enter minimum 6 chars")
    lateinit var repass_e: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_reset_password, container, false)
        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(ResetViewModel::class.java)
        validator = com.mobsandgeeks.saripaar.Validator(this)

        pass_e = v.findViewById(R.id.reset_pass_et)
        repass_e = v.findViewById(R.id.reset_confirm_pass_et)


        validator.setValidationListener(object :
            com.mobsandgeeks.saripaar.Validator.ValidationListener {
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

                body.addProperty("Mail", args.mail)
                body.addProperty("Code", args.code)
                body.addProperty("Password", pass_e.text.toString())

                (activity as MainActivity).showProgress(true)

                viewModel.resetPass(body)


            }
        })



        viewModel.result.observe(requireActivity(), Observer {
            (activity as MainActivity).showProgress(false)

            if (it!!.errorCode == 555)
                Toast.makeText(requireActivity(), "request timeout", Toast.LENGTH_LONG).show()

        })

        v.btn_login.setOnClickListener {
            validator.validate()
        }


        return v
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).changeToolbar(false)
        (activity as MainActivity).showToggle(false)
    }
}
