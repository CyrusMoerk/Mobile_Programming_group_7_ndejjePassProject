package com.example.ndejjepassproject.view

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.example.ndejjepassproject.ui.theme.*

fun generateQRBitmap(content: String, size: Int = 512): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap.setPixel(
                x, y,
                if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            )
        }
    }
    return bitmap
}

@Composable
fun QRCodeScreen(
    studentName: String = "Nakato Sarah",
    regNumber: String = "24/2/306/D/001",
    programme: String = "BSc. Computer Science",
    clearanceStatus: String = "60% — Partial",
    percentage: Int = 60,
    isCleared: Boolean = false,
    onBack: () -> Unit = {}
) {
    val headerGradient = androidx.compose.ui.graphics.Brush.verticalGradient(
        listOf(Green800, Green600)
    )

    // Build QR content
    val qrContent = """
        NAME:$studentName
        REG:$regNumber
        PROGRAMME:$programme
        CLEARANCE:$clearanceStatus
        PERCENTAGE:$percentage
    """.trimIndent()

    val qrBitmap = remember(qrContent) {
        generateQRBitmap(qrContent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = headerGradient)
                .padding(top = 40.dp, bottom = 20.dp,
                    start = 16.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, null, tint = White)
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
            // Avatar
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Green400)
                    .border(3.dp, Green600, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (studentName.isNotEmpty())
                        studentName.first().toString() else "?",
                    fontSize = 30.sp,
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

            // Real QR code
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.size(220.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "Student QR Code",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Status badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = when {
                    percentage >= 100 -> Green50
                    percentage >= 60  -> Amber50
                    else              -> Red50
                },
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, when {
                        percentage >= 100 -> Green400
                        percentage >= 60  -> Amber600
                        else              -> Red600
                    }
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when {
                            percentage >= 100 -> Icons.Filled.CheckCircle
                            percentage >= 60  -> Icons.Filled.PendingActions
                            else              -> Icons.Filled.Cancel
                        },
                        contentDescription = null,
                        tint = when {
                            percentage >= 100 -> Green600
                            percentage >= 60  -> Amber600
                            else              -> Red600
                        },
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = clearanceStatus,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = when {
                            percentage >= 100 -> Green800
                            percentage >= 60  -> Amber600
                            else              -> Red600
                        }
                    )
                }
            }

            // Info card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Green50),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    QRInfoRow("Programme", programme)
                    Divider(color = Green100)
                    QRInfoRow("Clearance", clearanceStatus)
                }
            }

            Text(
                "Security staff scan this QR code to verify your " +
                        "identity and clearance at block entrances.",
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