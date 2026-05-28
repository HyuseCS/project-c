package com.hyuse.projectc.ui.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.hyuse.projectc.domain.model.ReminderImportance
import com.hyuse.projectc.ui.reminders.components.LucidGlassSurface
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreen(
    onNavigateBack: () -> Unit,
    viewModel: RemindersViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Initial map position (fallback to somewhere, maybe current location in real app)
    val initialPos = LatLng(37.7749, -122.4194)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPos, 15f)
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf(250f) }
    var importance by remember { mutableStateOf(ReminderImportance.MEDIUM) }

    LaunchedEffect(uiState) {
        if (uiState is RemindersUiState.Success) {
            viewModel.resetState()
            onNavigateBack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Z=0 Base Layer: Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = true)
        ) {
            // Draw a circle to represent the geofence radius
            val currentTarget = cameraPositionState.position.target
            Circle(
                center = currentTarget,
                radius = radius.toDouble(),
                fillColor = Color(0xFF6200EE).copy(alpha = 0.2f),
                strokeColor = Color(0xFF6200EE),
                strokeWidth = 2f
            )
        }

        // Fixed center pin (drawn over map to represent the exact target)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                // In a real app, use a custom vector pin. Using standard icon here.
                imageVector = Icons.Default.Add, 
                contentDescription = "Map Center Pin",
                tint = Color(0xFF6200EE),
                modifier = Modifier.size(48.dp)
            )
        }

        // Top Floating Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LucidGlassSurface(cornerRadius = 24f) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
            }
            // A search pill would go here
        }

        // Persistent Bottom Panel (Tactile Glass)
        LucidGlassSurface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            cornerRadius = 32f
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "New Reminder",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("What to do?") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    singleLine = true
                )
                Divider(color = Color.LightGray)

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Details...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                Divider(color = Color.LightGray)

                Spacer(modifier = Modifier.height(16.dp))
                
                Text(text = "Radius: ${radius.toInt()}m", fontWeight = FontWeight.Bold)
                Slider(
                    value = radius,
                    onValueChange = { radius = it },
                    valueRange = 100f..1000f,
                    steps = 9,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF6200EE),
                        activeTrackColor = Color(0xFF6200EE)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Monetization limit inline warning
                if (uiState is RemindersUiState.Error) {
                    Text(
                        text = (uiState as RemindersUiState.Error).message,
                        color = Color.Red,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        val currentTarget = cameraPositionState.position.target
                        viewModel.saveReminder(
                            title = title.ifBlank { "Untitled Reminder" },
                            description = description,
                            dateMillis = System.currentTimeMillis(), // Simplified
                            timeMillis = 0L, // Simplified
                            importance = importance,
                            latitude = currentTarget.latitude,
                            longitude = currentTarget.longitude,
                            radius = radius.toDouble()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE)
                    ),
                    shape = MaterialTheme.shapes.large,
                    enabled = uiState !is RemindersUiState.Saving
                ) {
                    Text("Set Reminder", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
