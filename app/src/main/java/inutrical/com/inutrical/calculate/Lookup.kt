package inutrical.com.inutrical.calculate


import com.google.gson.annotations.SerializedName

data class Lookup(
    @SerializedName("Categories")
    var categories: List<Category> = listOf(),
    @SerializedName("Formulas")
    var formulas: List<Formula> = listOf()
) {
    data class Category(
        @SerializedName("Id")
        var id: Int = 0,
        @SerializedName("Name")
        var name: String = ""
    ){
        override fun toString(): String {
            return name
        }
    }

    data class Formula(
        @SerializedName("CHO")
        var cHO: Double = 0.0,
        @SerializedName("Category")
        var category: String = "",
        @SerializedName("CategoryId")
        var categoryId: Int = 0,
        @SerializedName("Company")
        var company: Any = Any(),
        @SerializedName("FAT")
        var fAT: Double = 0.0,
        @SerializedName("Fiber")
        var fiber: Double = 0.0,
        @SerializedName("Fluid")
        var fluid: Double = 0.0,
        @SerializedName("Id")
        var id: Int = 0,
        @SerializedName("K")
        var k: Double = 0.0,
        @SerializedName("NA")
        var nA: Double = 0.0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("PRO")
        var pRO: Double = 0.0,
        @SerializedName("ServingSize")
        var servingSize: Double = 0.0,
        @SerializedName("TotalCaloriesPerCan")
        var totalCaloriesPerCan: Double = 0.0
    ){
        override fun toString(): String {
            return name
        }
    }
}