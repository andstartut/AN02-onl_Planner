package io.techmeskills.an02onl_plannerapp.cloud

import com.google.gson.annotations.SerializedName

class CloudNote(
    @SerializedName("title") val title: String,
    @SerializedName("date") val date: Long,
    @SerializedName("alarmEnabled") val setEvent: Boolean,
    @SerializedName("noteColor") val color: String,
    @SerializedName("notePinned") val pinned: Boolean
)