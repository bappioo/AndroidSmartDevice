package fr.isen.picco.androidsmartdevice

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import fr.isen.picco.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class ScanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vérifier si le Bluetooth est disponible
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

        if (bluetoothAdapter == null) {
            // Afficher une erreur si Bluetooth non disponible
            Toast.makeText(this, "Bluetooth non disponible sur cet appareil", Toast.LENGTH_LONG).show()
            finish() // Fermer l'activité
            return
        }

        // Vérifier si le Bluetooth est activé
        if (!bluetoothAdapter.isEnabled) {
            // Demander à l'utilisateur d'activer le Bluetooth
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            startActivity(enableBtIntent)
        }

        setContent {
            AndroidSmartDeviceTheme {
                ScanScreen(bluetoothAdapter)
            }
        }
    }
}

@Composable
fun ScanScreen(bluetoothAdapter: BluetoothAdapter) {
    var isScanning by remember { mutableStateOf(false) }
    var devices by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titre
        Text(
            text = "Scan BLE",
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 36.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier.padding(top = 40.dp)
        )

        // Icône de scan (cliquable)
        Image(
            painter = painterResource(
                id = if (isScanning) R.drawable.ic_stop else R.drawable.ic_scan
            ),
            contentDescription = "Scan BLE",
            modifier = Modifier
                .size(250.dp)
                .clickable {
                    isScanning = !isScanning
                    devices = if (isScanning) {
                        // Vérifier si le Bluetooth est activé avant de scanner
                        if (bluetoothAdapter.isEnabled) {
                            listOf("Device 1", "Device 2", "Device 3") // Simuler des appareils détectés
                        } else {
                            listOf() // Pas d'appareils détectés si Bluetooth éteint
                        }
                    } else {
                        listOf()
                    }
                }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Statut du scan
        Text(
            text = if (isScanning) "Scanning..." else "Scan arrêté",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Liste des appareils détectés
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(devices) { device ->
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