package inutrical.com.inutrical.search


import com.google.gson.annotations.SerializedName

data class HistoryModel(
    @SerializedName("Data")
    var `data`: List<Data> = listOf(),
    @SerializedName("ErrorCode")
    var errorCode: Int = 0
) {
    data class Data(
        var Id: Int = 0,

        var followUpDate: String = "",
        @SerializedName("LabResults")
        var labResults: LabResults = LabResults(),
        @SerializedName("MedicalHistory")
        var medicalHistory: MedicalHistory = MedicalHistory(),
        @SerializedName("PatientName")
        var patientName: String = "",
        @SerializedName("PatientNumber")
        var patientNumber: String = "",
        @SerializedName("PhysicianName")
        var physicianName: String = "",
        @SerializedName("Doctor")
        var clinicalDietation: String? = ""
    ) {
        data class LabResults(
            @SerializedName("FileName")
            var fileName: String = "",
            @SerializedName("URL")
            var uRL: String = ""
        )

        data class MedicalHistory(
            @SerializedName("FileName")
            var fileName: String = "",
            @SerializedName("URL")
            var uRL: String = ""
        )
    }
}