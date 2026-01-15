package com.example.lifestyle.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.lifestyle.data.LocalRepository
import com.example.lifestyle.model.ChecklistItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LocalRepository(application)

    private val _notes = MutableStateFlow(repository.getNotes())
    val notes = _notes.asStateFlow()

    private val _checklist = MutableStateFlow(repository.getChecklist())
    val checklist = _checklist.asStateFlow()

    private val _waterCount = MutableStateFlow(repository.getWaterCount())
    val waterCount = _waterCount.asStateFlow()

    fun updateNotes(newNotes: String) {
        _notes.value = newNotes
        repository.saveNotes(newNotes)
    }

    fun addChecklistItem(task: String) {
        val newList = _checklist.value + ChecklistItem(task = task)
        updateChecklist(newList)
    }

    fun toggleChecklistItem(id: String) {
        val newList = _checklist.value.map {
            if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
        }
        updateChecklist(newList)
    }

    fun deleteChecklistItem(id: String) {
        val newList = _checklist.value.filter { it.id != id }
        updateChecklist(newList)
    }

    private fun updateChecklist(list: List<ChecklistItem>) {
        _checklist.value = list
        repository.saveChecklist(list)
    }

    fun incrementWater() {
        _waterCount.value += 1
        repository.setWaterCount(_waterCount.value)
    }

    fun decrementWater() {
        if (_waterCount.value > 0) {
            _waterCount.value -= 1
            repository.setWaterCount(_waterCount.value)
        }
    }

    fun getCurrentDate(): String {
        return SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(Date())
    }

    fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }
}
