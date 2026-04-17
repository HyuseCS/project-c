package com.hyuse.projectc.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hyuse.projectc.domain.model.AppConstants
import com.hyuse.projectc.domain.model.User

/**
 * Screen for creating or editing a user profile.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    profileState: ProfileState,
    onSave: (name: String, nickname: String, university: String, course: String, currencySymbol: String) -> Unit,
    onBack: () -> Unit,
    onClearError: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    var name by rememberStringState("")
    var nickname by rememberStringState("")
    var university by rememberStringState("")
    var course by rememberStringState("")
    var currencySymbol by rememberStringState("₱")

    // Dropdown state
    var currencyExpanded by remember { mutableStateOf(false) }

    // Update fields when profile is loaded
    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            name = profileState.profile.name
            nickname = profileState.profile.nickname ?: ""
            university = profileState.profile.university
            course = profileState.profile.course
            currencySymbol = profileState.profile.currencySymbol
        } else if (profileState is ProfileState.SaveSuccess) {
            onSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Profile", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Complete your profile to get started with Project C.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = user.email,
                    onValueChange = {},
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    readOnly = true
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter your full name") }
                )

                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nickname (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("How should we call you?") }
                )

                OutlinedTextField(
                    value = university,
                    onValueChange = { university = it },
                    label = { Text("University") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Which university do you attend?") }
                )

                OutlinedTextField(
                    value = course,
                    onValueChange = { course = it },
                    label = { Text("Course") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("What are you studying?") }
                )

                // Currency Selection
                ExposedDropdownMenuBox(
                    expanded = currencyExpanded,
                    onExpandedChange = { currencyExpanded = !currencyExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val currentCurrencyName = AppConstants.currencies.find { it.second == currencySymbol }?.first ?: currencySymbol
                    OutlinedTextField(
                        value = currentCurrencyName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Preferred Currency") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = currencyExpanded,
                        onDismissRequest = { currencyExpanded = false }
                    ) {
                        AppConstants.currencies.forEach { (name, symbol) ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    currencySymbol = symbol
                                    currencyExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSave(name, nickname, university, course, currencySymbol) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    enabled = profileState !is ProfileState.Loading
                ) {
                    if (profileState is ProfileState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Save Profile")
                    }
                }

                if (profileState is ProfileState.Error) {
                    AlertDialog(
                        onDismissRequest = onClearError,
                        title = { Text("Error") },
                        text = { Text(profileState.message) },
                        confirmButton = {
                            TextButton(onClick = onClearError) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }
}

// Helper to use remember with mutableStateOf and initial value easily
@Composable
private fun rememberStringState(initialValue: String) = remember { mutableStateOf(initialValue) }
