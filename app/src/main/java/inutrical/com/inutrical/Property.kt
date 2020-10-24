package inutrical.com.inutrical

import com.google.gson.annotations.SerializedName

data class Property(
    var data: Data = Data(),
    var msg: String = "",
    @SerializedName("status_code")
    var statusCode: Int = 0
) {
    data class Data(
        var result: List<Result> = listOf()
    ) {
        data class Result(
            @SerializedName("building_area")
            var buildingArea: String = "",
            @SerializedName("created_at")
            var createdAt: String = "",
            var description: String = "",
            @SerializedName("description_ar")
            var descriptionAr: String = "",
            var id: String = "",
            var image: String = "",
            var location: String = "",
            var name: String = "",
            @SerializedName("owner_id")
            var ownerId: String = "",
            var price: String = "",
            var status: String = "",
            var type: String = "",
            @SerializedName("updated_at")
            var updatedAt: String = ""
        )
    }
}