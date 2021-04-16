package io.techmeskills.an02onl_plannerapp.screen.main

import io.techmeskills.an02onl_plannerapp.data.db.NotesDatabase
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel

class TaskModel(val database: NotesDatabase) : CoroutineViewModel() {

    val tasks = listOf(
        Task("Помыть кошку", "25.04.21"),
        Task("Купить хлеб"),
        Task("Заплатить за обучение", "23.03.21"),
        Task("Сделать домашку", "25.03.21"),
        Task("Купить билет на Марс"),
        Task("val - value\nvar - variable", "26.04.21"),
        Task("Уборка!", "16.05.21"),
        Task("Почистить зубы")
    )
}

class Task(val task: String, val date: String? = null)