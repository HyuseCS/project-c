package com.hyuse.projectc.ui.expenses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagerScreen(
    state: ExpensesState,
    onAddCustomCategory: (String, String?) -> Unit,
    onBack: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }

    val categories = if (state is ExpensesState.Success) state.categories else emptyList()
    
    // Separate system and custom
    val systemCategories = categories.filter { it.isSystem }
    val customCategories = categories.filter { !it.isSystem }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Categories", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("➕")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text("Custom Categories", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
            }
            if (customCategories.isEmpty()) {
                item {
                    Text("No custom categories yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                items(customCategories) { cat ->
                    ListItem(headlineContent = { Text(cat.name) })
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text("System Categories", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
            }
            items(systemCategories) { cat ->
                ListItem(headlineContent = { Text(cat.name) })
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add Custom Category") },
                text = {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Category Name") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (newCategoryName.isNotBlank()) {
                            onAddCustomCategory(newCategoryName, null)
                            newCategoryName = ""
                            showDialog = false
                        }
                    }) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
