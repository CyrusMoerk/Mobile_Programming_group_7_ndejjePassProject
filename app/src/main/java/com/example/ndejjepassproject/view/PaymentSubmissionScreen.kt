package com.example.ndejjepassproject.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ndejjepassproject.ui.theme.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalContext
import java.io.File

@Composable
fun PaymentSubmissionScreen(
    balanceDue: Double = 720_000.0,
    onSubmit: (method: String, amount: String, filePath: String, fileType: String) -> Unit = { _, _, _, _ -> },
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedMethod by remember { mutableStateOf("Bank Transfer") }
    var amount         by remember { mutableStateOf("") }
    var methodMenuOpen by remember { mutableStateOf(false) }
    var submitted      by remember { mutableStateOf(false) }
    var receiptUri     by remember { mutableStateOf<Uri?>(null) }
    var fileType       by remember { mutableStateOf("") }

    val paymentMethods = listOf("Bank Transfer", "MTN Mobile Money", "Airtel Money")
    val headerGradient = Brush.verticalGradient(listOf(Green800, Green600))

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            receiptUri = it
            fileType = "image"
        }
    }

    val pdfPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            receiptUri = it
            fileType = "pdf"
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, null, tint = White)
                }
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("Submit Payment",
                        style = MaterialTheme.typography.titleLarge,
                        color = White)
                    Text("Balance due: UGX ${String.format("%,.0f", balanceDue)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Green100)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (submitted) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Green50),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.CheckCircle, null,
                            tint = Green600, modifier = Modifier.size(56.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("Payment Submitted",
                            style = MaterialTheme.typography.titleLarge,
                            color = Green800)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Your receipt has been submitted and is awaiting " +
                                    "approval from the Accounts Office. Your balance " +
                                    "will update once approved.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Green600
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = {
                                submitted = false
                                receiptUri = null
                                amount = ""
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Submit Another", color = Green600)
                        }
                    }
                }
            } else {

                // ── Info banner ───────────────────────────────
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Green50),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Info, null,
                            tint = Green600, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Pay at any bank or via Mobile Money, then upload " +
                                    "your receipt below. The Accounts Office will confirm it.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Green800
                        )
                    }
                }

                // ── Payment method ────────────────────────────
                Text("Payment Method",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { methodMenuOpen = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = when (selectedMethod) {
                                "MTN Mobile Money" -> Icons.Filled.PhoneAndroid
                                "Airtel Money"     -> Icons.Filled.PhoneAndroid
                                else               -> Icons.Filled.AccountBalance
                            },
                            contentDescription = null,
                            tint = Green600
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(selectedMethod,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.primary)
                        Icon(Icons.Filled.ArrowDropDown, null, tint = Green600)
                    }
                    DropdownMenu(
                        expanded = methodMenuOpen,
                        onDismissRequest = { methodMenuOpen = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        paymentMethods.forEach { method ->
                            DropdownMenuItem(
                                text = { Text(method) },
                                onClick = {
                                    selectedMethod = method
                                    methodMenuOpen = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (method == "Bank Transfer")
                                            Icons.Filled.AccountBalance
                                        else Icons.Filled.PhoneAndroid,
                                        contentDescription = null,
                                        tint = Green600
                                    )
                                }
                            )
                        }
                    }
                }

                // ── Amount ────────────────────────────────────
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount Paid (UGX)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Text("UGX",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Green600,
                            modifier = Modifier.padding(start = 12.dp))
                    }
                )

                // ── Receipt upload ────────────────────────────
                Text("Upload Receipt",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)

                if (receiptUri != null) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Green50),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (fileType == "image") {
                                AsyncImage(
                                    model = receiptUri,
                                    contentDescription = "Receipt",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            } else {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Filled.PictureAsPdf, null,
                                        tint = Red600,
                                        modifier = Modifier.size(32.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("PDF receipt selected",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Green800)
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            TextButton(onClick = { receiptUri = null }) {
                                Text("Remove receipt", color = Red600)
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { imagePicker.launch("image/*") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Filled.Image, null, tint = Green600,
                                modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Upload Image", color = Green600)
                        }
                        OutlinedButton(
                            onClick = { pdfPicker.launch("application/pdf") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Filled.PictureAsPdf, null, tint = Red600,
                                modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Upload PDF", color = Red600)
                        }
                    }
                }

                // ── Warning ───────────────────────────────────
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
                        Text(
                            "Your balance will only update after the Accounts " +
                                    "Office reviews and approves your receipt.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Amber600
                        )
                    }
                }

                // ── Submit button ─────────────────────────────
                Button(
                    onClick = {

                        if (receiptUri != null && amount.isNotBlank()) {

                            val uri = receiptUri!!

                            val inputStream = context.contentResolver.openInputStream(uri)

                            val fileName = "receipt_${System.currentTimeMillis()}.pdf"

                            val file = File(context.getExternalFilesDir(null), fileName)

                            try {
                                inputStream?.use { input ->
                                    file.outputStream().use { output ->
                                        input.copyTo(output)
                                    }
                                }

                                onSubmit(
                                    selectedMethod,
                                    amount,
                                    file.absolutePath,   // ✅ REAL FILE PATH NOW
                                    fileType
                                )

                                submitted = true

                            } catch (e: Exception) {
                                android.util.Log.e("FILE_SAVE", "Failed to save file", e)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = receiptUri != null && amount.isNotBlank()
                ) {
                    Icon(Icons.Filled.Upload, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Submit Payment & Receipt",
                        style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentSubmissionPreview() {
    NdejjeClearPassTheme {
        PaymentSubmissionScreen()
    }
}