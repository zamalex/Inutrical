package inutrical.com.inutrical.calculate


import com.google.gson.annotations.SerializedName

data class Lookup(
    @SerializedName("data")
    var data: Data = Data()

    ){

    data class Data(
        @SerializedName("Categories")
        var categories: List<Category> = listOf(),
        @SerializedName("Formulas")
        var formulas: List<Formula> = listOf()
    ) {
        data class Category(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("name")
            var name: String = ""
        ){
            override fun toString(): String {
                return name
            }
        }

        data class Formula(


            @SerializedName("category_id")
            var categoryId: Int = 0,
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("name")
            var name: String = ""

        ){
            override fun toString(): String {
                return name
            }
        }
    }
}
