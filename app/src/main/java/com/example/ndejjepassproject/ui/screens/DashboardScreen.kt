package com.example.ndejjepassproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.ndejjepassproject.data.db.entities.ClearanceEntity
import com.example.ndejjepassproject.data.db.entities.PaymentEntity
import com.example.ndejjepassproject.data.db.entities.StudentEntity
import com.example.ndejjepassproject.ui.navigation.Screen
import com.example.ndejjepassproject.ui.theme.Mobile_Programming_group_7_ndejjePassProjectTheme
import com.example.ndejjepassproject.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(vm: DashboardViewModel, nav: NavController) {
    val s by vm.state.collectAsState()
    val student = s.student ?: return
    val clearance = s.clearance

    DashboardContent(
        student = student,
        clearance = clearance,
        payments = s.payments,
        onPaymentClick = { nav.navigate(Screen.Payment.route) }
    )
}

@Composable
fun DashboardContent(
    student: StudentEntity,
    clearance: ClearanceEntity?,
    payments: List<PaymentEntity>,
    onPaymentClick: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F6E56))
                .padding(20.dp)
        ) {
            Column {
                Text("Hello, ${student.name}", color = Color.White, fontSize = 18.sp)
                Text(student.regNumber, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Text(student.programName, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
        Card(Modifier.fillMaxWidth().padding(16.dp)) {
            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Clearance status")
                    Text("${clearance?.percentage ?: 0}%", fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(
                    progress = { (clearance?.percentage ?: 0) / 100f },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                Button(
                    onClick = { },
                    enabled = clearance?.isCleared == true,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (clearance?.isCleared == true) "View QR permit" else "QR permit locked")
                }
            }
        }
        Card(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Tuition fee", fontWeight = FontWeight.Medium)
                    Text("UGX 1,800,000", style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    if (clearance?.isCleared == true) "Paid" else "Tap to pay ->",
                    color = if (clearance?.isCleared == true) Color(0xFF0F6E56) else MaterialTheme.colorScheme.primary
                )
            }
        }
        LazyColumn(Modifier.padding(16.dp)) {
            items(payments, key = { it.id }) { payment ->
                PaymentHistoryItem(payment)
            }
        }
    }
}

@Composable
private fun PaymentHistoryItem(payment: PaymentEntity) {
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("UGX ${payment.amount.toLong()}", fontWeight = FontWeight.SemiBold)
                Text(payment.method.uppercase(), style = MaterialTheme.typography.bodySmall)
            }
            Text(payment.status.uppercase(), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    val sampleStudent = StudentEntity(
        name = "John Doe",
        regNumber = "2023/BSIT/001",
        programName = "Bachelor of Science in Information Technology",
        email = "john@example.com",
        passwordHash = ""
    )
    val sampleClearance = ClearanceEntity(
        studentId = 1,
        percentage = 75,
        isCleared = false
    )
    val samplePayments = listOf(
        PaymentEntity(id = 1, studentId = 1, amount = 500000.0, reference = "REF1", method = "Mobile Money", status = "approved"),
        PaymentEntity(id = 2, studentId = 1, amount = 300000.0, reference = "REF2", method = "Bank", status = "pending")
    )

    Mobile_Programming_group_7_ndejjePassProjectTheme {
        DashboardContent(
            student = sampleStudent,
            clearance = sampleClearance,
            payments = samplePayments,
            onPaymentClick = {}
        )
    }
}