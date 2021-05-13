package io.techmeskills.an02onl_plannerapp.cloud

import com.google.gson.annotations.SerializedName

class CloudNote(
    @SerializedName("accountName") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("date") val date: Long?,
    @SerializedName("setEvent") val setEvent: Boolean
)