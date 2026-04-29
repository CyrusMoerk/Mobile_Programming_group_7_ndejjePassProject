package com.example.ndejjepassproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ndejjepassproject.ui.theme.Mobile_Programming_group_7_ndejjePassProjectTheme
import com.example.ndejjepassproject.viewmodel.PaymentUiState
import com.example.ndejjepassproject.viewmodel.PaymentViewModel

@Composable
fun PaymentScreen(vm: PaymentViewModel, nav: NavController) {
    val s by vm.state.collectAsState()

    LaunchedEffect(s.isSuccess) {
        if (s.isSuccess) {
            nav.popBackStack()
            vm.reset()
        }
    }

    PaymentContent(
        s = s,
        onMethodSelected = vm::onMethodSelected,
        onPhoneChanged = vm::onPhoneChanged,
        onRefChanged = vm::onRefChanged,
        onAmountChanged = vm::onAmountChanged,
        submitPayment = vm::submitPayment
    )
}

@Composable
fun PaymentContent(
    s: PaymentUiState,
    onMethodSelected: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onRefChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    submitPayment: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Pay tuition fee", style = MaterialTheme.typography.titleLarge)
        Text(
            "UGX 1,800,000",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF0F6E56)
        )
        Spacer(Modifier.height(20.dp))
        Text("Payment method", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("mtn" to "MTN MoMo", "airtel" to "Airtel", "bank" to "Bank").forEach { (id, label) ->
                FilterChip(
                    selected = s.method == id,
                    onClick = { onMethodSelected(id) },
                    label = { Text(label) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        if (s.method != "bank") {
            OutlinedTextField(
                value = s.phone,
                onValueChange = onPhoneChanged,
                label = { Text(if (s.method == "mtn") "MTN number" else "Airtel number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
        } else {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Stanbic Bank Uganda", fontWeight = FontWeight.Bold)
                    Text("Account: 9030005812345")
                    Text("Name: Ndejje University")
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = s.reference,
                onValueChange = onRefChanged,
                label = { Text("Bank reference number") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = s.amount,
            onValueChange = onAmountChanged,
            label = { Text("Amount (UGX)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        s.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp) }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = submitPayment,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            enabled = !s.isProcessing
        ) {
            if (s.isProcessing) {
                CircularProgressIndicator(Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(if (s.method != "bank") "Waiting for PIN approval..." else "Submitting...")
            } else {
                Text("Pay now")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    Mobile_Programming_group_7_ndejjePassProjectTheme {
        PaymentContent(
            s = PaymentUiState(
                method = "mtn",
                phone = "0770000000",
                amount = "1800000",
                isProcessing = false
            ),
            onMethodSelected = {},
            onPhoneChanged = {},
            onRefChanged = {},
            onAmountChanged = {},
            submitPayment = {}
        )
    }
}
