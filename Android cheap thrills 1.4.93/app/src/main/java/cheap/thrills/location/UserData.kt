package cheap.thrills.location

import com.google.gson.annotations.SerializedName


data class UserData(
    @SerializedName("Coordinate") val location: Coordinate,
    @SerializedName("userId") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("orderId") val orderId: String,
    @SerializedName("guest") val guest: String,
    @SerializedName("Gid") val gid: String,
) {

    data class Coordinate(
        @SerializedName("Latitude") val lat: Double,
        @SerializedName("Longitude") val lng: Double,
    ) {

    }
}