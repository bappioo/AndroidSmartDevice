package fr.isen.picco.androidsmartdevice.screen

import fr.isen.picco.androidsmartdevice.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class BluetoothScreen {

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothControlScreen(
    name: String,
    address: String,
    rssi: Int,
    onBack: () -> Unit,
    onConnectClick: () -> Unit,
    connectionStatus: String,
    isConnected: Boolean,
    ledStates: List<Boolean>,
    onLedToggle: (Int) -> Unit,
    isSubscribedButton1: Boolean,
    isSubscribedButton3: Boolean,
    onSubscribeToggleButton1: (Boolean) -> Unit,
    onSubscribeToggleButton3: (Boolean) -> Unit,
    counterButton1: Int,
    counterButton3: Int,
    onResetCounter: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contrôle Bluetooth") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = "Retour"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Informations sur l'appareil
            DeviceInfoSection(name, address, rssi)

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton de connexion
            Button(onClick = onConnectClick) {
                Text(if (isConnected) "Déconnecter" else "Se connecter")
            }

            Text(
                text = connectionStatus,
                modifier = Modifier.padding(8.dp),
                color = if (isConnected) Color.Green else Color.Red
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Contrôle des LEDs
            Text(
                text = "Contrôle des LEDs",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // LED 1
                LedControl(
                    index = 0,
                    isOn = ledStates[0],
                    onToggle = onLedToggle
                )

                // LED 2
                LedControl(
                    index = 1,
                    isOn = ledStates[1],
                    onToggle = onLedToggle
                )

                // LED 3
                LedControl(
                    index = 2,
                    isOn = ledStates[2],
                    onToggle = onLedToggle
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Compteurs de boutons
            CountersSection(
                isSubscribedButton1 = isSubscribedButton1,
                isSubscribedButton3 = isSubscribedButton3,
                onSubscribeToggleButton1 = onSubscribeToggleButton1,
                onSubscribeToggleButton3 = onSubscribeToggleButton3,
                counterButton1 = counterButton1,
                counterButton3 = counterButton3,
                onResetCounter = onResetCounter
            )
        }
    }
}

@Composable
fun DeviceInfoSection(name: String, address: String, rssi: Int) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Appareil: $name",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(text = "Adresse: $address")
            Text(text = "Signal: $rssi dBm")
        }
    }
}

@Composable
fun LedControl(index: Int, isOn: Boolean, onToggle: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageRes = if (isOn) R.drawable.led_on else R.drawable.led_off

        IconButton(
            onClick = { onToggle(index) },
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "LED ${index + 1}",
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = "LED ${index + 1}",
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = if (isOn) "ON" else "OFF",
            color = if (isOn) Color.Green else Color.Gray
        )
    }
}

@Composable
fun CountersSection(
    isSubscribedButton1: Boolean,
    isSubscribedButton3: Boolean,
    onSubscribeToggleButton1: (Boolean) -> Unit,
    onSubscribeToggleButton3: (Boolean) -> Unit,
    counterButton1: Int,
    counterButton3: Int,
    onResetCounter: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Compteurs de boutons",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Bouton 1
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Bouton 1: $counterButton1", modifier = Modifier.weight(1f))
                Switch(
                    checked = isSubscribedButton1,
                    onCheckedChange = onSubscribeToggleButton1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bouton 3
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Bouton 3: $counterButton3", modifier = Modifier.weight(1f))
                Switch(
                    checked = isSubscribedButton3,
                    onCheckedChange = onSubscribeToggleButton3
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onResetCounter,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Réinitialiser")
            }
        }
    }
}

}
