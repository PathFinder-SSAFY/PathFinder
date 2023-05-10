package com.dijkstra.pathfinder

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dijkstra.pathfinder.screen.login.LoginViewModel
import com.dijkstra.pathfinder.screen.main.MainScreen
import com.dijkstra.pathfinder.screen.nfc_start.NFCViewModel
import com.dijkstra.pathfinder.ui.theme.PathFinderTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity_싸피"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // NFC Module
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    private lateinit var filters: Array<IntentFilter>

    // Navigation Module
    private lateinit var navController: NavHostController

    // ViewModels
    private val nfcViewModel by viewModels<NFCViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ${application}")
        setContent {
            // val intent = Intent(this, UnityHolderActivity::class.java)
            PathFinderTheme {

                navController = rememberNavController()
//                SetUpNavGraph(navController = navController)

                MainScreen(navController = navController)
//                startUnityLayout()
            }
        }

        initNFCAdapter()
    } // End of onCreate

    private fun initNFCAdapter() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC Tag", Toast.LENGTH_SHORT).show()
            finish()
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)

        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        val filter = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        filters = arrayOf(filter)
    } // End of initNFCAdapter

    override fun onResume() {
        super.onResume()
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, null)
    } // End of onResume

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        readNFCData(getIntent())
    } // End of onNewIntent

    private fun readNFCData(intent: Intent) {
        val message = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        if (message != null) {
            message.forEach {
                val ndef = it as NdefMessage

                for (rec in ndef.records) {
//                    Log.d(TAG, "TNF  : ${rec.tnf}")
//                    Log.d(TAG, "ID   : ${String(rec.id)}")
//                    Log.d(TAG, "TYPE : ${String(rec.type)}")
//                    Log.d(TAG, "PLoad: ${String(rec.payload)}")

                    val strPload = String(rec.payload)
                    Log.d(TAG, "processNFC: ${strPload.substring(3)}")

                    // nfcViewModel setNFCState
                    nfcViewModel.setNFCState(newNFCState = strPload.substring(3))

                    // nfcViewModel setNFCSharedFlow
                    nfcViewModel.setNFCSharedFlow(newSharedNFCState = strPload.substring(3))

                    val type = String(rec.type)
                    when (type) {
                        "T" -> {
                            val payload = rec.payload
                            Log.d(TAG, "processNFC type T : ${String(payload)}")
                        }
                        "U" -> {
                            val uri = rec.toUri()
                            startActivity(Intent().apply {
                                setAction(Intent.ACTION_SENDTO)
                                data = uri
                                Log.d(TAG, "processNFC: $uri")
                            })
                        }
                        "Sp" -> {
                            Log.d(TAG, "processNFC: ${String(rec.type)}")
                        }
                    }
                }
            }
        }
    } // End of readNFCData

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun startUnityLayout() {

        val btPermissionsState = rememberMultiplePermissionsState(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.NFC
                )
            } else {
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.NFC
                )
            }
        ) // End of btPermissionsState

        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp),
                    onClick = {
                        for (i in btPermissionsState.permissions.indices) {
                            Log.d(
                                "SSAFY_PERMISSION",
                                "${btPermissionsState.permissions[i].permission}: ${btPermissionsState.permissions[i].status}"
                            )
                        }
                        if (btPermissionsState.allPermissionsGranted) {
                            val intent = Intent(this@MainActivity, UnityHolderActivity::class.java)
                            intent.putExtra("destination", doubleArrayOf(0.0, 0.0, 0.0))
                            startActivity(intent)
                        } else {
                            btPermissionsState.launchMultiplePermissionRequest()
                        }
                    }) {

                }
            }
        }
    }

} // End of MainActivity class
