package fr.isen.picco.androidsmartdevice

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.picco.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult

class ScanActivity : ComponentActivity() {
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private val scannedDevices = mutableStateListOf<String>()
    private val requestCodePermissions = 101

    private val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_ADMIN

        )
    } else {
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vérifier si le Bluetooth est disponible
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
        bluetoothAdapter = bluetoothManager?.adapter

        fun hasPermissions(): Boolean {
            return requiredPermissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }
        }

        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this, requiredPermissions, requestCodePermissions)
        }

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth non disponible sur cet appareil", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner



        setContent {
            AndroidSmartDeviceTheme {
                ScanScreen(
                    scannedDevices = scannedDevices,
                    onScanToggle = { startBLEScan(it) }
                )
            }
        }
    }

    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val deviceInfo = "${device.name ?: "Unknown"} - ${device.address}"
            if (!scannedDevices.contains(deviceInfo)) {
                scannedDevices.add(deviceInfo)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Toast.makeText(this@ScanActivity, "Erreur de scan: $errorCode", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startBLEScan(isScanning: Boolean) {
        bluetoothLeScanner?.stopScan(scanCallback)

        if (isScanning) {
            scannedDevices.clear()
            val scanCallback = object : ScanCallback() {
                override fun onScanResult(
                    callbackType: Int,
                    result: android.bluetooth.le.ScanResult?
                ) {
                    result?.device?.let { device ->
                        val deviceInfo = "${device.name ?: "Unknown"} - ${device.address}"
                        if (!scannedDevices.contains(deviceInfo)) {
                            scannedDevices.add(deviceInfo)
                        }
                    }
                }

                override fun onScanFailed(errorCode: Int) {
                    Toast.makeText(this@ScanActivity, "Erreur de scan: $errorCode", Toast.LENGTH_SHORT).show()
                }
            }

            val scanFilters = listOf<ScanFilter>()
            val scanSettings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()

            bluetoothLeScanner?.startScan(scanFilters, scanSettings, scanCallback)
        } else {
            bluetoothLeScanner?.stopScan(scanCallback)
        }

    }
}

@Composable
fun ScanScreen(scannedDevices: List<String>, onScanToggle: (Boolean) -> Unit) {
    var isScanning by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Scan BLE",
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 36.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier.padding(top = 40.dp)
        )

        Image(
            painter = painterResource(id = if (isScanning) R.drawable.ic_stop else R.drawable.ic_scan),
            contentDescription = "Scan BLE",
            modifier = Modifier
                .size(250.dp)
                .clickable {
                    isScanning = !isScanning
                    onScanToggle(isScanning)
                }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isScanning) "Scanning..." else "Scan arrêté",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(scannedDevices) { device ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = device,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}