package inutrical.com.inutrical.forgotpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import inutrical.com.inutrical.api.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendCodeViewModel : ViewModel() {

    var result = MutableLiveData<CodeModel>()
    var verifyResult = MutableLiveData<CodeModel>()

    fun getCode(obj: JsonObject) {

        Retrofit.Api.getCode(obj).enqueue(object : Callback<CodeModel>{
            override fun onFailure(call: Call<CodeModel>, t: Throwable) {
                result.value = CodeModel().apply { errorCode=555 }
            }

            override fun onResponse(call: Call<CodeModel>, response: Response<CodeModel>) {
                result.value = response.body()
            }
        })

    }

    fun verifyCode(obj: JsonObject) {

        Retrofit.Api.verifyCode(obj).enqueue(object : Callback<CodeModel>{
            override fun onFailure(call: Call<CodeModel>, t: Throwable) {
                verifyResult.value = CodeModel().apply { errorCode=555 }

            }

            override fun onResponse(call: Call<CodeModel>, response: Response<CodeModel>) {
                verifyResult.value = response.body()
            }
        })

    }
}