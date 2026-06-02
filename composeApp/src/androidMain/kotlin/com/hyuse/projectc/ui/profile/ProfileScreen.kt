package com.hyuse.projectc.ui.profile

import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hyuse.projectc.domain.model.AppConstants
import com.hyuse.projectc.domain.model.User
import com.hyuse.projectc.ui.components.AddressSearchDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.withContext
import org.koin.compose.viewmodel.koinViewModel
import org.maplibre.compose.camera.*
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

/**
 * Screen for creating or editing a user profile.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    profileState: ProfileState,
    onSave: (name: String, nickname: String, university: String, course: String, currencySymbol: String, address: String, lat: Double?, lng: Double?) -> Unit,
    onBack: () -> Unit,
    onClearError: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
    showBackButton: Boolean = true
) {
    var name by rememberStringState("")
    var nickname by rememberStringState("")
    var university by rememberStringState("")
    var course by rememberStringState("")
    var currencySymbol by rememberStringState("₱")
    var address by rememberStringState("")
    var homeLat by remember { mutableStateOf<Double?>(null) }
    var homeLng by remember { mutableStateOf<Double?>(null) }

    // Dropdown state
    var currencyExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var showAddressSearch by remember { mutableStateOf(false) }
    var showMapPinning by remember { mutableStateOf(false) }

    // Update fields when profile is loaded
    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            name = profileState.profile.name
            nickname = profileState.profile.nickname ?: ""
            university = profileState.profile.university
            course = profileState.profile.course
            currencySymbol = profileState.profile.currencySymbol
            address = profileState.profile.address
            homeLat = profileState.profile.homeLatitude
            homeLng = profileState.profile.homeLongitude
        } else if (profileState is ProfileState.SaveSuccess) {
            onSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Profile", fontWeight = FontWeight.Bold) },
                actions = {
                    if (showBackButton) {
                        TextButton(onClick = onBack) {
                            Text("Back")
                        }
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

                // Address Selection Block
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Home Location",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Surface(
                        onClick = { showAddressSearch = true },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (address.isEmpty()) "No address selected" else address,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (address.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
                            )
                            if (homeLat != null && homeLng != null) {
                                Text(
                                    text = "Precise coordinates set",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Search Button
                        Button(
                            onClick = { showAddressSearch = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("SEARCH", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        
                        // Map Pin Button
                        Button(
                            onClick = { showMapPinning = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer, contentColor = MaterialTheme.colorScheme.onTertiaryContainer),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("PIN ON MAP", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

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
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
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
                    onClick = { onSave(name, nickname, university, course, currencySymbol, address, homeLat, homeLng) },
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

    // Address Search Dialog (Photon API)
    if (showAddressSearch) {
        AddressSearchDialog(
            viewModel = viewModel,
            onAddressSelected = { feature ->
                address = feature.properties.getFormattedAddress()
                homeLat = feature.geometry.latitude
                homeLng = feature.geometry.longitude
                showAddressSearch = false
            },
            onDismiss = { showAddressSearch = false }
        )
    }

    // Map Pinning Modal (MapLibre)
    if (showMapPinning) {
        MapPinningDialog(
            initialLocation = if (homeLat != null && homeLng != null) Position(longitude = homeLng!!, latitude = homeLat!!) else Position(longitude = 101.6869, latitude = 3.1390),
            onConfirm = { selectedPos, resolvedAddress ->
                homeLat = selectedPos.latitude
                homeLng = selectedPos.longitude
                address = resolvedAddress
                showMapPinning = false
            },
            onDismiss = { showMapPinning = false }
        )
    }
}

@OptIn(FlowPreview::class)
@Composable
fun MapPinningDialog(
    initialLocation: Position,
    onConfirm: (Position, String) -> Unit,
    onDismiss: () -> Unit
) {
    val cameraState = rememberCameraState(
        firstPosition = CameraPosition(target = initialLocation, zoom = 15.0)
    )
    val context = LocalContext.current
    var resolvedAddress by remember { mutableStateOf("Resolving...") }

    // Reverse geocode whenever the camera stops moving (debounced snapshotFlow)
    LaunchedEffect(cameraState) {
        snapshotFlow { cameraState.position.target }
            .debounce(500)
            .collect { target ->
                resolvedAddress = withContext(Dispatchers.IO) {
                    try {
                        val geocoder = Geocoder(context)
                        val addresses = geocoder.getFromLocation(target.latitude, target.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            addresses[0].getAddressLine(0) ?: "Unknown Address"
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
                MaplibreMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraState = cameraState,
                    baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/dark")
                )

                // Center Pin
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Pin",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(48.dp)
                            .offset(y = (-24).dp)
                    )
                }

                // Header
                Surface(
                    modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pin Your Location",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }

                // Bottom Confirmation
                Surface(
                    modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shadowElevation = 16.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "SELECTED ADDRESS",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = resolvedAddress,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { onConfirm(cameraState.position.target, resolvedAddress) },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("CONFIRM LOCATION", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// Helper to use remember with mutableStateOf and initial value easily
@Composable
private fun rememberStringState(initialValue: String) = remember { mutableStateOf(initialValue) }
