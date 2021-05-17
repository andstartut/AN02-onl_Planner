package io.techmeskills.an02onl_plannerapp.repository

import android.content.Context
import android.widget.Toast
import io.techmeskills.an02onl_plannerapp.cloud.CloudAccount
import io.techmeskills.an02onl_plannerapp.cloud.CloudNote
import io.techmeskills.an02onl_plannerapp.cloud.ExportNotesRequestBody
import io.techmeskills.an02onl_plannerapp.cloud.IRetrofitSettings
import io.techmeskills.an02onl_plannerapp.database.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.coroutineContext

class CloudRepository(
    private val accountRepository: AccountRepository,
    private val noteRepository: NoteRepository,
    private val retrofitSettings: IRetrofitSettings,
    private val context: Context
) {

    @ExperimentalCoroutinesApi
    suspend fun importNotes(): Boolean {
        try {
            val accountName = accountRepository.getCurrentAccountName()
            val response = retrofitSettings.importNotes(accountName, accountRepository.phoneId)
            val cloudNotes = response.body() ?: emptyList()
            val notes = cloudNotes.map { cloudNote ->
                Note(
                    id = cloudNote.id,
                    accountName = accountName,
                    title = cloudNote.title,
                    date = cloudNote.date,
                    setEvent = cloudNote.setEvent,
                    cloudSync = true
                )
            }
            noteRepository.saveNotes(notes)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Notes imported successfully", Toast.LENGTH_LONG).show()
            }
            return response.isSuccessful
        } catch (e: IOException) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.printStackTrace().toString(), Toast.LENGTH_LONG).show()
            }
            return false
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun exportNotes(): Boolean {
        try {
            val accountName = accountRepository.getCurrentAccountName()
            val notes = noteRepository.currentAccountNotesFlow
            val exportRequestBody =
                ExportNotesRequestBody(
                    CloudAccount(accountName),
                    accountRepository.phoneId,
                    notes.first().map {
                        CloudNote(it.id, it.accountName, it.title, it.date, it.setEvent)
                    })
            val exportResult = retrofitSettings.exportNotes(exportRequestBody).isSuccessful
            if (exportResult) {
                noteRepository.setAllNotesSyncWithCloud()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Notes exported successfully", Toast.LENGTH_LONG).show()
                }
            }
            return exportResult
        } catch (e: IOException) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.printStackTrace().toString(), Toast.LENGTH_LONG).show()
            }
            return false
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun eraseNotes(): Boolean {
        val accountName = accountRepository.getCurrentAccountName()
        val exportRequestBody =
            ExportNotesRequestBody(
                CloudAccount(accountName),
                accountRepository.phoneId,
                emptyList()
            )
        val exportResult = retrofitSettings.exportNotes(exportRequestBody).isSuccessful
        if (exportResult) {
            noteRepository.setAllNotesSyncWithCloud()
        }
        return exportResult
    }
}