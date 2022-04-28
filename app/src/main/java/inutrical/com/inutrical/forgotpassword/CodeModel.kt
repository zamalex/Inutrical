package inutrical.com.inutrical.forgotpassword


import com.google.gson.annotations.SerializedName

data class CodeModel(
    @SerializedName("status_code")
    var errorCode: Int = 0
)