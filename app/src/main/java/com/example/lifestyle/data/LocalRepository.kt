package com.example.lifestyle.data

import android.content.Context
import android.content.SharedPreferences
import com.example.lifestyle.model.ChecklistItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LocalRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("daily_dashboard_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_NOTES = "notes"
        private const val KEY_CHECKLIST = "checklist"
        private const val KEY_WATER_COUNT = "water_count"
        private const val KEY_LAST_DATE = "last_date"
    }

    fun saveNotes(notes: String) {
        prefs.edit().putString(KEY_NOTES, notes).apply()
    }

    fun getNotes(): String = prefs.getString(KEY_NOTES, "") ?: ""

    fun saveChecklist(items: List<ChecklistItem>) {
        val json = gson.toJson(items)
        prefs.edit().putString(KEY_CHECKLIST, json).apply()
    }

    fun getChecklist(): List<ChecklistItem> {
        val json = prefs.getString(KEY_CHECKLIST, null) ?: return emptyList()
        val type = object : TypeToken<List<ChecklistItem>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getWaterCount(): Int {
        checkAndResetDaily()
        return prefs.getInt(KEY_WATER_COUNT, 0)
    }

    fun setWaterCount(count: Int) {
        prefs.edit().putInt(KEY_WATER_COUNT, count).apply()
    }

    private fun checkAndResetDaily() {
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val lastDate = prefs.getString(KEY_LAST_DATE, "")
        if (today != lastDate) {
            prefs.edit()
                .putInt(KEY_WATER_COUNT, 0)
                .putString(KEY_LAST_DATE, today)
                .apply()
        }
    }
}
