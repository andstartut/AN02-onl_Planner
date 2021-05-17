package io.techmeskills.an02onl_plannerapp.cloud

import com.google.gson.annotations.SerializedName

class CloudNote(
    @SerializedName("id") val id: Long,
    @SerializedName("accountName") val account: String,
    @SerializedName("title") val title: String,
    @SerializedName("date") val date: Long,
    @SerializedName("setEvent") val setEvent: Boolean
)