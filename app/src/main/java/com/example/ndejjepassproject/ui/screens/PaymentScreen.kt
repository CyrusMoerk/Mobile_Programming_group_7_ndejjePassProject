// ui/screens/PaymentScreen.kt
// Three method buttons (MTN / Airtel / Bank) plus dynamic input fields.
// When isProcessing = true, shows a spinner and disables the button.
// When isSuccess = true, navigates back to dashboard automatically.

@Composable
fun PaymentScreen(vm: PaymentViewModel, nav: NavController) {
    val s by vm.state.collectAsStateWithLifecycle()

    // Navigate back automatically on success
    LaunchedEffect(s.isSuccess) {
        if (s.isSuccess) { nav.popBackStack(); vm.reset() }
    }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Pay tuition fee", style = MaterialTheme.typography.titleLarge)
        Text("UGX 1,800,000", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF0F6E56))
        Spacer(Modifier.height(20.dp))
        Text("Payment method", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        // Method selector buttons
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("mtn" to "MTN MoMo", "airtel" to "Airtel", "bank" to "Bank").forEach { (id, label) ->
                FilterChip(
                    selected = s.method == id,
                    onClick = { vm.onMethodSelected(id) },
                    label = { Text(label) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        // Dynamic fields based on method
        if (s.method != "bank") {
            OutlinedTextField(
                value = s.phone, onValueChange = vm::onPhoneChanged,
                label = { Text(if (s.method == "mtn") "MTN number" else "Airtel number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
        } else {
            // Bank transfer — show account details + reference input
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Stanbic Bank Uganda", fontWeight = FontWeight.Bold)
                    Text("Account: 9030005812345")
                    Text("Name: Ndejje University")
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = s.reference, onValueChange = vm::onRefChanged,
                label = { Text("Bank reference number") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = s.amount, onValueChange = vm::onAmountChanged,
            label = { Text("Amount (UGX)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        s.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp) }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = vm::submitPayment,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            enabled = !s.isProcessing
        ) {
            if (s.isProcessing) {
                CircularProgressIndicator(Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(if (s.method != "bank") "Waiting for PIN approval..." else "Submitting...")
            } else Text("Pay now")
        }
    }
}