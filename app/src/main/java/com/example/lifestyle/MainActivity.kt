package com.example.lifestyle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifestyle.ui.DashboardViewModel
import com.example.lifestyle.ui.components.*
import com.example.lifestyle.ui.theme.DailyLifeDashboardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DailyLifeDashboardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DashboardScreen()
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val notes by viewModel.notes.collectAsState()
    val checklist by viewModel.checklist.collectAsState()
    val waterCount by viewModel.waterCount.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderSection(
                greeting = viewModel.getGreeting(),
                date = viewModel.getCurrentDate()
            )
        }
        item {
            NotesSection(
                notes = notes,
                onNotesChange = { viewModel.updateNotes(it) }
            )
        }
        item {
            WaterTrackerSection(
                count = waterCount,
                onIncrement = { viewModel.incrementWater() },
                onDecrement = { viewModel.decrementWater() }
            )
        }
        item {
            ChecklistSection(
                items = checklist,
                onAdd = { viewModel.addChecklistItem(it) },
                onToggle = { viewModel.toggleChecklistItem(it) },
                onDelete = { viewModel.deleteChecklistItem(it) }
            )
        }
        item {
            ToolsSection()
        }
        item {
            FooterSection()
        }
    }
}
