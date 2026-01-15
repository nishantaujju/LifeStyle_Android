package com.example.lifestyle.model

import java.util.UUID

data class ChecklistItem(
    val id: String = UUID.randomUUID().toString(),
    val task: String,
    val isCompleted: Boolean = false
)
