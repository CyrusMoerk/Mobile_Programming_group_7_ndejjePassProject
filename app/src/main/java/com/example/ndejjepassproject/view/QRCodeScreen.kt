package com.example.ndejjepassproject.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ndejjepassproject.ui.theme.*

@Composable
fun QRCodeScreen(
    studentName: String = "NABAYA NESTROY",
    regNumber: String = "24/2/306/D/053",
    programme: String = "BSc. Computer Science",
    clearanceStatus: String = "60% — Partial",
    isCleared: Boolean = false,
    onBack: () -> Unit = {}
) {
    val headerGradient = Brush.verticalGradient(listOf(Green800, Green600))

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back",
                        tint = White)
                }
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("My QR Code",
                        style = MaterialTheme.typography.titleLarge,
                        color = White)
                    Text("Show this to security staff at block entrances",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Green100)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Student avatar ────────────────────────────────
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Green400)
                    .border(3.dp, Green600, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (studentName.isNotEmpty()) studentName.first().toString() else "?",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }

            Text(studentName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground)
            Text(regNumber,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            // ── QR code placeholder ───────────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.size(220.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // QR pattern placeholder
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            Icons.Filled.QrCode2,
                            contentDescription = "QR Code",
                            tint = Green800,
                            modifier = Modifier.size(140.dp)
                        )
                        Text(
                            text = "QR code generates\nafter Emmanuel\nwires the ViewModel",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // ── Clearance badge ───────────────────────────────
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isCleared) Green50 else Amber50,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, if (isCleared) Green400 else Amber600)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isCleared) Icons.Filled.CheckCircle
                        else Icons.Filled.PendingActions,
                        contentDescription = null,
                        tint = if (isCleared) Green600 else Amber600,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = clearanceStatus,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = if (isCleared) Green800 else Amber600
                    )
                }
            }

            // ── Info card ─────────────────────────────────────
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Green50),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    QRInfoRow(label = "Programme", value = programme)
                    Divider(color = Green100)
                    QRInfoRow(label = "Clearance", value = clearanceStatus)
                    Divider(color = Green100)
                    QRInfoRow(label = "NCHE Fee", value = "Paid")
                }
            }

            Text(
                text = "Security staff scan this QR code to verify your identity " +
                        "and clearance status at block entrances.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun QRInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Green800)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QRCodePreview() {
    NdejjeClearPassTheme {
        QRCodeScreen()
    }
}