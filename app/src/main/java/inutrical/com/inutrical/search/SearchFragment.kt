package inutrical.com.inutrical.search

import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import inutrical.com.inutrical.R
import inutrical.com.inutrical.RequestDietPlan
import inutrical.com.inutrical.api.LocalData
import inutrical.com.inutrical.main.MainActivity
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.search_dialog.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(), HistoryClickListener {

    lateinit var viewModel: SearchViewModel
    lateinit var dialog: Dialog
    lateinit var v: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
        v.search_btn.setOnClickListener {
            var jsonObject = JsonObject()

            jsonObject.addProperty("UserName", LocalData.getUser(requireActivity()).name)
            jsonObject.addProperty("Password", LocalData.getUser(requireActivity()).pass)
            jsonObject.addProperty("PatientNumber",v.p_num_et.text.toString() )
            jsonObject.addProperty("PatientName", v.p_name_et.text.toString())

            (activity as MainActivity).showProgress(true)
            viewModel.search(jsonObject)

        }



        viewModel.result.observe(requireActivity(), Observer {
            (activity as MainActivity).showProgress(false)

            if (it != null) {
                if (it != null && it.errorCode == 0&&it.data!=null) {
                    v.p_num_txt.text = "Patient Number: ${it.data.number}"
                    v.p_name_txt.text = "Patient Name: ${it.data.name}"
                    v.gender_txt.text = "Gender: ${it.data.gender}"
                    v.birth_txt.text = "Age: ${it.data.dateOfBirth}"
                    v.weight_txt.text = "Weight (KG): ${it.data.weight}"
                    v.height_txt.text = "Height (CM): ${it.data.height}"

                    val s = it.data.dateOfBirth
                    val inFormat =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val dtIn = inFormat.parse(s)




                    val date = inFormat.parse(it.data.dateOfBirth)
                    val calendar = Calendar.getInstance(TimeZone.getDefault())
                    calendar.time = date

//                    v.birth_txt.text = "Age: ${(Calendar.getInstance().get(Calendar.YEAR)-calendar[Calendar.YEAR]).toString()}"


                    var jsonObject = JsonObject()

                    jsonObject.addProperty("UserName", LocalData.getUser(requireActivity()).name)
                    jsonObject.addProperty("Password", LocalData.getUser(requireActivity()).pass)
                    jsonObject.addProperty("PatientId", it.data.id)
                    jsonObject.addProperty("Index", "0")
                    jsonObject.addProperty("Size", "10")
                    viewModel.getHistory(jsonObject)
                } else if (it != null && it.errorCode == 555)
                    Toast.makeText(requireActivity(), "request timeout", Toast.LENGTH_LONG).show()
            }


        })


        viewModel.history.observe(requireActivity(), Observer {
            (activity as MainActivity).showProgress(false)
            if (it != null) {
                if (it.errorCode == 0 && it != null) {
                    v.search_rv.apply {
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
                    Toast.makeText(requireActivity(), "request timeout", Toast.LENGTH_LONG).show()
            }

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
                    Toast.makeText(requireActivity(), "failed to generate pdf", Toast.LENGTH_LONG)
                        .show()


            } else
                Toast.makeText(requireActivity(), "failed to generate pdf", Toast.LENGTH_LONG)
                    .show()

        })
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

                        if (!obj.medicalHistory.uRL.isEmpty()) {
                            decodePdf("medicalhistory", obj.medicalHistory.uRL)
                        }
                } else
                    requireActivity().requestPermissions(
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1200
                    )


            } else {
                if (obj.medicalHistory != null)

                    if (!obj.medicalHistory.uRL.isEmpty()) {
                        decodePdf("medicalhistory", obj.medicalHistory.uRL)
                    }
            }
        }

        dialog.lab_res_btn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                if (requireActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (obj.labResults != null)
                        if (!obj.labResults.uRL.isEmpty()) {
                            decodePdf("labresult", obj.labResults.uRL)
                        }
                } else
                    requireActivity().requestPermissions(
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1200
                    )


            } else {
                if (obj.labResults != null)

                    if (!obj.labResults.uRL.isEmpty()) {
                        decodePdf("labresult", obj.labResults.uRL)
                    }
            }
        }
        dialog.diet_plan.setOnClickListener {

            (activity as MainActivity).showProgress(true)
            viewModel.exportPdf(RequestDietPlan(obj.Id, LocalData.getUser(requireActivity()).pass!!,LocalData.getUser(requireActivity()).name!!))
        }

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
