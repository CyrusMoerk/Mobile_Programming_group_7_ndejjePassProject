// ui/screens/DashboardScreen.kt
// Reads DashboardUiState. Progress bar and QR button react to
// clearance.percentage automatically — no manual refresh needed.

@Composable
fun DashboardScreen(vm: DashboardViewModel, nav: NavController) {
    val s by vm.state.collectAsStateWithLifecycle()
    val student    = s.student ?: return
    val clearance  = s.clearance

    Column(Modifier.fillMaxSize()) {
        // --- Header ---
        Box(Modifier.fillMaxWidth().background(Color(0xFF0F6E56)).padding(20.dp)) {
            Column {
                Text("Hello, ${student.name}", color = Color.White, fontSize = 18.sp)
                Text(student.regNumber, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Text(student.programName, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
        // --- Clearance Card ---
        Card(Modifier.fillMaxWidth().padding(16.dp)) {
            Column(Modifier.padding(16.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Clearance status")
                    Text("${clearance?.percentage ?: 0}%", fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(
                    progress = (clearance?.percentage ?: 0) / 100f,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                // QR button only active when 100% cleared
                Button(
                    onClick = { /* open QR screen */ },
                    enabled = clearance?.isCleared == true,
                    modifier = Modifier.fillMaxWidth()
                ) { Text(if (clearance?.isCleared == true) "View QR permit" else "QR permit locked") }
            }
        }
        // --- Tuition Fee Card ---
        Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable {
            nav.navigate(Screen.Payment.route)
        }) {
            Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Tuition fee", fontWeight = FontWeight.Medium)
                    Text("UGX 1,800,000", style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    if (clearance?.isCleared == true) "Paid" else "Tap to pay →",
                    color = if (clearance?.isCleared == true) Color(0xFF0F6E56) else MaterialTheme.colorScheme.primary
                )
            }
        }
        // --- Payment history ---
        LazyColumn(Modifier.padding(16.dp)) {
            items(s.payments) { payment ->
                PaymentHistoryItem(payment)
            }
        }
    }
}