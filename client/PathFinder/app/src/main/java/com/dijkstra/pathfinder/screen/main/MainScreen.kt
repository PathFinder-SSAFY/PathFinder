package com.dijkstra.pathfinder.screen.main

import android.Manifest
import android.speech.SpeechRecognizer
import android.util.Log
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
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.dijkstra.pathfinder.R
import com.dijkstra.pathfinder.components.*
import com.dijkstra.pathfinder.ui.theme.IconColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val TAG = "MainScreen_SDR"

@Preview
@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun MainScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    navController: NavController = NavController(LocalContext.current)
) {
    // Search State
    val searchQueryState = remember { mutableStateOf("") }
    val speechDialogQueryState = remember { mutableStateOf("") }
    val destinationQueryState = remember { mutableStateOf("") }
    val searchBarActiveState = remember { mutableStateOf(false) }
    val isRecording = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    val speechPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
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
            .background(Color.Blue)
    ) {
        AutoCompleteSearchBar(
            value = searchQueryState.value,
            onValueChange = { value -> searchQueryState.value = value },
            active = searchBarActiveState.value,
            onActiveChange = { searchBarActiveState.value = it },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 10.dp)
                .zIndex(2.0f),
            placeholder = stringResource(id = R.string.input_destination),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // TODO: logic onSearch
                    destinationQueryState.value = searchQueryState.value.trim()
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
                        contentDescription = "Search",
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
        }

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

        // ModalBottomSheet
        if (openBottomSheet.value) {
            MainModalBottomSheet(
                onDismissRequest = { openBottomSheet.value = false },
                sheetState = bottomSheetState,
                openBottomSheet = openBottomSheet,
                nowLocation = "현재 위치",
                destination = destinationQueryState.value,
                countdownText = countdownText.value,
                scope = coroutineScope
            )
        } // End of Bottom Modal

    } // End of MainScreen


} // End of MainScreen