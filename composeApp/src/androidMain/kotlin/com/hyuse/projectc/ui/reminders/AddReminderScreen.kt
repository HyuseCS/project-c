package com.hyuse.projectc.ui.reminders

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.hyuse.projectc.domain.model.ReminderImportance
import com.hyuse.projectc.ui.theme.LucidSurface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreen(
    onNavigateBack: () -> Unit,
    viewModel: RemindersViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val primaryColor = MaterialTheme.colorScheme.primary
    val context = LocalContext.current
    
    // Default fallback position (e.g., SF)
    val defaultPos = LatLng(37.7749, -122.4194)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultPos, 15f)
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf(250f) }
    var importance by remember { mutableStateOf(ReminderImportance.MEDIUM) }

    val hasLocationPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Logic to set initial location based on Profile coordinates, address, or GPS
    LaunchedEffect(userProfile, hasLocationPermission) {
        val profile = userProfile
        var targetLatLng: LatLng? = null

        // 1. Try explicit Home Lat/Lng first (Highest Precision)
        if (profile?.homeLatitude != null && profile.homeLongitude != null) {
            targetLatLng = LatLng(profile.homeLatitude!!, profile.homeLongitude!!)
        } 
        // 2. Fallback to Geocoding Profile Address string
        else if (!profile?.address.isNullOrBlank()) {
            targetLatLng = withContext(Dispatchers.IO) {
                try {
                    val geocoder = Geocoder(context)
                    val addresses = geocoder.getFromLocationName(profile!!.address, 1)
                    if (!addresses.isNullOrEmpty()) {
                        LatLng(addresses[0].latitude, addresses[0].longitude)
                    } else null
                } catch (e: Exception) {
                    null
                }
            }
        }

        // 3. Fallback to GPS Last Location if profile data is unavailable
        if (targetLatLng == null && hasLocationPermission) {
            try {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                val location = fusedLocationClient.lastLocation.await()
                if (location != null) {
                    targetLatLng = LatLng(location.latitude, location.longitude)
                }
            } catch (e: Exception) {
                // Ignore
            }
        }

        // 4. Animate Camera if a location was resolved
        if (targetLatLng != null) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(targetLatLng, 15f)
            )
        }
    }

    val mapProperties = remember {
        MapProperties(
            mapStyleOptions = MapStyleOptions(DARK_MAP_STYLE_JSON),
            isMyLocationEnabled = hasLocationPermission
        )
    }

    LaunchedEffect(uiState) {
        if (uiState is RemindersUiState.Success) {
            viewModel.resetState()
            onNavigateBack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Z=0 Base Layer: Map (OLED Dark Mode)
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = true)
        ) {
            // Draw a circle to represent the geofence radius
            val currentTarget = cameraPositionState.position.target
            Circle(
                center = currentTarget,
                radius = radius.toDouble(),
                fillColor = primaryColor.copy(alpha = 0.2f),
                strokeColor = primaryColor,
                strokeWidth = 2f
            )
        }

        // Fixed center pin (Classic Location Pin)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "📍",
                fontSize = 40.sp,
                modifier = Modifier
                    .offset(y = (-20).dp) // Offset to make the tip point at the center
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), ambientColor = Color.Black)
            )
        }

        // Top Floating Solid Bar (Solid Elevated Surfaces)
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
                Text(
                    text = "New Reminder",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Persistent Bottom Panel (Solid Elevated Surfaces)
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
                        val currentTarget = cameraPositionState.position.target
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
            .background(MaterialTheme.colorScheme.surfaceVariant) // Lighter contrast
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

/**
 * Custom OLED Dark Map Style for Google Maps
 */
private const val DARK_MAP_STYLE_JSON = """
[
  {
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#0C0A09"
      }
    ]
  },
  {
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#746855"
      }
    ]
  },
  {
    "elementType": "labels.text.stroke",
    "stylers": [
      {
        "color": "#242f3e"
      }
    ]
  },
  {
    "featureType": "administrative.locality",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "poi",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#1C1917"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#6b9a76"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#1C1917"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#1E293B"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#9ca5b3"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#312E81"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#1f2835"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#f3d19c"
      }
    ]
  },
  {
    "featureType": "transit",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#2f3948"
      }
    ]
  },
  {
    "featureType": "transit.station",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#0C0A09"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#515c6d"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "labels.text.stroke",
    "stylers": [
      {
        "color": "#17263c"
      }
    ]
  }
]
"""
