package fr.isen.picco.androidsmartdevice.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.picco.androidsmartdevice.R

@Composable
fun ScanScreen(
    scannedDevices: List<String>,
    isScanning: Boolean,
    onScanToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Scan BLE",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 40.dp)
        )

        Image(
            painter = painterResource(id = if (isScanning) R.drawable.ic_stop else R.drawable.ic_scan),
            contentDescription = "Scan BLE",
            modifier = Modifier
                .size(250.dp)
                .clickable(onClick = onScanToggle)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isScanning) "Scanning..." else "Scan arrêté",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        DeviceList(devices = scannedDevices)
    }
}

@Composable
private fun DeviceList(devices: List<String>) {
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