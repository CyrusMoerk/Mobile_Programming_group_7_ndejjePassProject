package com.example.ndejjepassproject.view

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjepassproject.ui.theme.*
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun PaymentSubmissionScreen(
    balanceDue: Double = 720_000.0,
    onSubmit: (method: String, reference: String, amount: String) -> Unit = { _, _, _ -> },
    onBack: () -> Unit = {}
) {
    var selectedMethod  by remember { mutableStateOf("Bank Transfer") }
    var referenceCode   by remember { mutableStateOf("") }
    var amount          by remember { mutableStateOf("") }
    var methodMenuOpen  by remember { mutableStateOf(false) }
    var submitted       by remember { mutableStateOf(false) }

    val paymentMethods = listOf("Bank Transfer", "MTN Mobile Money", "Airtel Money")
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
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                        tint = White)
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
                // ── Success state ─────────────────────────────
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Green50),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null,
                            tint = Green600, modifier = Modifier.size(56.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("Reference Submitted",
                            style = MaterialTheme.typography.titleLarge,
                            color = Green800)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Your payment reference has been submitted and is awaiting " +
                                    "confirmation from the Accounts Office. Your balance will " +
                                    "update once approved.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Green600
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = { submitted = false },
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
                        Icon(Icons.Filled.Info, contentDescription = null,
                            tint = Green600, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Pay at any bank or via Mobile Money, then enter your " +
                                    "reference code below. The Accounts Office will confirm it.",
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
                                "MTN Mobile Money"  -> Icons.Filled.PhoneAndroid
                                "Airtel Money"      -> Icons.Filled.PhoneAndroid
                                else                -> Icons.Filled.AccountBalance
                            },
                            contentDescription = null,
                            tint = Green600
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(selectedMethod,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.primary)
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = null,
                            tint = Green600)
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

                // ── Reference code ────────────────────────────
                OutlinedTextField(
                    value = referenceCode,
                    onValueChange = { referenceCode = it.uppercase() },
                    label = { Text("Bank / Mobile Money Reference Code") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text("e.g. TXN202504280001") },
                    leadingIcon = {
                        Icon(Icons.Filled.Receipt, contentDescription = null,
                            tint = Green600)
                    }
                )

                // ── Warning ───────────────────────────────────
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Amber50),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Warning, contentDescription = null,
                            tint = Amber600, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Your balance will only update after the Accounts Office " +
                                    "confirms your reference. This may take up to 24 hours.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Amber600
                        )
                    }
                }

                // ── Submit button ─────────────────────────────
                Button(
                    onClick = {
                        if (referenceCode.isNotBlank() && amount.isNotBlank()) {
                            onSubmit(selectedMethod, referenceCode, amount)
                            submitted = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = referenceCode.isNotBlank() && amount.isNotBlank()
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Submit Payment Reference",
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