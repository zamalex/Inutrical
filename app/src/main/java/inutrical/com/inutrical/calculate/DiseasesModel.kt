package inutrical.com.inutrical.calculate

import com.google.gson.annotations.SerializedName

data class DiseasesModel(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("disease_name")
	val diseaseName: String? = null


) {
	override fun toString(): String {
		return diseaseName!!
	}
}
