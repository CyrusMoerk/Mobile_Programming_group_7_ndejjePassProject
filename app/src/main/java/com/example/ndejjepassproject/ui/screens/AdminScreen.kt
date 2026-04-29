package com.example.ndejjepassproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ndejjepassproject.data.db.entities.PaymentEntity
import com.example.ndejjepassproject.ui.theme.Mobile_Programming_group_7_ndejjePassProjectTheme
import com.example.ndejjepassproject.viewmodel.AdminViewModel

@Composable
fun AdminScreen(vm: AdminViewModel) {
    val s by vm.state.collectAsState()
    AdminScreen(
        pendingPayments = s.pendingPayments,
        onApprove = { id, sid -> vm.approve(id, sid) },
        onReject = { id, reason -> vm.reject(id, reason) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminScreen(
    pendingPayments: List<PaymentEntity>,
    onApprove: (Int, Int) -> Unit,
    onReject: (Int, String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Pending payments (${pendingPayments.size})") })
        if (pendingPayments.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No pending payments", color = Color.Gray)
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(12.dp)) {
                items(pendingPayments, key = { it.id }) { payment ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Student ID: ${payment.studentId}", fontWeight = FontWeight.Medium)
                            Text("Amount: UGX ${payment.amount.toLong()}")
                            Text("Method: ${payment.method.uppercase()}")
                            Text("Ref: ${payment.reference}", fontSize = 12.sp, color = Color.Gray)
                            Spacer(Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { onApprove(payment.id, payment.studentId) }) {
                                    Text("Approve")
                                }
                                OutlinedButton(onClick = { onReject(payment.id, "Invalid reference") }) {
                                    Text("Reject")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminScreenPreview() {
    Mobile_Programming_group_7_ndejjePassProjectTheme {
        AdminScreen(
            pendingPayments = listOf(
                PaymentEntity(id = 1, studentId = 101, amount = 50000.0, reference = "REF123", method = "momo"),
                PaymentEntity(id = 2, studentId = 102, amount = 75000.0, reference = "REF456", method = "bank")
            ),
            onApprove = { _, _ -> },
            onReject = { _, _ -> }
        )
    }
}
