package inutrical.com.inutrical.forgotpassword


import com.google.gson.annotations.SerializedName

data class CodeModel(
    @SerializedName("ErrorCode")
    var errorCode: Int = 0
)