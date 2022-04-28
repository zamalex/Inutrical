package inutrical.com.inutrical.calculate


import com.google.gson.annotations.SerializedName

data class BmiModel(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("status_code")
    var errorCode: Int = 0
) {
    data class Data(
        @SerializedName("ABW")
        var aBW: Double = 0.0,
        @SerializedName("BMI")
        var bMI: Double = 0.0,
        @SerializedName("BMIText")
        var bMIText: String = "",
        @SerializedName("IBW")
        var iBW: Double = 0.0
    )
}