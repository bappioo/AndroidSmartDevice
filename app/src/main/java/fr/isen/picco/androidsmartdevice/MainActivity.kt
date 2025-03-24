package fr.isen.picco.androidsmartdevice

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import fr.isen.picco.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSmartDeviceTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppIntroScreen(
                        modifier = Modifier.padding(innerPadding),
                        onStartScan = {
                            val intent = Intent(this, ScanActivity::class.java)
                            startActivity(intent)

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppIntroScreen(
    modifier: Modifier = Modifier,
    onStartScan: () -> Unit
) {
    val context = LocalContext.current  // Récupérer le contexte

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Android Smart Device",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Icon",
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Bienvenue dans votre application Android Smart Device, appuyer sur 'Lancer le scan'.",
            fontSize = 18.sp,
            fontWeight = FontWeight.W900
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Cette application permet de scanner les appareils BLE à proximité.",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val intent = Intent(context, ScanActivity::class.java)
            context.startActivity(intent) // Utiliser `context.startActivity`
        }) {
            Text("Lancer le scan")
        }
    }
}