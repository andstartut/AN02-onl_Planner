package io.techmeskills.an02onl_plannerapp.cloud

import com.google.gson.annotations.SerializedName

class CloudAccount(
    @SerializedName("id") val userId: Long,
    @SerializedName("name") val userName: String
)