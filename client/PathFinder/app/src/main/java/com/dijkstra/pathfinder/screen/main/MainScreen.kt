package com.dijkstra.pathfinder.screen.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.dijkstra.pathfinder.R
import com.dijkstra.pathfinder.components.*
import com.dijkstra.pathfinder.ui.theme.IconColor
import com.dijkstra.pathfinder.ui.theme.nanumSquareNeo
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.service.BeaconService
import org.altbeacon.beacon.service.ScanJob


private const val TAG = "MainScreen_SDR"

@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun MainScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    // Search State
    val searchQueryState = remember { mutableStateOf("") }
    val speechDialogQueryState = remember { mutableStateOf("") }
    val destinationQueryState = remember { mutableStateOf("") }
    val searchBarActiveState = remember { mutableStateOf(false) }
    val isRecording = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    val speechPermissionState = rememberPermissionState(
        Manifest.permission.RECORD_AUDIO,
        onPermissionResult = { permission ->
            if (permission) {
                if (!isRecording.value) {
                    isRecording.value = true
                    Log.d(TAG, "MainScreen: speech")
                    startListening(speechRecognizer, searchQueryState, isRecording)
                }
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.mic_permission_plz),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val pictureUrl = remember { mutableStateOf("https://i.redd.it/nxa1etbwlfa51.jpg") }

    // BottomSheet State
    val openBottomSheet = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val countdownText = remember { mutableStateOf("3") }

    // Emergency State
    val openEmergencyDialog = remember { mutableStateOf(false) }

    // Floor State
    val openFloorDialog = remember { mutableStateOf(false) }

    // Beacon State
    val btPermissionsState = rememberMultiplePermissionsState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
            )
        } else {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
            )
        },
        onPermissionsResult = { permissionMap ->
            var flag = true
            permissionMap.forEach { entry ->
                if (!entry.value) {
                    flag = false
                }
            }
            if (flag) {
                mainViewModel.startRangingBeacons()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.bt_permission_plz),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    ) // End of btPermissionsState

    LaunchedEffect(key1 = btPermissionsState) {
        if (btPermissionsState.allPermissionsGranted) {
            mainViewModel.startRangingBeacons()
        } else {
            btPermissionsState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(key1 = openBottomSheet.value) {
        if (openBottomSheet.value) {
            for (i in 2 downTo 0) {
                delay(1000)
                countdownText.value = i.toString()
            }
            coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                if (!bottomSheetState.isVisible) {
                    openBottomSheet.value = false
                }
                countdownText.value = "3"
            }
        } else {
            countdownText.value = "3"
        }
    } // End of LaunchedEffect

    DisposableEffect(lifecycleOwner) {
        // Destroy speechRecognizer on dispose
        onDispose {
            speechRecognizer.destroy()
        }
    } // End of DisposableEffect

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        AutoCompleteSearchBar(
            value = searchQueryState.value,
            onValueChange = { value -> searchQueryState.value = value },
            active = searchBarActiveState.value,
            onActiveChange = { searchBarActiveState.value = it },
            modifier = Modifier
                .zIndex(3.0f)
                .padding(top = 10.dp),
            placeholder = stringResource(id = R.string.input_destination),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // TODO: logic onSearch
                    destinationQueryState.value = searchQueryState.value.trim()
                    Log.d(TAG, "MainScreen: ${destinationQueryState.value}")
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            leadingIcon = {
                IconButton(
                    onClick = {
                        if (speechPermissionState.status.isGranted) {
                            if (!isRecording.value) {
                                isRecording.value = true
                                Log.d(TAG, "MainScreen: speech")
                                startListening(speechRecognizer, searchQueryState, isRecording)
                            }
                        } else {
                            Log.d(TAG, "MainScreen: ${speechPermissionState.permission}")
                            speechPermissionState.launchPermissionRequest()

                        }
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Speech To Text",
                        tint = IconColor
                    )
                } // Mic Icon
            }, // End of leadingIcon
            trailingIcon = {
                IconButton(
                    onClick = {
                        destinationQueryState.value = searchQueryState.value.trim()
                        focusManager.clearFocus()
                        keyboardController?.hide()

                        if (destinationQueryState.value.isNotBlank()) {
                            openBottomSheet.value = true
                        }

                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search),
                        tint = IconColor
                    )
                } // Search Icon
            } // End of trailingIcon
        ) {
            // TODO : Change Person Dummy Data to Coord
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    persons.map { person ->
                        person.name
                    }.filter { name ->
                        name.lowercase().contains(searchQueryState.value.lowercase())
                    }.sorted()
                ) { name ->
                    ListItem(
                        headlineContent = {
                            Text(text = name)
                        },
                        modifier = Modifier
                            .clickable {
                                searchQueryState.value = name
                                searchBarActiveState.value = false
                            }
                            .background(Color.Transparent)
                    )
                } // End of Item
            } // End of AutoCompleteLazyColumn
        } // End of AutoCompleteSearchBar

        // Picture
        Box(
            modifier = Modifier.zIndex(1.0f),
            contentAlignment = Alignment.Center
        ) {
            ZoomableImage(model = pictureUrl.value)
        } // End of Picture Box

        // Bottom Floating Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(2.0f)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.BottomStart
            ) {
                MainScreenBottomIconButton(onClick = { openEmergencyDialog.value = true }) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Emergency Button",
                        tint = Color.Red
                    )
                }
            } // End of Emergency Button

            // Floating Action Buttons Box
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(modifier = Modifier) {
                    MainScreenBottomIconButton(
                        onClick = { openFloorDialog.value = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = "Floor Button",
                            tint = IconColor
                        )
                    } // End of Stair Button

                    Spacer(modifier = Modifier.height(8.dp))

                    MainScreenBottomIconButton(
                        onClick = {
                            if (btPermissionsState.allPermissionsGranted) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.cal_now_location),
                                    Toast.LENGTH_SHORT
                                ).show()
                                // TODO: Now Location to Server
                                Log.d(TAG, "MyLocation: ${mainViewModel.kalmanLocation.value}")
                            } else {
                                btPermissionsState.launchMultiplePermissionRequest()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "My Location Button",
                            tint = IconColor
                        )
                    } // End of My Location Button

                } // End of Floating Action Buttons Column
            } // End of Floating Action Buttons Box
        } // End of Bottom Floating Button Row


        // Speech Dialog
        if (isRecording.value) {
            Dialog(
                onDismissRequest = {
                    isRecording.value = false
                    searchQueryState.value = speechDialogQueryState.value
                    speechRecognizer.stopListening()
                }
            ) {
                Surface(
                    modifier = Modifier,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    speechDialogQueryState.value = ""
                    SpeechDialogContent(transcription = speechDialogQueryState)
                }
            }
        } // Speech Dialog if-state

        // Emergency Dialog
        if (openEmergencyDialog.value) {
            Dialog(
                onDismissRequest = {
                    openEmergencyDialog.value = false
                }
            ) {
                Surface(
                    modifier = Modifier,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Emergency Button",
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .padding(bottom = 16.dp),
                            tint = Color.Red
                        )
                        Text(
                            text = "경고",
                            modifier = Modifier.padding(bottom = 16.dp),
                            fontFamily = nanumSquareNeo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        )
                        Button(
                            onClick = {
                                destinationQueryState.value = "심장제세동기"
                                openEmergencyDialog.value = false
                                openBottomSheet.value = true
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(bottom = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.start_nav_to_aed),
                                fontFamily = nanumSquareNeo,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        } // End of AED Button
                        Button(
                            onClick = {
                                destinationQueryState.value = "소화기"
                                openEmergencyDialog.value = false
                                openBottomSheet.value = true
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(bottom = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.start_nav_to_fire_extinguisher),
                                fontFamily = nanumSquareNeo,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        } // End of Fire Extinguisher Button
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            TextButton(onClick = { openEmergencyDialog.value = false }) {
                                Text(
                                    text = stringResource(id = R.string.cancel),
                                    fontFamily = nanumSquareNeo,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = IconColor
                                )
                            }
                        } // End of Cancel Button Box
                    } // End of Column
                } // End of Surface
            } // End of Dialog
        }  // Emergency Dialog if-state

        // Floor Dialog
        if (openFloorDialog.value) {
            Dialog(
                onDismissRequest = {
                    openFloorDialog.value = false
                }
            ) {
                Surface(
                    modifier = Modifier,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                    }
                }
            }
        }

        // ModalBottomSheet
        if (openBottomSheet.value) {
            MainModalBottomSheet(
                onDismissRequest = { openBottomSheet.value = false },
                sheetState = bottomSheetState,
                openBottomSheet = openBottomSheet,
                // TODO : 현재 위치 -> 진짜 위치
                nowLocation = "현재 위치",
                destination = destinationQueryState.value,
                countdownText = countdownText.value,
                scope = coroutineScope
            )
        } // End of Bottom Modal

    } // End of MainScreen Surface

} // End of MainScreen