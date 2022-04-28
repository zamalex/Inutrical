package inutrical.com.inutrical.api

import com.google.gson.JsonObject
import inutrical.com.inutrical.RequestDietPlan
import inutrical.com.inutrical.calculate.*
import inutrical.com.inutrical.forgotpassword.CodeModel
import inutrical.com.inutrical.login.LoginModel
import inutrical.com.inutrical.search.HistoryModel
import inutrical.com.inutrical.search.SearchModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("app/Profile/Login")
    fun login(@Body body:JsonObject): Call<LoginModel>


    @POST("app/PatientsData/CalculateBMI")
    fun calculateBmi(@Body body:JsonObject): Call<BmiModel>

    @GET("app/Formulas/Lookups")
    fun getCats(): Call<Lookup>

    @GET("app/Diseases")
    fun getDiseases(): Call<DiseasesModel>

    @POST("app/Profile/ForgetPassword")
    fun getCode(@Body body:JsonObject): Call<CodeModel>

    @POST("app/PatientsData/update")
    fun updatePatient(@Body body:JsonObject): Call<ResponseBody>

    @Multipart
    @POST("app/PatientsData/CalculateDietplan")
    fun createDietPlan(@Part labresults: MultipartBody.Part?,@Part medicalhistory: MultipartBody.Part?, @PartMap() partMap: Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<ApplyResponse>

    @POST("app/Profile/ValidateCode")
    fun verifyCode(@Body body:JsonObject): Call<CodeModel>

    @POST("app/Profile/ResetPassword")
    fun resetPassword(@Body body:JsonObject): Call<CodeModel>

    @POST("app/PatientsData/CalculateResults")
    fun calculateResult(@Body body:JsonObject): Call<CalculateResultModel>

    @POST("app/patients/Search")
    fun search(@Body body:JsonObject): Call<SearchModel>

    @POST("app/Patients/history/GetPatientHistory")
    fun getHistory(@Body body:JsonObject): Call<HistoryModel>

    @POST("ExportPlan/ExportPdf")
    fun exportPdf(@Body body:RequestDietPlan): Call<ResponseBody>
}