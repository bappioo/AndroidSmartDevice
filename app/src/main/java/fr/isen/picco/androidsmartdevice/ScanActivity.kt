package fr.isen.picco.androidsmartdevice

import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.picco.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class ScanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidSmartDeviceTheme {
                ScanScreen()
            }
        }
    }
}

@Composable
fun ScanScreen() {
    var isScanning by remember { mutableStateOf(false) }
    var devices by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titre
        Text(text = "Scan BLE", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Icône de scan (cliquable)
        Image(
            painter = painterResource(
                id = if (isScanning) R.drawable.ic_stop else R.drawable.ic_scan
            ),
            contentDescription = "Scan BLE",
            modifier = Modifier
                .size(100.dp)
                .clickable {
                    isScanning = !isScanning
                    devices = if (isScanning) {
                        // Simuler des appareils détectés
                        listOf("Device 1", "Device 2", "Device 3")
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

@Preview(showBackground = true)
@Composable
fun PreviewScanScreen() {
    AndroidSmartDeviceTheme {
        ScanScreen()
    }
}