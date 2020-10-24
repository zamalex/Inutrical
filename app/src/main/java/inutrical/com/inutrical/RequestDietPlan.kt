package inutrical.com.inutrical


import com.google.gson.annotations.SerializedName

data class RequestDietPlan(
    @SerializedName("EntryId")
    var entryId: Int = 0,
    @SerializedName("Password")
    var password: String = "",
    @SerializedName("UserName")
    var userName: String = ""
)