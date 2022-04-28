package inutrical.com.inutrical.search

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.print.PdfConverter
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.webviewtopdf.PdfView
import inutrical.com.inutrical.BuildConfig
import inutrical.com.inutrical.R
import inutrical.com.inutrical.RequestDietPlan
import inutrical.com.inutrical.WebViewActivity
import inutrical.com.inutrical.api.LocalData
import inutrical.com.inutrical.main.MainActivity
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.search_dialog.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(), HistoryClickListener {

    lateinit var viewModel: SearchViewModel
    lateinit var dialog: Dialog
     var v: View? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (v==null) {

            v = inflater.inflate(R.layout.fragment_search, container, false)



            dialog = Dialog(requireActivity())
            dialog.setContentView(R.layout.search_dialog)
            dialog.setCancelable(false)


            viewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                    .create(SearchViewModel::class.java)




            dialog.x_doalog.setOnClickListener {
                if (dialog != null && dialog.isShowing)
                    dialog.dismiss()
            }
            v!!.search_btn.setOnClickListener {
                var jsonObject = JsonObject()
                val imm: InputMethodManager? =
                    requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
//Hide:
//Hide:
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }


                jsonObject.addProperty("UserName", LocalData.getUser(requireActivity()).name)
                jsonObject.addProperty("Password", LocalData.getUser(requireActivity()).pass)
                jsonObject.addProperty("PatientNumber", v!!.p_num_et.text.toString())
                jsonObject.addProperty("PatientName", v!!.p_name_et.text.toString())

                (activity as MainActivity).showProgress(true)
                viewModel.search(jsonObject)

            }



            viewModel.result.observe(requireActivity(), Observer {
                (activity as MainActivity).showProgress(false)

                if (it != null) {
                    if (it != null && it.statusCode == 200 && it.data != null && it.data.isNotEmpty()) {
                        v!!.p_num_txt.text = "Patient Number: ${it.data[0]?.patientNumber}"
                        v!!.p_name_txt.text = "Patient Name: ${it.data[0]?.patientName}"
                        v!!.gender_txt.text = "Gender: ${it.data[0]?.gender}"
                        v!!.birth_txt.text = "Age: ${it.data[0]?.birthdate}"
                        v!!.weight_txt.text = "Weight (KG): ${it.data[0]?.patientWeight}"
                        v!!.height_txt.text = "Height (CM): ${it.data[0]?.patientHeight}"

                        val s = it.data[0]?.birthdate
                        val inFormat =
                            SimpleDateFormat("yyyy-MM-dd")
                        val dtIn = inFormat.parse(s)

                        v!!.update_diet_plan.visibility = View.VISIBLE

                        v!!.update_diet_plan.setOnClickListener { i ->
                            val bundle = bundleOf(
                                "weight" to it.data[0]?.patientWeight,
                                "height" to it.data[0]?.patientHeight,
                                "id" to it.data[0]?.patientNumber
                            )
                            v!!.findNavController()
                                .navigate(R.id.action_searchFragment_to_calculateFragment, bundle)

                        }

                        val date = inFormat.parse(it.data[0]?.birthdate)
                        val calendar = Calendar.getInstance(TimeZone.getDefault())
                        calendar.time = date

                        v!!.birth_txt.text = "Age: ${
                            (Calendar.getInstance()
                                .get(Calendar.YEAR) - calendar[Calendar.YEAR]).toString()
                        }"


                        var jsonObject = JsonObject()

                        jsonObject.addProperty("PatientId", it.data[0]?.patientNumber)
                        jsonObject.addProperty("Index", "0")
                        jsonObject.addProperty("Size", "10")
                        viewModel.getHistory(jsonObject)
                    } else if (it != null && it.statusCode == 555) {
                        v!!.p_num_txt.text = "Patient Number:"
                        v!!.p_name_txt.text = "Patient Name:"
                        v!!.gender_txt.text = "Gender:"
                        v!!.birth_txt.text = "Age:"
                        v!!.weight_txt.text = "Weight (KG):"
                        v!!.height_txt.text = "Height (CM):"
                        v!!.search_rv.apply {
                            layoutManager = LinearLayoutManager(requireActivity())
                            adapter =
                                SearchAdapter(requireActivity(), ArrayList(), this@SearchFragment)
                        }
                        Toast.makeText(requireActivity(), "request timeout", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        v!!.p_num_txt.text = "Patient Number:"
                        v!!.p_name_txt.text = "Patient Name:"
                        v!!.gender_txt.text = "Gender:"
                        v!!.birth_txt.text = "Age:"
                        v!!.weight_txt.text = "Weight (KG):"
                        v!!.height_txt.text = "Height (CM):"
                        v!!.search_rv.apply {
                            layoutManager = LinearLayoutManager(requireActivity())
                            adapter =
                                SearchAdapter(requireActivity(), ArrayList(), this@SearchFragment)
                        }
                        Toast.makeText(requireActivity(), "not found", Toast.LENGTH_LONG).show()
                    }
                }


            })


            viewModel.history.observe(requireActivity(), Observer {
                (activity as MainActivity).showProgress(false)
                if (it != null) {
                    if (it.errorCode == 200 && it != null) {
                        v!!.search_rv.apply {
                            layoutManager = LinearLayoutManager(requireActivity())
                            adapter = SearchAdapter(requireActivity(), it.data, this@SearchFragment)
                        }


                        /* try {
                         val dwldsPath =
                             File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/hhhh" + ".pdf")
                         val pdfAsBytes: ByteArray = Base64.decode(it.data[1].medicalHistory.uRL.replace("data:application/pdf;base64,","").trim(), 0)
                         val os: FileOutputStream
                         os = FileOutputStream(dwldsPath, false)
                         os.write(pdfAsBytes)
                         os.flush()
                         os.close()
                     } catch (e: IOException) {
                         Log.d("errrr", e.message)
                         e.printStackTrace()
                     }
       */
                        /*
                    *   val share = Intent()
                         share.action = Intent.ACTION_SEND
                         share.type = "application/pdf"
                         share.putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(
                             requireActivity()!!,
                             BuildConfig.APPLICATION_ID.toString() + ".provider",
                             dwldsPath
                         ))
                         startActivity(Intent.createChooser(share, "Share"))
                    * */

                    } else if (it != null && it.errorCode == 555)
                        Toast.makeText(requireActivity(), "request timeout", Toast.LENGTH_LONG)
                            .show()
                }

            })

            viewModel.htmlReponse.observe(viewLifecycleOwner, Observer {
                (activity as MainActivity).showProgress(false)

                if (it != null) {

                    Dexter.withContext(activity)
                        .withPermissions(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        .withListener(object : MultiplePermissionsListener {
                            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    val converter = PdfConverter.getInstance()
                                    val file =
                                        File(
                                            Environment.getExternalStoragePublicDirectory(
                                                Environment.DIRECTORY_DOWNLOADS
                                            ).toString(), "diet_plan.pdf"
                                        )
                                    var htmlString: String = "${it.string().replace("\\r\\n", " ")}"


                                    startActivity(
                                        Intent(
                                            requireActivity(),
                                            WebViewActivity::class.java
                                        ).apply {
                                            putExtra("html", htmlString)
                                        })


                                }

                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    // Toast.makeText(getActivity(), "قم بالسماح للتطبيق للوصول الى موقعك من خلال الاعدادات", Toast.LENGTH_LONG).show();
                                }
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                permissions: List<PermissionRequest?>?,
                                token: PermissionToken
                            ) {
                                token.continuePermissionRequest()
                            }
                        })
                        .onSameThread()
                        .check()

                } else
                    Toast.makeText(requireActivity(), "failed to generate pdf", Toast.LENGTH_LONG)
                        .show()
            })
            viewModel.pdfresponse.observe(requireActivity(), Observer {
                (activity as MainActivity).showProgress(false)

                if (it != null) {
                    if (it)
                        Toast.makeText(
                            requireActivity(),
                            "pdf downloaded successfully",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    else
                        Toast.makeText(
                            requireActivity(),
                            "failed to generate pdf",
                            Toast.LENGTH_LONG
                        )
                            .show()


                } else
                    Toast.makeText(requireActivity(), "failed to generate pdf", Toast.LENGTH_LONG)
                        .show()

            })

        }
        // Inflate the layout for this fragment
        return v
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).changeToolbar(true)
        (activity as MainActivity).showToggle(false)
    }

    override fun onItemClick(obj: HistoryModel.Data) {
        showDoalog()



        dialog.dietation.text = obj.clinicalDietation
        dialog.phy_name.text = obj.physicianName

        try {
            val cal = Calendar.getInstance()

            val s = obj.followUpDate
            val inFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val dtIn = inFormat.parse(s)

            val month_date = SimpleDateFormat("dd MMMM yyyy")
            val month_name: String = month_date.format(dtIn)
            dialog.c_date.text = month_name

        } catch (e: Exception) {
            dialog.c_date.text = obj.followUpDate

        }


        dialog.medical_history_btn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                if (requireActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (obj.medicalHistory != null)

                        if (!obj.medicalHistory.isEmpty()) {
                            decodePdf("medicalhistory", obj.medicalHistory)
                        }
                } else
                    requireActivity().requestPermissions(
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1200
                    )


            } else {
                if (obj.medicalHistory != null)

                    if (!obj.medicalHistory.isEmpty()) {
                        decodePdf("medicalhistory", obj.medicalHistory)
                    }
            }
        }

        dialog.lab_res_btn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                if (requireActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (obj.labResults != null)
                        if (!obj.labResults.isEmpty()) {
                            decodePdf("labresult", obj.labResults)
                        }
                } else
                    requireActivity().requestPermissions(
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1200
                    )


            } else {
                if (obj.labResults != null)

                    if (!obj.labResults.isEmpty()) {
                        decodePdf("labresult", obj.labResults)
                    }
            }
        }
        dialog.diet_plan.setOnClickListener {

            (activity as MainActivity).showProgress(true)
            viewModel.exportPdf(RequestDietPlan(obj.id, LocalData.getUser(requireActivity()).pass!!,LocalData.getUser(requireActivity()).name!!))
        }

    }

    fun generateFromHtml(html:String){

    }

    fun showDoalog() {
        if (dialog != null) {
            dialog.show()
            val window: Window = dialog.getWindow()!!
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
    }


    fun decodePdf(filename: String, decode: String) {
        viewModel.downloadFile(decode,requireActivity(),filename)
    }


}
