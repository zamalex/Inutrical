package inutrical.com.inutrical.search

import com.google.gson.annotations.SerializedName

data class SearchModel(

	@field:SerializedName("status_code")
	var statusCode: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("birthdate")
	val birthdate: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("nationality")
	val nationality: String? = null,

	@field:SerializedName("patient_weight")
	val patientWeight: Double? = null,

	@field:SerializedName("patient_name")
	val patientName: String? = null,

	@field:SerializedName("doctor_name")
	val doctorName: String? = null,

	@field:SerializedName("hospital_id")
	val hospitalId: Int? = null,

	@field:SerializedName("patient_height")
	val patientHeight: Double? = null,

	@field:SerializedName("patient_number")
	val patientNumber: String? = null,

	@field:SerializedName("entry_date")
	val entryDate: Any? = null
)
