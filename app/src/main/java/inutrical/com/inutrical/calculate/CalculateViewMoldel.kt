package inutrical.com.inutrical.calculate

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Part
import retrofit2.http.PartMap

class CalculateViewMoldel : ViewModel() {

    var bmiResult = MutableLiveData<BmiModel>()
    var catResult = MutableLiveData<Lookup>()
    var diseasesResult = MutableLiveData<DiseasesModel>()
    var updatePatientResult = MutableLiveData<ResponseBody>()
    var createDietPlanResult = MutableLiveData<ApplyResponse>()
    var calculateResult = MutableLiveData<CalculateResultModel>().apply { value=null }


    fun calculateBmi(obj:JsonObject){

        inutrical.com.inutrical.api.Retrofit.Api.calculateBmi(obj).enqueue(object :Callback<BmiModel>{
            override fun onFailure(call: Call<BmiModel>, t: Throwable) {

                bmiResult.value = BmiModel().apply { errorCode=555 }
            }

            override fun onResponse(call: Call<BmiModel>, response: Response<BmiModel>) {

                bmiResult.value = response.body()
            }
        })
    }




    fun getCats(jsonObject: JsonObject){
        inutrical.com.inutrical.api.Retrofit.Api.getCats().enqueue(object :Callback<Lookup>{
            override fun onFailure(call: Call<Lookup>, t: Throwable) {
                catResult.value = null
            }

            override fun onResponse(call: Call<Lookup>, response: Response<Lookup>) {
                catResult.value = response.body()
            }
        })
    }


    fun updatePatient(jsonObject: JsonObject){
        inutrical.com.inutrical.api.Retrofit.Api.updatePatient(jsonObject).enqueue(object :Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                updatePatientResult.value = null
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                updatePatientResult.value = response.body()
            }
        })
    }
    fun createDietPlan(labresults: MultipartBody.Part?,medicalhistory: MultipartBody.Part?,partMap: Map<String, @JvmSuppressWildcards RequestBody>){
        inutrical.com.inutrical.api.Retrofit.Api.createDietPlan(labresults = labresults,medicalhistory = medicalhistory,partMap = partMap).enqueue(object :Callback<ApplyResponse>{
            override fun onFailure(call: Call<ApplyResponse>, t: Throwable) {
                createDietPlanResult.value = null

                Log.e("error",t.localizedMessage)
            }

            override fun onResponse(call: Call<ApplyResponse>, response: Response<ApplyResponse>) {

                if (response.isSuccessful)
                createDietPlanResult.value = response.body()
                else
                createDietPlanResult.value = null
            }
        })
    }

    fun getDiseases(){
        inutrical.com.inutrical.api.Retrofit.Api.getDiseases().enqueue(object :Callback<DiseasesModel>{
            override fun onFailure(call: Call<DiseasesModel>, t: Throwable) {
                diseasesResult.value = null
            }

            override fun onResponse(call: Call<DiseasesModel>, response: Response<DiseasesModel>) {
                diseasesResult.value = response.body()
            }
        })
    }


    fun calculateResult(jsonObject: JsonObject){
        inutrical.com.inutrical.api.Retrofit.Api.calculateResult(jsonObject).enqueue(object :Callback<CalculateResultModel>{
            override fun onFailure(call: Call<CalculateResultModel>, t: Throwable) {
                calculateResult.value = CalculateResultModel().apply { errorCode=555 }
                Log.e("error",t.localizedMessage)

            }

            override fun onResponse(call: Call<CalculateResultModel>, response: Response<CalculateResultModel>) {
                calculateResult.value = response.body()
            }
        })
    }



}