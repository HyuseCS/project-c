package com.hyuse.projectc.ui.reminders

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.hyuse.projectc.domain.model.ReminderImportance
import com.hyuse.projectc.ui.components.AddressSearchDialog
import com.hyuse.projectc.ui.profile.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.maplibre.compose.camera.*
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreen(
    onNavigateBack: () -> Unit,
    viewModel: RemindersViewModel = koinViewModel(),
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val primaryColor = MaterialTheme.colorScheme.primary
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // Default fallback position (e.g., Kuala Lumpur)
    val initialPos = Position(longitude = 101.6869, latitude = 3.1390)
    val cameraState = rememberCameraState(
        firstPosition = CameraPosition(target = initialPos, zoom = 14.0)
    )

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf(250f) }
    var importance by remember { mutableStateOf(ReminderImportance.MEDIUM) }
    
    var showAddressSearch by remember { mutableStateOf(false) }
    var selectedAddress by remember { mutableStateOf("") }

    val hasLocationPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Logic to set initial location based on GPS
    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {
                @SuppressLint("MissingPermission")
                val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) 
                    ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                
                lastLocation?.let {
                    cameraState.animateTo(
                        CameraPosition(
                            target = Position(longitude = it.longitude, latitude = it.latitude),
                            zoom = 15.0
                        )
                    )
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is RemindersUiState.Success) {
            viewModel.resetState()
            onNavigateBack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Z=0 Base Layer: MapLibre (OLED Look via OpenFreeMap)
        MaplibreMap(
            modifier = Modifier.fillMaxSize(),
            cameraState = cameraState,
            baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/dark")
        )

        // Fixed center pin (Classic Location Pin)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "📍",
                fontSize = 40.sp,
                modifier = Modifier.offset(y = (-20).dp)
            )
        }

        // Top Floating Solid Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Text("←", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
                
                // Search Trigger Bar
                Surface(
                    onClick = { showAddressSearch = true },
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🔍", fontSize = 16.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (selectedAddress.isEmpty()) "Search location..." else selectedAddress,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (selectedAddress.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Persistent Bottom Panel
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Title Field
                SolidInputField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "What to do?",
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                // Description Field
                SolidInputField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "Details...",
                    modifier = Modifier.heightIn(min = 60.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Radius", 
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "${radius.toInt()}m", 
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = primaryColor
                    )
                }
                Slider(
                    value = radius,
                    onValueChange = { radius = it },
                    valueRange = 100f..1000f,
                    steps = 9,
                    colors = SliderDefaults.colors(
                        thumbColor = primaryColor,
                        activeTrackColor = primaryColor,
                        inactiveTrackColor = primaryColor.copy(alpha = 0.2f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState is RemindersUiState.Error) {
                    Text(
                        text = (uiState as RemindersUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        val currentTarget = cameraState.position.target
                        viewModel.saveReminder(
                            title = title.ifBlank { "Untitled Reminder" },
                            description = description,
                            dateMillis = System.currentTimeMillis(),
                            timeMillis = 0L,
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
                        containerColor = primaryColor
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = uiState !is RemindersUiState.Saving
                ) {
                    Text(
                        text = "Set Reminder", 
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, 
                        color = Color.White
                    )
                }
            }
        }
    }

    if (showAddressSearch) {
        AddressSearchDialog(
            viewModel = profileViewModel,
            onAddressSelected = { feature ->
                selectedAddress = feature.properties.getFormattedAddress()
                coroutineScope.launch {
                    cameraState.animateTo(
                        CameraPosition(
                            target = Position(longitude = feature.geometry.longitude, latitude = feature.geometry.latitude),
                            zoom = 16.0
                        )
                    )
                }
                showAddressSearch = false
            },
            onDismiss = { showAddressSearch = false }
        )
    }
}

@Composable
fun SolidInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = singleLine
        )
    }
}
