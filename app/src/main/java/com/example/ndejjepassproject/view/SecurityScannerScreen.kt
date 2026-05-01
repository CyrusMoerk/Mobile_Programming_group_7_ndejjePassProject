package com.example.ndejjepassproject.view

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.example.ndejjepassproject.ui.theme.*

data class ScannedStudentInfo(
    val name: String,
    val regNumber: String,
    val programme: String,
    val percentage: Int,
    val nchePaid: Boolean,
    val isCleared: Boolean
)

@Composable
fun SecurityScannerScreen(
    onLogout: () -> Unit = {}
) {
    val headerGradient = Brush.verticalGradient(listOf(Green800, Green600))
    var scannedInfo by remember { mutableStateOf<ScannedStudentInfo?>(null) }
    var scanError   by remember { mutableStateOf<String?>(null) }

    // QR scanner launcher
    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            scanError = null
            // Parse the QR content
            val content = result.contents
            try {
                val lines = content.lines()
                val name       = lines.find { it.startsWith("NAME:") }
                    ?.removePrefix("NAME:") ?: "Unknown"
                val reg        = lines.find { it.startsWith("REG:") }
                    ?.removePrefix("REG:") ?: ""
                val programme  = lines.find { it.startsWith("PROGRAMME:") }
                    ?.removePrefix("PROGRAMME:") ?: ""
                val pctStr     = lines.find { it.startsWith("PERCENTAGE:") }
                    ?.removePrefix("PERCENTAGE:") ?: "0"
                val percentage = pctStr.trim().toIntOrNull() ?: 0

                scannedInfo = ScannedStudentInfo(
                    name        = name.trim(),
                    regNumber   = reg.trim(),
                    programme   = programme.trim(),
                    percentage  = percentage,
                    nchePaid    = true,
                    isCleared   = percentage >= 100
                )
            } catch (e: Exception) {
                scanError = "Could not read QR code. Please try again."
            }
        } else {
            scanError = "Scan cancelled."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Header ────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = headerGradient)
                .padding(top = 40.dp, bottom = 20.dp,
                    start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Security Scanner",
                        style = MaterialTheme.typography.titleLarge,
                        color = White)
                    Text("Scan student QR codes for clearance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Green100)
                }
                IconButton(onClick = onLogout) {
                    Icon(Icons.Filled.Logout, null, tint = White)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Scanner button ────────────────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val options = ScanOptions().apply {
                        setPrompt("Scan student QR code")
                        setBeepEnabled(true)
                        setOrientationLocked(false)
                        setBarcodeImageEnabled(false)
                    }
                    scanLauncher.launch(options)
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Filled.QrCodeScanner,
                        contentDescription = "Scan QR Code",
                        tint = Green600,
                        modifier = Modifier.size(80.dp)
                    )
                    Text("Tap to Scan QR Code",
                        style = MaterialTheme.typography.titleMedium,
                        color = Green800,
                        fontWeight = FontWeight.Bold)
                    Text("Point camera at student's QR code",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center)
                }
            }

            // ── Scan error ────────────────────────────────────
            if (scanError != null) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Amber50),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Warning, null,
                            tint = Amber600, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(scanError ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Amber600)
                    }
                }
            }

            // ── Scanned result ────────────────────────────────
            if (scannedInfo != null) {
                val info = scannedInfo?.let { info ->
                    val statusColor = when {
                        info.percentage >= 80 -> Green600
                        info.percentage >= 60 -> Amber600
                        else -> Red600
                    }
                    val statusBg = when {
                        info.percentage >= 80 -> Green50
                        info.percentage >= 60 -> Amber50
                        else -> Red50
                    }
                    val accessMessage = when {
                        info.percentage >= 80 -> "✓ ALLOW ENTRY"
                        info.percentage >= 60 -> "⚠ REGISTERED — Below 80%"
                        else -> "✗ DENY ENTRY — Below 60%"
                    }


                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = statusBg),
                        border = androidx.compose.foundation.BorderStroke(
                            2.dp, statusColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                accessMessage,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                            Divider(color = statusColor.copy(alpha = 0.3f))
                            SummaryRow("Name", info.name)
                            SummaryRow("Reg Number", info.regNumber)
                            SummaryRow("Programme", info.programme)
                            SummaryRow("Tuition Paid", "${info.percentage}%")
                            SummaryRow(
                                "NCHE Fee",
                                if (info.nchePaid) "Paid" else "NOT PAID"
                            )
                            Spacer(Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { (info.percentage / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp),
                                color = statusColor,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                            Text(
                                text = when {
                                    info.percentage >= 80 ->
                                        "Student meets the 80% threshold for lecture attendance."

                                    info.percentage >= 60 ->
                                        "Student is registered but below the 80% lecture threshold."

                                    else ->
                                        "Student has not reached the 60% registration threshold."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = statusColor
                            )
                            // Scan again button
                            OutlinedButton(
                                onClick = {
                                    scannedInfo = null
                                    val options = ScanOptions().apply {
                                        setPrompt("Scan student QR code")
                                        setBeepEnabled(true)
                                        setOrientationLocked(false)
                                    }
                                    scanLauncher.launch(options)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.QrCodeScanner, null,
                                    tint = Green600,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text("Scan Another Student", color = Green600)
                            }
                        }
                    }
                }
            } else if (scanError == null) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.PersonSearch, null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("No student scanned yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center)
                        Text("Tap the scanner above to scan a student's QR code",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SecurityScannerPreview() {
    NdejjeClearPassTheme {
        SecurityScannerScreen()
    }
}