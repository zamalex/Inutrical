package inutrical.com.inutrical.resetpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import inutrical.com.inutrical.api.Retrofit
import inutrical.com.inutrical.forgotpassword.CodeModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetViewModel : ViewModel() {

    var result = MutableLiveData<CodeModel>()

    fun resetPass(obj: JsonObject) {

        Retrofit.Api.resetPassword(obj).enqueue(object : Callback<CodeModel> {
            override fun onFailure(call: Call<CodeModel>, t: Throwable) {
                result.value = CodeModel().apply { errorCode=555 }
            }

            override fun onResponse(call: Call<CodeModel>, response: Response<CodeModel>) {
                result.value = response.body()
            }
        })

    }
}