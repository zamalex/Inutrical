package inutrical.com.inutrical.login


import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("Data")
    var `data`: Data = Data(),
    @SerializedName("ErrorCode")
    var errorCode: Int = 0
) {
    data class Data(
        @SerializedName("FullName")
        var fullName: String = "",
        @SerializedName("Mail")
        var mail:  String = "",
        @SerializedName("UserId")
        var userId: String = ""
    )
}