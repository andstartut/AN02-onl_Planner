package io.techmeskills.an02onl_plannerapp.database.repository

import io.techmeskills.an02onl_plannerapp.cloud.CloudAccount
import io.techmeskills.an02onl_plannerapp.cloud.CloudNote
import io.techmeskills.an02onl_plannerapp.cloud.ExportNotesRequestBody
import io.techmeskills.an02onl_plannerapp.cloud.IRetrofitSettings
import io.techmeskills.an02onl_plannerapp.database.model.Note
import kotlinx.coroutines.flow.first

class CloudRepository(
    private val accountRepository: AccountRepository,
    private val noteRepository: NoteRepository,
    private val retrofitSettings: IRetrofitSettings
) {
    suspend fun importNotes(): Boolean {
        val account = accountRepository.getCurrentAccountFlow().first()
        val response = retrofitSettings.importNotes(account.name, accountRepository.phoneId)
        val cloudNotes = response.body() ?: emptyList()
        val notes = cloudNotes.map { cloudNote ->
            Note(
                accountId = account.id,
                title = cloudNote.title,
                date = cloudNote.date,
                cloudSync = true
            )
        }
        noteRepository.saveNotes(notes)
        return response.isSuccessful
    }

    suspend fun exportNotes(): Boolean {
        val account = accountRepository.getCurrentAccountFlow().first()
        val notes = noteRepository.currentAccountNotesFlow
        val exportRequestBody =
            ExportNotesRequestBody(
                CloudAccount(account.id, account.name),
                accountRepository.phoneId,
                notes.first().map {
                    CloudNote(it.id, it.title, it.date)
                })
        val exportResult = retrofitSettings.exportNotes(exportRequestBody).isSuccessful
        if (exportResult) {
            noteRepository.setAllNotesSyncWithCloud()
        }
        return exportResult
    }
}