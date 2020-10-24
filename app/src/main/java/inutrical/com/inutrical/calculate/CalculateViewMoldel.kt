package inutrical.com.inutrical.calculate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CalculateViewMoldel : ViewModel() {

    var bmiResult = MutableLiveData<BmiModel>()
    var catResult = MutableLiveData<Lookup>()
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
        inutrical.com.inutrical.api.Retrofit.Api.getCats(jsonObject).enqueue(object :Callback<Lookup>{
            override fun onFailure(call: Call<Lookup>, t: Throwable) {
                catResult.value = null
            }

            override fun onResponse(call: Call<Lookup>, response: Response<Lookup>) {
                catResult.value = response.body()
            }
        })
    }


    fun calculateResult(jsonObject: JsonObject){
        inutrical.com.inutrical.api.Retrofit.Api.calculateResult(jsonObject).enqueue(object :Callback<CalculateResultModel>{
            override fun onFailure(call: Call<CalculateResultModel>, t: Throwable) {
                calculateResult.value = CalculateResultModel().apply { errorCode=555 }
            }

            override fun onResponse(call: Call<CalculateResultModel>, response: Response<CalculateResultModel>) {
                calculateResult.value = response.body()
            }
        })
    }



}