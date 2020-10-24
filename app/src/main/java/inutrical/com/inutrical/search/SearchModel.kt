package inutrical.com.inutrical.search


import com.google.gson.annotations.SerializedName

data class SearchModel(
    @SerializedName("Data")
    var `data`: Data = Data(),
    @SerializedName("ErrorCode")
    var errorCode: Int = 0
) {
    data class Data(
        @SerializedName("AdmissionDate")
        var admissionDate: String = "",
        @SerializedName("DateOfBirth")
        var dateOfBirth: String = "",
        @SerializedName("Gender")
        var gender: String = "",
        @SerializedName("Height")
        var height: Double = 0.0,
        @SerializedName("HospitalId")
        var hospitalId: Any = Any(),
        @SerializedName("Id")
        var id: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Number")
        var number: String = "",
        @SerializedName("PhysicianName")
        var physicianName: String = "",
        @SerializedName("Weight")
        var weight: Double = 0.0
    )
}