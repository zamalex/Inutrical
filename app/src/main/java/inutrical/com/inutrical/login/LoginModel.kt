package inutrical.com.inutrical.login

data class LoginModel(
	var status_code: Int? = null,
	val data: List<DataItem?>? = null,
	val message: String? = null
)

data class DataItem(
	val name: String? = null,
	val id: Int? = null,
	val token: String? = null
)

