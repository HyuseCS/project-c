package com.hyuse.projectc.ui.reminders

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.hyuse.projectc.domain.model.LocationData
import com.hyuse.projectc.domain.model.ReminderImportance
import com.hyuse.projectc.ui.components.AddressSearchDialog
import com.hyuse.projectc.ui.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.viewmodel.koinViewModel
import org.maplibre.compose.camera.*
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreen(
    onNavigateBack: () -> Unit,
    viewModel: RemindersViewModel = koinViewModel(),
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var importance by remember { mutableStateOf(ReminderImportance.MEDIUM) }
    
    // Location State
    var selectedLocation by remember { mutableStateOf<LocationData?>(null) }
    var selectedAddress by remember { mutableStateOf("") }
    var showMapDialog by remember { mutableStateOf(false) }

    // Permission Launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (fineGranted) {
            showMapDialog = true
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is RemindersUiState.Success) {
            viewModel.resetState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Reminder", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.saveReminder(
                                title = title.ifBlank { "Untitled Reminder" },
                                description = description,
                                dateMillis = System.currentTimeMillis(),
                                timeMillis = 0L,
                                importance = importance,
                                latitude = selectedLocation?.latitude,
                                longitude = selectedLocation?.longitude,
                                radius = selectedLocation?.radius
                            )
                        },
                        enabled = uiState !is RemindersUiState.Saving
                    ) {
                        if (uiState is RemindersUiState.Saving) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Text("SAVE", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title Input
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text(
                            text = "Title",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    innerTextField()
                }
            )

            // Description Input
            BasicTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (description.isEmpty()) {
                        Text(
                            text = "Note",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Location Trigger Section
            Text(
                text = "TRIGGER",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Surface(
                onClick = {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    
                    if (hasPermission) {
                        showMapDialog = true
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (selectedLocation == null) "Add Location Trigger" else "Location Reminder Set",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (selectedAddress.isNotEmpty()) {
                            Text(
                                text = selectedAddress,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        } else if (selectedLocation != null) {
                            Text(
                                text = "Radius: ${selectedLocation?.radius?.toInt()}m",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (selectedLocation != null) {
                        IconButton(onClick = {
                            selectedLocation = null
                            selectedAddress = ""
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Remove location")
                        }
                    }
                }
            }

            // Importance Section
            Text(
                text = "IMPORTANCE",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReminderImportance.values().forEach { imp ->
                    FilterChip(
                        selected = importance == imp,
                        onClick = { importance = imp },
                        label = { Text(imp.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            if (uiState is RemindersUiState.Error) {
                Text(
                    text = (uiState as RemindersUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

    if (showMapDialog) {
        ReminderMapDialog(
            initialLocation = selectedLocation ?: LocationData(longitude = 101.6869, latitude = 3.1390, radius = 250.0),
            profileViewModel = profileViewModel,
            onConfirm = { loc, addr ->
                selectedLocation = loc
                selectedAddress = addr
                showMapDialog = false
            },
            onDismiss = { showMapDialog = false }
        )
    }
}

@OptIn(FlowPreview::class)
@Composable
fun ReminderMapDialog(
    initialLocation: LocationData,
    profileViewModel: ProfileViewModel,
    onConfirm: (LocationData, String) -> Unit,
    onDismiss: () -> Unit
) {
    val cameraState = rememberCameraState(
        firstPosition = CameraPosition(
            target = Position(longitude = initialLocation.longitude, latitude = initialLocation.latitude),
            zoom = 15.0
        )
    )
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var radius by remember { mutableStateOf(initialLocation.radius.toFloat()) }
    var resolvedAddress by remember { mutableStateOf("Resolving...") }
    var showAddressSearch by remember { mutableStateOf(false) }

    // Logic to set initial location based on GPS
    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        if (hasPermission) {
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

    // Reverse geocode whenever camera stops
    LaunchedEffect(cameraState) {
        snapshotFlow { cameraState.position.target }
            .debounce(500)
            .collect { target ->
                resolvedAddress = withContext(Dispatchers.IO) {
                    try {
                        val geocoder = Geocoder(context)
                        val addresses = geocoder.getFromLocation(target.latitude, target.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            addresses[0].getAddressLine(0) ?: "Unknown Location"
                        } else "Coordinates: ${target.latitude}, ${target.longitude}"
                    } catch (e: Exception) {
                        "Coordinates: ${target.latitude}, ${target.longitude}"
                    }
                }
            }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Map Layer
                MaplibreMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraState = cameraState,
                    baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/dark")
                )

                // Radius Visualizer & Center Pin
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val lat = cameraState.position.target.latitude
                    val zoom = cameraState.position.zoom
                    val density = LocalDensity.current.density
                    val metersPerPixel = 78271.51696 * cos(lat * PI / 180.0) / 2.0.pow(zoom)
                    val radiusPx = radius / metersPerPixel
                    val radiusDp = (radiusPx / density).toFloat().dp

                    Box(
                        modifier = Modifier
                            .size(radiusDp * 2)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )

                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Pin",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp).offset(y = (-24).dp)
                    )
                }

                // Header / Search
                Surface(
                    modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Surface(
                            onClick = { showAddressSearch = true },
                            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Search location...", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            }
                        }
                    }
                }

                // Bottom Panel
                Surface(
                    modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 16.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(resolvedAddress, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 2)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Radius", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${radius.toInt()}m", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Slider(
                            value = radius,
                            onValueChange = { radius = it },
                            valueRange = 50f..1000f,
                            steps = 18
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = {
                                onConfirm(
                                    LocationData(
                                        latitude = cameraState.position.target.latitude,
                                        longitude = cameraState.position.target.longitude,
                                        radius = radius.toDouble()
                                    ),
                                    resolvedAddress
                                )
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("CONFIRM LOCATION", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (showAddressSearch) {
            AddressSearchDialog(
                viewModel = profileViewModel,
                onAddressSelected = { feature ->
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
}
