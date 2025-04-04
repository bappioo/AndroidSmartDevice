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
import androidx.compose.runtime.mutableStateListOf
import fr.isen.picco.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import androidx.compose.runtime.mutableStateOf
import fr.isen.picco.androidsmartdevice.screen.ScanScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class ScanActivity : ComponentActivity() {
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private val scannedDevices = mutableStateListOf<String>()
    private val requestCodePermissions = 101
    private var isScanning by mutableStateOf(false)

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
                    isScanning = isScanning,            // Ajout du paramètre
                    onScanToggle = {
                        isScanning = !isScanning        // Mise à jour de l'état
                        startBLEScan(isScanning)
                    }
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
            val scanFilters = listOf<ScanFilter>()
            val scanSettings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()

            bluetoothLeScanner?.startScan(scanFilters, scanSettings, scanCallback)
        }
    }
}

