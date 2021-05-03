package io.techmeskills.an02onl_plannerapp.database.repository

import io.techmeskills.an02onl_plannerapp.cloud.CloudAccount
import io.techmeskills.an02onl_plannerapp.cloud.CloudNote
import io.techmeskills.an02onl_plannerapp.cloud.ExportNotesRequestBody
import io.techmeskills.an02onl_plannerapp.cloud.IRetrofitSettings
import io.techmeskills.an02onl_plannerapp.database.model.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first

class CloudRepository(
    private val accountRepository: AccountRepository,
    private val noteRepository: NoteRepository,
    private val retrofitSettings: IRetrofitSettings
) {

    @ExperimentalCoroutinesApi
    suspend fun importNotes(): Boolean {
        val accountName = accountRepository.getCurrentAccountName()
        val response = retrofitSettings.importNotes(accountName, accountRepository.phoneId)
        val cloudNotes = response.body() ?: emptyList()
        val notes = cloudNotes.map { cloudNote ->
            Note(
                accountName = accountName,
                title = cloudNote.title,
                date = cloudNote.date,
                cloudSync = true
            )
        }
        noteRepository.saveNotes(notes)
        return response.isSuccessful
    }

    @ExperimentalCoroutinesApi
    suspend fun exportNotes(): Boolean {
        val accountName = accountRepository.getCurrentAccountName()
        val notes = noteRepository.currentAccountNotesFlow
        val exportRequestBody =
            ExportNotesRequestBody(
                CloudAccount(accountName),
                accountRepository.phoneId,
                notes.first().map {
                    CloudNote(it.accountName, it.title, it.date)
                })
        val exportResult = retrofitSettings.exportNotes(exportRequestBody).isSuccessful
        if (exportResult) {
            noteRepository.setAllNotesSyncWithCloud()
        }
        return exportResult
    }
}