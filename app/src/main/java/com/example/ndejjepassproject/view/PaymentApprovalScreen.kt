package com.example.ndejjepassproject.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjepassproject.ui.theme.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.LaunchedEffect

data class PaymentReference(
    val id: String,
    val studentName: String,
    val regNumber: String,
    val method: String,
    val reference: String,
    val amount: String,
    val submittedAt: String,
    val status: String = "PENDING"
)

@Composable
fun PaymentApprovalScreen(
    payments: List<PaymentReference> = listOf(
        PaymentReference("1", "Nakato Sarah", "24/2/306/D/001",
            "Bank Transfer", "TXN202504280001", "900,000",
            "28 Apr 2026, 09:14 AM"),
        PaymentReference("2", "Okello Brian", "24/2/306/D/002",
            "MTN Mobile Money", "MOMO8823771", "500,000",
            "28 Apr 2026, 10:02 AM"),
        PaymentReference("3", "Namukasa Fatuma", "24/2/306/D/003",
            "Airtel Money", "AIR992341", "1,800,000",
            "27 Apr 2026, 03:45 PM", "APPROVED"),
    ),
    onApprove: (String) -> Unit = {},
    onReject: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val headerGradient = Brush.verticalGradient(listOf(Green800, Green600))
    val localPayments  = remember { payments.toMutableStateList() }

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
                    Text("Payment Approval",
                        style = MaterialTheme.typography.titleLarge,
                        color = White)
                    Text("${localPayments.count { it.status == "PENDING" }} pending references",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Green100)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatMiniCard(
                        label = "Pending",
                        value = localPayments.count { it.status == "PENDING" }.toString(),
                        color = Amber600,
                        modifier = Modifier.weight(1f)
                    )
                    StatMiniCard(
                        label = "Approved",
                        value = localPayments.count { it.status == "APPROVED" }.toString(),
                        color = Green600,
                        modifier = Modifier.weight(1f)
                    )
                    StatMiniCard(
                        label = "Rejected",
                        value = localPayments.count { it.status == "REJECTED" }.toString(),
                        color = Red600,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            items(localPayments, key = { it.id }) { payment ->
                AnimatedPaymentCard(
                    payment = payment,
                    onApprove = {
                        val index = localPayments.indexOfFirst { it.id == payment.id }
                        if (index >= 0)
                            localPayments[index] = payment.copy(status = "APPROVED")
                        onApprove(payment.id)
                    },
                    onReject = {
                        val index = localPayments.indexOfFirst { it.id == payment.id }
                        if (index >= 0)
                            localPayments[index] = payment.copy(status = "REJECTED")
                        onReject(payment.id)
                    }
                )
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun StatMiniCard(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color)
            Text(label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun AnimatedPaymentCard(
    payment: PaymentReference,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    // Tracks whether this card is visible
    var visible by remember { mutableStateOf(false) }

    // Trigger entrance animation when card first appears
    LaunchedEffect(payment.id) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(400)) +
                slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ),
        exit = fadeOut(animationSpec = tween(300)) +
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
    ) {
        PaymentReferenceCard(
            payment = payment,
            onApprove = {
                // Slide out before updating state
                visible = false
                onApprove()
            },
            onReject = {
                visible = false
                onReject()
            }
        )
    }
}

@Composable
fun PaymentReferenceCard(
    payment: PaymentReference,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    val statusColor = when (payment.status) {
        "APPROVED" -> Green600
        "REJECTED" -> Red600
        else       -> Amber600
    }
    val statusBg = when (payment.status) {
        "APPROVED" -> Green50
        "REJECTED" -> Red50
        else       -> Amber50
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(payment.studentName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface)
                    Text(payment.regNumber,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusBg
                ) {
                    Text(
                        payment.status,
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            Divider(color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    ApprovalField("Method", payment.method)
                    Spacer(Modifier.height(4.dp))
                    ApprovalField("Reference", payment.reference)
                }
                Column(modifier = Modifier.weight(1f)) {
                    ApprovalField("Amount", "UGX ${payment.amount}")
                    Spacer(Modifier.height(4.dp))
                    ApprovalField("Submitted", payment.submittedAt)
                }
            }

            if (payment.status == "PENDING") {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Red600)
                    ) {
                        Icon(Icons.Filled.Close, null,
                            modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Reject")
                    }
                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green600)
                    ) {
                        Icon(Icons.Filled.Check, null,
                            modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Approve")
                    }
                }
            }
        }
    }
}

@Composable
fun ApprovalField(label: String, value: String) {
    Column {
        Text(label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentApprovalPreview() {
    NdejjeClearPassTheme {
        PaymentApprovalScreen()
    }
}