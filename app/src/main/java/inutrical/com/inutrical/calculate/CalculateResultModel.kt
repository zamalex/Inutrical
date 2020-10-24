package inutrical.com.inutrical.calculate


import com.google.gson.annotations.SerializedName

data class CalculateResultModel(
    @SerializedName("Data")
    var `data`: Data = Data(),
    @SerializedName("ErrorCode")
    var errorCode: Int = 100
) {
    data class Data(
        @SerializedName("Carb")
        var carb: Double = 0.0,
        @SerializedName("FAT")
        var fAT: Double = 0.0,
        @SerializedName("K")
        var k: Double = 0.0,
        @SerializedName("NA")
        var nA: Double = 0.0,
        @SerializedName("NumberOFCans")
        var numberOFCans: Double = 0.0,
        @SerializedName("Protein")
        var protein: Double = 0.0,
        @SerializedName("TotalCalories")
        var totalCalories: Double = 0.0,
        @SerializedName("TotalVolume")
        var totalVolume: Double = 0.0,
        @SerializedName("Water")
        var water: Double = 0.0
    )
}