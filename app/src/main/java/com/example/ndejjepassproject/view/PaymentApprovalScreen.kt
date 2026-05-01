package com.example.ndejjepassproject.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ndejjepassproject.ui.theme.*
import com.example.ndejjepassproject.viewmodel.AdminViewModel
import java.io.File

data class ReceiptItem(
    val id: String,
    val paymentId: String,
    val studentId: String,
    val studentName: String,
    val regNumber: String,
    val method: String,
    val amount: String,
    val filePath: String,
    val fileType: String,
    val submittedAt: String,
    val status: String = "PENDING"
)

@Composable
fun PaymentApprovalScreen(
    viewModel: AdminViewModel,
    onBack: () -> Unit = {}
) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // 🔥 Convert DB → UI model
    val receipts = state.payments.map {
        ReceiptItem(
            id = it.id.toString(),
            paymentId = it.id.toString(),
            studentId = it.studentId.toString(),
            studentName = "Student ${it.studentId}",
            regNumber = "N/A",
            method = it.method,
            amount = it.amount.toString(),
            filePath = it.filePath ?: "",
            fileType = it.fileType ?: "",
            submittedAt = it.submittedAt.toString(),
            status = it.status.uppercase()
        )
    }

    val localReceipts = remember(receipts) {
        receipts.toMutableStateList()
    }

    val headerGradient = Brush.verticalGradient(listOf(Green800, Green600))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerGradient)
                .padding(top = 40.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, null, tint = White)
                }
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("Payment Approval", style = MaterialTheme.typography.titleLarge, color = White)
                    Text("${localReceipts.count { it.status == "PENDING" }} pending receipts",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Green100)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(localReceipts, key = { it.id }) { receipt ->

                ReceiptCard(
                    receipt = receipt,
                    context = context,
                    onApprove = {
                        viewModel.approve(
                            receipt.id.toInt(),
                            receipt.paymentId.toInt(),
                            receipt.studentId.toInt()
                        )
                    },
                    onReject = {
                        viewModel.reject(
                            receipt.id.toInt(),
                            receipt.paymentId.toInt()
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ReceiptCard(
    receipt: ReceiptItem,
    context: Context,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {

    var showImage by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(receipt.studentName, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(10.dp))

            Text("Method: ${receipt.method}")
            Text("Amount: UGX ${receipt.amount}")

            Spacer(Modifier.height(10.dp))

            // 🔥 FILE VIEW
            if (receipt.filePath.isNotEmpty()) {

                Row {

                    Button(
                        onClick = {
                            if (receipt.fileType == "pdf") {
                                val file = File(receipt.filePath)

                                if (!file.exists()) {
                                    android.widget.Toast.makeText(
                                        context,
                                        "File not found",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }

                                val uri = androidx.core.content.FileProvider.getUriForFile(
                                    context,
                                    context.packageName + ".fileprovider",
                                    file
                                )

                                val mimeType = if (receipt.fileType == "pdf") {
                                    "application/pdf"
                                } else {
                                    "image/*"
                                }

                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(uri, mimeType)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }

                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    android.util.Log.e("PDF_ERROR", "Cannot open file", e)

                                    android.widget.Toast.makeText(
                                        context,
                                        "Install a PDF viewer (Google Drive or Adobe Acrobat)",
                                        android.widget.Toast.LENGTH_LONG
                                    ).show()
                                }

                            } else {
                                showImage = !showImage
                            }
                        }
                    ) {
                        Text("View Receipt")
                    }

                    if (receipt.fileType == "image" && showImage) {
                        AsyncImage(
                            model = Uri.parse(receipt.filePath),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(200.dp)
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                Row {

                    Button(onClick = onApprove) {
                        Text("Approve")
                    }

                    Spacer(Modifier.width(10.dp))

                    Button(onClick = onReject) {
                        Text("Reject")
                    }
                }
            }
        }
    }
}