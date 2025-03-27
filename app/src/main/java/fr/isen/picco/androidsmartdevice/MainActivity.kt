package fr.isen.picco.androidsmartdevice

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import fr.isen.picco.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
import fr.isen.picco.androidsmartdevice.screen.AppIntroScreen

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
