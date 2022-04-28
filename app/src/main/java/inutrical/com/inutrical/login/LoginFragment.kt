package inutrical.com.inutrical.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.mobsandgeeks.saripaar.annotation.Password
import inutrical.com.inutrical.R
import inutrical.com.inutrical.api.LocalData
import inutrical.com.inutrical.api.Retrofit
import inutrical.com.inutrical.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    lateinit var validation: Validator

    lateinit var viewModel: LoginViewModel

    @NotEmpty
    lateinit var mail: EditText

    @NotEmpty
    lateinit var pass: EditText

    lateinit var mview: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        mview = inflater.inflate(R.layout.fragment_login, container, false)


        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(LoginViewModel::class.java)
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

                body.addProperty("username", mail.text.toString())
                body.addProperty("password", pass.text.toString())
                /* val action = LoginFragmentDirections.actionLoginFragmentToMainFragment("Welcome")

                 mview.findNavController().navigate(action)*/


                (activity as MainActivity).showProgress(true)
                viewModel.login(body)
            }
        })


        viewModel.result.observe(requireActivity(), object : Observer<LoginModel> {
            override fun onChanged(t: LoginModel?) {

                (activity as MainActivity).showProgress(false)

                if (t != null) {
                    when (t!!.status_code) {
                        200 -> {
                            Toast.makeText(requireActivity(), "Success", Toast.LENGTH_LONG).show()
                            LocalData.saveUser(
                                mail.text.toString(),
                                t.data?.get(0)?.token,
                                t.data?.get(0)?.name,
                                requireActivity()
                            )
                            val action =
                                LoginFragmentDirections.actionLoginFragmentToMainFragment("welcome")

                            mview.findNavController().navigate(action)
                        }
                        1 -> Toast.makeText(
                            requireActivity(),
                            "Account is locked",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        2 -> Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show()
                        3 -> Toast.makeText(requireActivity(), "Technical Error", Toast.LENGTH_LONG)
                            .show()
                        4 -> Toast.makeText(requireActivity(), "Invalid Input", Toast.LENGTH_LONG)
                            .show()
                        401 -> Toast.makeText(requireActivity(), "Unauthorized", Toast.LENGTH_LONG)
                            .show()
                        555 -> Toast.makeText(
                            requireActivity(),
                            "request timeout",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }


            }
        })

        mail = mview.findViewById(R.id.login_email_et)
        pass = mview.findViewById(R.id.password_in_login)

        mview.btn_login.setOnClickListener {


            (activity as MainActivity).hideKeybaord(mview)

            validation.validate()


        }
        mview.forgot_pass.setOnClickListener {
            mview.findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }



        return mview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (LocalData.getUser(requireActivity()).name != null) {
            val action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
            view.findNavController().navigate(action)
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.hide()

    }
}
