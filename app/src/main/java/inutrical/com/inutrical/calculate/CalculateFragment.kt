package inutrical.com.inutrical.calculate

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.icu.util.LocaleData
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.JsonObject
import com.hendrix.pdfmyxml.PdfDocument
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer
import inutrical.com.inutrical.BuildConfig
import inutrical.com.inutrical.R
import inutrical.com.inutrical.api.LocalData
import inutrical.com.inutrical.main.MainActivity
import kotlinx.android.synthetic.main.fragment_calculate.*
import kotlinx.android.synthetic.main.fragment_calculate.view.*
import kotlinx.android.synthetic.main.fragment_calculate.view.height_et
import java.io.File
import java.util.jar.Manifest


/**
 * A simple [Fragment] subclass.
 */
class CalculateFragment : Fragment() {

    var formulaArr: ArrayList<Lookup.Formula> = ArrayList<Lookup.Formula>()
    var lookup: Lookup? = null
    lateinit var viewModel: CalculateViewMoldel
    lateinit var mview: View
    lateinit var formulaName: String
    var bbws:String = "IBW:-\nBMI:-\nABW:-"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mview = inflater.inflate(R.layout.fragment_calculate, container, false)
        formulaName = "not specified"


        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(CalculateViewMoldel::class.java)


        getCats()

        val pieChart: PieChart = mview.findViewById(R.id.piechart)
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(16.6f, ""))

        listColors.add(resources.getColor(R.color.mwhite))
        listPie.add(PieEntry(16.6f, ""))

        listColors.add(resources.getColor(R.color.mwhite))
        listPie.add(PieEntry(16.6f, ""))
        listColors.add(resources.getColor(R.color.mwhite))
        listPie.add(PieEntry(16.6f, ""))
        listColors.add(resources.getColor(R.color.mwhite))
        listPie.add(PieEntry(16.6f, ""))
        listColors.add(resources.getColor(R.color.mwhite))
        listPie.add(PieEntry(16.6f, ""))
        listColors.add(resources.getColor(R.color.mwhite))

        val typeface: Typeface =
            Typeface.createFromAsset(requireActivity().assets, "fonts/arialmt.ttf")
        pieChart.setEntryLabelTypeface(Typeface.create(typeface, Typeface.BOLD))
        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(0f)
        pieChart.data = pieData

        pieChart.holeRadius = 22f
        pieChart.setCenterTextSize(22f)
        pieChart.legend.isEnabled = false
        pieChart.setUsePercentValues(true)
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(resources.getColor(R.color.cardgrey))
        pieChart.centerText = "BMI"
        pieChart.setCenterTextColor(resources.getColor(R.color.mgreen))
        pieChart.offsetLeftAndRight(10)
        pieChart.offsetTopAndBottom(10)
        pieDataSet.sliceSpace = 5f
        pieChart.description.isEnabled = false
        //pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.setEntryLabelColor(resources.getColor(R.color.mwhite))
        pieChart.setEntryLabelTextSize(10f)

        pieChart.transparentCircleRadius = 0f





        pieChart.isRotationEnabled = false


        val myStrings = arrayOf("Choose Formula Category", "One", "Two", "Three", "Four")
        var adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            myStrings
        )
        mview.spinner.adapter = adapter
        val myStrings2 = arrayOf("Choose Formula Name", "One", "Two", "Three", "Four")
        var adapter2 = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            myStrings2
        )
        mview.spinner2.adapter = adapter2


        mview.weight_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!mview.weight_et.text.isEmpty() && !mview.height_et.text.isEmpty()) {
                    mview.apply_btn.isEnabled = true
                } else {
                    mview.apply_btn.isEnabled = false

                }

                if (!mview.weight_et.text.isEmpty() && !mview.height_et.text.isEmpty() && !mview.cal_et.text.isEmpty() && formulaArr.size > 0 && spinner2.selectedItemPosition >= 0) {
                    mview.show_results.isEnabled = true
                } else {
                    mview.show_results.isEnabled = false

                }

            }
        })


        mview.show_results.setOnClickListener {


            var jsonObject = JsonObject()

            jsonObject.addProperty("UserName", LocalData.getUser(requireActivity()).name)
            jsonObject.addProperty("Password", LocalData.getUser(requireActivity()).pass)
            jsonObject.addProperty("Weight", mview.weight_et.text.toString())
            jsonObject.addProperty("Height", mview.height_et.text.toString())
            jsonObject.addProperty("FormulaId", formulaArr[mview.spinner2.selectedItemPosition].id)
            jsonObject.addProperty("TotalCalories", mview.cal_et.text.toString())
            (activity as MainActivity).showProgress(true)
            (activity as MainActivity).hideKeybaord(mview)
            viewModel.calculateResult(jsonObject)
        }

        mview.height_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!mview.weight_et.text.isEmpty() && !mview.height_et.text.isEmpty()) {
                    mview.apply_btn.isEnabled = true
                } else {
                    mview.apply_btn.isEnabled = false

                }

                if (!mview.weight_et.text.isEmpty() && !mview.height_et.text.isEmpty() && !mview.cal_et.text.isEmpty() && formulaArr.size > 0 && spinner2.selectedItemPosition >= 0) {
                    mview.show_results.isEnabled = true
                } else {
                    mview.show_results.isEnabled = false

                }

            }
        })

        mview.cal_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!mview.weight_et.text.isEmpty() && !mview.height_et.text.isEmpty() && !mview.cal_et.text.isEmpty() && formulaArr.size > 0 && spinner2.selectedItemPosition >= 0) {
                    mview.show_results.isEnabled = true
                } else {
                    mview.show_results.isEnabled = false

                }

            }
        })


        mview.apply_btn.setOnClickListener {
            var obj = JsonObject()
            obj.addProperty("UserName", LocalData.getUser(requireActivity()).name)
            obj.addProperty("Password", LocalData.getUser(requireActivity()).pass)
            obj.addProperty("Weight", mview.weight_et.text.toString())
            obj.addProperty("Height", mview.height_et.text.toString())
            (activity as MainActivity).hideKeybaord(mview)
            (activity as MainActivity).showProgress(true)
            viewModel.calculateBmi(obj)
        }

        mview.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                formulaArr = ArrayList<Lookup.Formula>()

                if (!mview.weight_et.text.isEmpty() && !mview.height_et.text.isEmpty() && !mview.cal_et.text.isEmpty() && formulaArr.size > 0 && spinner2.selectedItemPosition >= 0) {
                    mview.show_results.isEnabled = true
                } else {
                    mview.show_results.isEnabled = false

                }
                if (lookup != null) {
                    for (f in lookup!!.formulas) {
                        if (f.categoryId == lookup!!.categories[position].id) {
                            formulaArr.add(f)
                        }

                    }
                    var adapter = ArrayAdapter(
                        requireActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        formulaArr
                    )
                    mview.spinner2.adapter = adapter
                }

            }
        }

        mview.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                mview.show_results.isEnabled = false

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!mview.weight_et.text.isEmpty() && !mview.height_et.text.isEmpty() && !mview.cal_et.text.isEmpty() && formulaArr.size > 0 && spinner2.selectedItemPosition >= 0) {
                    mview.show_results.isEnabled = true
                } else {
                    mview.show_results.isEnabled = false

                }

            }
        }

        viewModel.bmiResult.observe(requireActivity(), object : Observer<BmiModel> {
            override fun onChanged(t: BmiModel?) {
                (activity as MainActivity).showProgress(false)

                if (t != null) {
                    if (t!!.errorCode == 555) {
                        Toast.makeText(requireActivity(), "request timeout", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        if (t != null&&t.data!=null) {
                            mview.ibw_tv.text = "IBW : " + t.data.iBW.toString() + "kg"
                            mview.bmi_tv.text = "BMI : ${t.data.bMI} kg/m (${t.data.bMIText})"
                            mview.abw_tv.text = "ABW : " + t.data.aBW.toString()


                            bbws="IBW : " + t.data.iBW.toString() + "kg"+"\nBMI : ${t.data.bMI} kg/m (${t.data.bMIText})"+"\nABW : " + t.data.aBW.toString()
                            when {
                                t.data.bMI < 18.5 -> {
                                    pieChart.highlightValue(5f, 0, false);
                                    pieDataSet.colors[0] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[1] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[2] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[3] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[4] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[5] = resources.getColor(R.color.mgreen)
                                }
                                t.data.bMI >= 25 && t.data.bMI <= 29.9 -> {
                                    pieChart.highlightValue(0f, 0, false);
                                    pieDataSet.colors[0] = resources.getColor(R.color.mgreen)
                                    pieDataSet.colors[1] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[2] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[3] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[4] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[5] = resources.getColor(R.color.mwhite)
                                }
                                t.data.bMI >= 18.5 && t.data.bMI <= 24.9 -> {
                                    pieChart.highlightValue(1f, 0, false);
                                    pieDataSet.colors[0] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[1] = resources.getColor(R.color.mgreen)
                                    pieDataSet.colors[2] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[3] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[4] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[5] = resources.getColor(R.color.mwhite)
                                }

                                t.data.bMI >= 30 && t.data.bMI <= 34.9 -> {
                                    pieChart.highlightValue(2f, 0, false);
                                    pieDataSet.colors[0] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[1] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[2] = resources.getColor(R.color.mgreen)
                                    pieDataSet.colors[3] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[4] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[5] = resources.getColor(R.color.mwhite)
                                }
                                t.data.bMI >= 35 && t.data.bMI <= 39.9 -> {
                                    pieChart.highlightValue(3f, 0, false);
                                    pieDataSet.colors[0] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[1] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[2] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[3] = resources.getColor(R.color.mgreen)
                                    pieDataSet.colors[4] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[5] = resources.getColor(R.color.mwhite)
                                }
                                t.data.bMI >= 40 -> {
                                    pieChart.highlightValue(4f, 0, false);
                                    pieDataSet.colors[0] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[1] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[2] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[3] = resources.getColor(R.color.mwhite)
                                    pieDataSet.colors[4] = resources.getColor(R.color.mgreen)
                                    pieDataSet.colors[5] = resources.getColor(R.color.mwhite)
                                }
                            }
                        }
                    }
                }


            }
        })


        viewModel.catResult.observe(requireActivity(), object : Observer<Lookup> {
            override fun onChanged(t: Lookup?) {
                (activity as MainActivity).showProgress(false)


                if (t != null) {
                    lookup = t
                    var adapter = ArrayAdapter(
                        requireActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        t.categories
                    )
                    mview.spinner.adapter = adapter
                }
            }
        })


        viewModel.calculateResult.observe(requireActivity(), Observer { res ->
            (activity as MainActivity).showProgress(false)

            if (res != null) {
                if (res.errorCode == 555)
                    Toast.makeText(requireActivity(), "request timeout", Toast.LENGTH_LONG).show()

                if (res.errorCode == 0) {
                    mview.cal_txt.text = "Calories: ${cal_et.text.toString()} kcal/day"
                    if (mview.spinner2.selectedItemPosition >= 0)
                        mview.formula_txt.text =
                            "Formula Name: ${formulaArr[mview.spinner2.selectedItemPosition].name} "
                    mview.volume_txt.text = "Total Volume: ${res.data.totalVolume} ml"
                    mview.total_cal_txt.text = "Total Calories: ${res.data.totalCalories} kcal"
                    mview.proten_txt.text = "Protein: ${res.data.protein} g"
                    mview.carb_txt.text = "Carb: ${res.data.carb} g"
                    mview.fat_txt.text = "Fat: ${res.data.fAT} g"
                    mview.na_txt.text = "Na: ${res.data.nA} mg"
                    mview.k_txt.text = "K: ${res.data.k} mg"
                    mview.water_txt.text = "Water: ${res.data.water} ml"
                    mview.cans_txt.text = "Number of Cans: ${res.data.numberOFCans}"
                    formulaName = formulaArr[mview.spinner2.selectedItemPosition].name


                }
            }


        })

        mview.btn_done.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                if (requireActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (viewModel.calculateResult.value!=null)
                    if (viewModel.calculateResult.value!!.errorCode == 0)
                        generatePdf(viewModel.calculateResult.value!!)
                } else
                    requireActivity().requestPermissions(
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1200
                    )


            } else {
                if (viewModel.calculateResult.value!!.errorCode == 0)
                    generatePdf(viewModel.calculateResult.value!!)
            }
        }

        return mview
    }

    private fun getCats() {
        var jsonObject = JsonObject()

        jsonObject.addProperty("UserName", LocalData.getUser(requireActivity()).name)
        jsonObject.addProperty("Password", LocalData.getUser(requireActivity()).pass)
        (activity as MainActivity).showProgress(true)

        viewModel.getCats(jsonObject)
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).changeToolbar(true)
        (activity as MainActivity).showToggle(false)
    }

    fun generatePdf(res: CalculateResultModel) {
        val page: AbstractViewRenderer =
            object : AbstractViewRenderer(activity, R.layout.fragment_calculate) {
                override fun initView(vieww: View) {
                    vieww.weight_et.visibility = View.GONE
                    vieww.weight_tv.visibility = View.GONE
                    vieww.height_et.visibility = View.GONE
                    vieww.height_tv.visibility = View.GONE
                    vieww.apply_btn.visibility = View.GONE
                    vieww.show_results.visibility = View.GONE
                    vieww.spinner.visibility = View.GONE
                    vieww.cardView2.visibility = View.GONE
                    vieww.textView8.visibility = View.GONE
                    vieww.cal_et.visibility = View.GONE
                    vieww.btn_cancel.visibility = View.GONE
                    vieww.btn_done.visibility = View.GONE
                    vieww.spinner2.visibility = View.GONE


                    if (res.errorCode == 0) {
                        vieww.cal_txt.text = "Calories: ${cal_et.text.toString()} kcal/day"
                        vieww.formula_txt.text = "Formula Name: ${formulaName} "
                        vieww.volume_txt.text = "Total Volume: ${res.data.totalVolume} ml"
                        vieww.total_cal_txt.text = "Total Calories: ${res.data.totalCalories} kcal"
                        vieww.proten_txt.text = "Protein: ${res.data.protein} g"
                        vieww.carb_txt.text = "Carb: ${res.data.carb} g"
                        vieww.fat_txt.text = "Fat: ${res.data.fAT} g"
                        vieww.na_txt.text = "Na: ${res.data.nA} mg"
                        vieww.k_txt.text = "K: ${res.data.k} mg"
                        vieww.water_txt.text = "Water: ${res.data.water} ml"
                        vieww.cans_txt.text = "Number of Cans: ${res.data.numberOFCans}"
                        vieww.bbw_txt.text=bbws
                        vieww.dr_txt.text="Dr/${LocalData.getUser(requireActivity()).name}"

                    }


                }
            }

// you can reuse the bitmap if you want

// you can reuse the bitmap if you want
        page.isReuseBitmap = true

        val doc = PdfDocument(activity)

// add as many pages as you have

// add as many pages as you have
        doc.addPage(page)

        doc.setRenderWidth(2115)
        doc.setRenderHeight(1500)
        doc.orientation = PdfDocument.A4_MODE.LANDSCAPE
        doc.setProgressTitle(R.string.app_name)
        doc.setProgressMessage(R.string.app_name)
        doc.fileName = "calculate_result"
        doc.setSaveDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
        doc.setInflateOnMainThread(false)
        doc.setListener(object : PdfDocument.Callback {
            override fun onComplete(file: File) {
                Log.i(
                    PdfDocument.TAG_PDF_MY_XML,
                    "Complete " + file.absolutePath
                )
                val uri = FileProvider.getUriForFile(
                    activity!!,
                    BuildConfig.APPLICATION_ID.toString() + ".provider",
                    file
                )
                try {
                    /* val intentUrl = Intent(Intent.ACTION_VIEW)
                     intentUrl.setDataAndType(uri, "application/pdf")
                     intentUrl.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                     intentUrl.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                     intentUrl.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                     startActivity(intentUrl)
 */
                    val share = Intent()
                    share.action = Intent.ACTION_SEND
                    share.type = "application/pdf"
                    share.putExtra(
                        Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                            requireActivity()!!,
                            BuildConfig.APPLICATION_ID.toString() + ".provider",
                            file
                        )
                    )
                    startActivity(Intent.createChooser(share, "Share"))


                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(activity, "No PDF Viewer Installed", Toast.LENGTH_LONG)
                        .show()
                }
                // (activity as MainActivity?).getFragmentStack().pop()
            }

            override fun onError(e: Exception) {
                Log.i(PdfDocument.TAG_PDF_MY_XML, "Error")
            }
        })

        doc.createPdf(activity)
    }


}
