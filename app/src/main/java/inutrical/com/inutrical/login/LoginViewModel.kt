package inutrical.com.inutrical.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import inutrical.com.inutrical.api.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    var result: MutableLiveData<LoginModel> = MutableLiveData<LoginModel>()


    fun login(data: JsonObject) {
        Retrofit.Api.login(data).enqueue(object : Callback<LoginModel> {
            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                result.value=LoginModel().apply { status_code=555 }
            }

            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                result.value = response.body()
            }
        })
    }
}