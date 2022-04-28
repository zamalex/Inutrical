package inutrical.com.inutrical.search


import com.google.gson.annotations.SerializedName

data class HistoryModel(
    @SerializedName("data")
    var `data`: List<Data> = listOf(),
    @SerializedName("status_code")
    var errorCode: Int = 0
) {
    data class Data(
        var id: Int = 0,

        @SerializedName("followup_date")
        var followUpDate: String = "",
        @SerializedName("labresults")
        var labResults: String = "",
        @SerializedName("medicalhistory")
        var medicalHistory: String = "",
        @SerializedName("patient_name")
        var patientName: String = "",
        @SerializedName("patient_number")
        var patientNumber: String = "",
        @SerializedName("physican_name")
        var physicianName: String = "",
        @SerializedName("doctor_name")
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