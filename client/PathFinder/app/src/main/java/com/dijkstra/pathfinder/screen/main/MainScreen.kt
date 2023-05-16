package com.dijkstra.pathfinder.screen.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.NavController
import com.chargemap.compose.numberpicker.ListItemPicker
import com.dijkstra.pathfinder.R
import com.dijkstra.pathfinder.UnityHolderActivity
import com.dijkstra.pathfinder.components.*
import com.dijkstra.pathfinder.data.dto.Point
import com.dijkstra.pathfinder.data.dto.SearchValidResponse
import com.dijkstra.pathfinder.screen.nfc_start.NFCViewModel
import com.dijkstra.pathfinder.ui.theme.IconColor
import com.dijkstra.pathfinder.ui.theme.nanumSquareNeo
import com.dijkstra.pathfinder.util.Constant
import com.dijkstra.pathfinder.util.NetworkResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val TAG = "MainScreen_SSAFY"

@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun MainScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    nfcViewModel: NFCViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    // Search State
    val searchQueryState = remember { mutableStateOf("") }
    val speechDialogQueryState = remember { mutableStateOf("") }
    val destinationLocationName = remember { mainViewModel.destinationLocationName }
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
    val postSearchValidResponseSharedFlow =
        mainViewModel.postSearchValidResponseSharedFlow.collectAsState(
            initial = null
        )
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // BottomSheet State
    val openBottomSheet = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val countdownText = remember { mutableStateOf("3") }

    // Current Location State
    val openCurrentLocationDialog = remember { mutableStateOf(false) }
    val postCurrentLocationResponseSharedFlow =
        mainViewModel.postCurrentLocationResponseSharedFlow.collectAsState(
            initial = null
        )
    val currentLocationName = remember { mainViewModel.currentLocationName }

    // Emergency State
    val openEmergencyDialog = remember { mutableStateOf(false) }

    // Floor State
    val openFloorDialog = remember { mutableStateOf(false) }
    val floorValues = nfcViewModel.getNFCData.value!!.floorsNumber!!
    val mapImageUrls = nfcViewModel.getNFCData.value!!.mapImageUrl!!

    var tempFloorIndex = remember { mutableStateOf(0) }
    var floorIndex = remember { mutableStateOf(0) }

    mainViewModel.allBeaconList = nfcViewModel.getNFCData.value!!.beaconList!!
    // MainViewModel Response State
//    val postFacilityDynamicResponseStateFlow =
//        mainViewModel.postFacilityDynamicResponseStateFlow.collectAsState()

    val postFacilityDynamicResponseSharedFlow =
        mainViewModel.postFacilityDynamicResponseSharedFlow.collectAsState(null)

    // Search LazyColumn
    var searchingList by remember {
        mutableStateOf(mutableListOf<String>())
    }

    // Permission State
    val btPermissionsState = rememberMultiplePermissionsState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
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

    LaunchedEffect(
        key1 = openBottomSheet.value,
        key2 = currentLocationName.value,
        key3 = destinationLocationName.value
    ) {

        if (openBottomSheet.value && currentLocationName.value.isNotEmpty() && destinationLocationName.value.isNotEmpty()) {
            for (i in 2 downTo 0) {
                delay(1000)
                countdownText.value = i.toString()
            }
            coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                if (!bottomSheetState.isVisible) {
                    openBottomSheet.value = false
                    val intent = Intent(context, UnityHolderActivity::class.java).apply {
                        putExtra(Constant.INTENT_START_POSITION, mainViewModel.currentLocationPoint)
                        putExtra(
                            Constant.INTENT_GOAL_POSITION,
                            mainViewModel.destinationLocationPoint
                        )
                        putExtra(
                            Constant.INTENT_GOAL_NAME,
                            mainViewModel.destinationLocationName.value
                        )
                    }
                    context.startActivity(intent)
                }
                countdownText.value = "3"
            }
        } else {
            countdownText.value = "3"
        }
    } // End of LaunchedEffect

    LaunchedEffect(key1 = lifecycleOwner.lifecycle.currentState) {
        Log.d(
            TAG, "sdr lifecycle: ${
                lifecycleOwner.lifecycle.currentState
            }"
        )
    }

    DisposableEffect(lifecycleOwner) {
        // Destroy speechRecognizer on dispose
        onDispose {
            Log.d(TAG, "DisposableEffect: 여기?")
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
            onValueChange = { value ->
                searchQueryState.value = value
                mainViewModel.postFacilityDynamic(searchQueryState.value)
            },
            active = searchBarActiveState.value,
            onActiveChange = { searchBarActiveState.value = it },
            modifier = Modifier
                .zIndex(3.0f)
                .padding(top = 10.dp),
            placeholder = stringResource(id = R.string.input_destination),
            keyboardActions = KeyboardActions(
                onSearch = {
                    mainViewModel.postFacilityValid(searchQueryState.value.trim())
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
                                startListening(speechRecognizer, searchQueryState, isRecording)
                            }
                        } else {
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
                        mainViewModel.postFacilityValid(searchQueryState.value.trim())
                        focusManager.clearFocus()
                        keyboardController?.hide()
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                postFacilityDynamicResponseSharedFlow.let {
                    if (it.value?.data != null) {
                        when (it.value!!) {
                            is NetworkResult.Success -> {
                                searchingList = it.value!!.data as MutableList<String>
                            }

                            is NetworkResult.Loading -> {
                                // Progressbar show
                                //searchingList = it.value!!.data as MutableList<String>
                            }

                            is NetworkResult.Error -> {
                                // Error message Showing
                                //searchingList = it.value!!.data as MutableList<String>
                            }
                        }
                    }
                } // End of postFacilityDynamicResponseStateFlow.let
                items(
                    searchingList
                ) {
                    ListItem(
                        headlineContent = {
                            Text(text = it)
                        },
                        modifier = Modifier
                            .clickable {
                                searchQueryState.value = it
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
            ZoomableImage(model = mapImageUrls[floorIndex.value])
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
                modifier = Modifier.padding(),
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
                                openCurrentLocationDialog.value = true
                                mainViewModel.postCurrentLocation(
                                    Point(
                                        mainViewModel.kalmanLocation.value[0],
                                        0.0,
                                        mainViewModel.kalmanLocation.value[2]
                                    )
                                )
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

        // Current Location Dialog
        if (openCurrentLocationDialog.value) {
            Dialog(
                onDismissRequest = {
                    openCurrentLocationDialog.value = false
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
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "My Location Button",
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .padding(bottom = 16.dp),
                            tint = IconColor
                        )
                        Text(
                            text = stringResource(id = R.string.current_location),
                            modifier = Modifier.padding(),
                            fontFamily = nanumSquareNeo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Loading
                        Column(
                            modifier = Modifier.height(100.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            postCurrentLocationResponseSharedFlow.let { networkResult ->
                                when (networkResult.value!!) {
                                    is NetworkResult.Success -> {
                                        Text(
                                            text = mainViewModel.tempLocationName,
                                            modifier = Modifier.padding(bottom = 16.dp),
                                            fontFamily = nanumSquareNeo,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                        )
                                        Text(
                                            text = stringResource(id = R.string.retry_current_location),
                                            modifier = Modifier.padding(),
                                            color = Color.LightGray,
                                            fontFamily = nanumSquareNeo,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 16.sp,
                                        )
                                    }
                                    is NetworkResult.Loading -> {
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResult.Error -> {
                                        // Error message Showing
                                        val response = networkResult.value as NetworkResult.Error
                                        ErrorBody(response.code)
                                    }
                                }
                            }
                        } // End of NowLocation Dialog Main Contents

                        // TextButtons
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                MainScreenTextButton(
                                    onClick = {
                                        openCurrentLocationDialog.value = false
                                        openBottomSheet.value = false
                                    },
                                    text = stringResource(id = R.string.cancel)
                                )
                                MainScreenTextButton(
                                    onClick = {
                                        mainViewModel.postCurrentLocation(
                                            Point(
                                                mainViewModel.kalmanLocation.value[0],
                                                0.0,
                                                mainViewModel.kalmanLocation.value[2]
                                            )
                                        )
                                    },
                                    text = stringResource(id = R.string.retry),
                                )
                                MainScreenTextButton(
                                    onClick = {
                                        openCurrentLocationDialog.value = false
                                        mainViewModel.currentLocationPoint =
                                            mainViewModel.tempLocationPoint
                                        mainViewModel.currentLocationName.value =
                                            mainViewModel.tempLocationName

                                        Log.d(
                                            TAG,
                                            "ok VM: ${mainViewModel.currentLocationPoint} ${mainViewModel.currentLocationName.value}"
                                        )
                                        Log.d(
                                            TAG,
                                            "ok: ${openBottomSheet.value} ${currentLocationName.value}"
                                        )
                                    },
                                    text = stringResource(id = R.string.ok),
                                )
                            } // End of TextButtons Row
                        }
                    }
                } // End of Surface
            } // End of Dialog
        } // Current Location Dialog if-state

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
                            text = stringResource(id = R.string.warning),
                            modifier = Modifier.padding(),
                            fontFamily = nanumSquareNeo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (mainViewModel.currentLocationName.value == "") {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.set_now_location),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    mainViewModel.postFindHelp(
                                        Constant.AED,
                                        mainViewModel.currentLocationPoint
                                    )
                                }
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
                                if (mainViewModel.currentLocationName.value == "") {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.set_now_location),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    mainViewModel.postFindHelp(
                                        Constant.FIRE,
                                        mainViewModel.currentLocationPoint
                                    )
                                }
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
                        Spacer(modifier = Modifier.height(24.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            MainScreenTextButton(
                                onClick = {
                                    openEmergencyDialog.value = false
                                },
                                text = stringResource(id = R.string.cancel),
                            )
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
                    tempFloorIndex = floorIndex
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
                            imageVector = Icons.Default.Layers,
                            contentDescription = "Floor Button",
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .padding(bottom = 16.dp),
                            tint = IconColor
                        )
                        Text(
                            text = stringResource(id = R.string.select_floor),
                            modifier = Modifier.padding(),
                            fontFamily = nanumSquareNeo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ListItemPicker(
                            label = { it },
                            value = floorValues[tempFloorIndex.value],
                            onValueChange = { value ->
                                tempFloorIndex.value = floorValues.indexOf(value)
                            },
                            list = floorValues
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            MainScreenTextButton(
                                onClick = {
                                    openFloorDialog.value = false
                                    tempFloorIndex.value = floorIndex.value
                                },
                                text = stringResource(id = R.string.cancel)
                            )
                            MainScreenTextButton(
                                onClick = {
                                    openFloorDialog.value = false
                                    floorIndex.value = tempFloorIndex.value
                                },
                                text = stringResource(id = R.string.ok),
                            )
                        } // End of Cancel Button Box
                    } // End of Column
                } // End of Surface
            } // End of Dialog
        } // Floor Dialog if-state

        // ModalBottomSheet
        if (openBottomSheet.value) {
            if (currentLocationName.value == "") {
                if (btPermissionsState.allPermissionsGranted) {
                    mainViewModel.postCurrentLocation(
                        Point(
                            mainViewModel.kalmanLocation.value[0],
                            0.0,
                            mainViewModel.kalmanLocation.value[2]
                        )
                    )
                    openCurrentLocationDialog.value = true
                } else {
                    btPermissionsState.launchMultiplePermissionRequest()
                }
            } else {
                MainModalBottomSheet(
                    onDismissRequest = { openBottomSheet.value = false },
                    sheetState = bottomSheetState,
                    nowLocation = currentLocationName.value,
                    destination = destinationLocationName.value,
                    countdownText = countdownText.value,
                    onClick = {
                        // TODO : GO TO UNITY ACTIVITY
                        openBottomSheet.value = false
                        val intent = Intent(context, UnityHolderActivity::class.java).apply {
                            putExtra(
                                Constant.INTENT_START_POSITION,
                                mainViewModel.currentLocationPoint
                            )
                            putExtra(
                                Constant.INTENT_GOAL_POSITION,
                                mainViewModel.destinationLocationPoint
                            )
                            putExtra(
                                Constant.INTENT_GOAL_NAME,
                                mainViewModel.destinationLocationName.value
                            )
                        }
                        context.startActivity(intent)
                        Log.d(TAG, "start: ${mainViewModel.currentLocationPoint}")
                        Log.d(TAG, "goal: ${mainViewModel.destinationLocationPoint}")
                    }
                )
            }
        } // End of BottomSheet Modal
    } // End of MainScreen Surface

    val postFindHelpResponseSharedFlow =
        mainViewModel.postFindHelpResponseSharedFlow.collectAsState(initial = null)

    postFindHelpResponseSharedFlow.let { networkResult ->
        LaunchedEffect(key1 = networkResult.value) {
            if (networkResult.value != null) {
                when (networkResult.value!!) {
                    is NetworkResult.Success -> {
                        openEmergencyDialog.value = false
                        openBottomSheet.value = true
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.error_current_location),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkResult.Loading -> {}
                    else -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.error_current_location),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    } // End of postFindHelpResponseSharedFlow

    postSearchValidResponseSharedFlow.let { networkResult ->
        LaunchedEffect(key1 = networkResult.value) {
            if (networkResult.value != null) {
                when (networkResult.value!!) {
                    is NetworkResult.Success -> {
                        val searchValidResponse = networkResult.value!!.data as SearchValidResponse
                        if (searchValidResponse.isValid) {
                            mainViewModel.destinationLocationName.value =
                                searchQueryState.value.trim()
                            mainViewModel.destinationLocationPoint = searchValidResponse.points
                            openBottomSheet.value = true
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.invalid_destination),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.error_current_location),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkResult.Loading -> {}
                    else -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.error_current_location),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } // End of LaunchedEffect
    } // End of postSearchValidResponseSharedFlow.let

} // End of MainScreen

@Composable
fun ErrorBody(errorCode: Int?) {
    // 422 층의 범위를 벗어남
    // 418 벽의 유효하지 않은 좌표입니다
    val errorText =
        when (errorCode) {
            418 -> {
                stringResource(id = R.string.invalid_current_location)
            }
            422 -> {
                stringResource(id = R.string.out_of_range_current_location)
            }
            else -> {
                stringResource(id = R.string.error_current_location)
            }
        }

    Text(
        text = errorText,
        modifier = Modifier.padding(bottom = 4.dp),
        fontFamily = nanumSquareNeo,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    )
    Text(
        text = stringResource(id = R.string.retry_after_current_location),
        modifier = Modifier.padding(),
        fontFamily = nanumSquareNeo,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    )
}