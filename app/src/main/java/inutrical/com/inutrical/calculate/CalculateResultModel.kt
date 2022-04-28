package inutrical.com.inutrical.calculate


import com.google.gson.annotations.SerializedName

data class CalculateResultModel(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("status_code")
    var errorCode: Int = 0
) {
    data class Data(
        @SerializedName("Carb")
        var carb:String = "0.0",
        @SerializedName("Fat")
        var fAT:String = "0.0",
        @SerializedName("K")
        var k:String = "0.0",
        @SerializedName("Na")
        var nA:String = "0.0",
        @SerializedName("NumberOfCans")
        var numberOFCans:String = "0.0",
        @SerializedName("Protien")
        var protein:String = "0.0",
        @SerializedName("TotalCalories")
        var totalCalories:String = "0.0",
        @SerializedName("TotalVolume")
        var totalVolume:String = "0.0",
        @SerializedName("water")
        var water: String = "0.0"
    )
}