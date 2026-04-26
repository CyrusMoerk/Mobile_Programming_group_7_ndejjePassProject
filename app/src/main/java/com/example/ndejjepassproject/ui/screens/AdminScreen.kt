// ui/screens/AdminScreen.kt
// Admin sees all pending payments. Approve fires PaymentRepository
// which auto-recalculates the student's clearance. List updates via Flow.

@Composable
fun AdminScreen(vm: AdminViewModel) {
    val s by vm.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Pending payments (${s.pendingPayments.size})") })
        if (s.pendingPayments.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No pending payments", color = Color.Gray)
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(12.dp)) {
                items(s.pendingPayments, key = { it.id }) { payment ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Student ID: ${payment.studentId}", fontWeight = FontWeight.Medium)
                            Text("Amount: UGX ${payment.amount.toLong()}")
                            Text("Method: ${payment.method.uppercase()}")
                            Text("Ref: ${payment.reference}", fontSize = 12.sp, color = Color.Gray)
                            Spacer(Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                // APPROVE — triggers clearance recalculation
                                Button(onClick = { vm.approve(payment.id, payment.studentId) }) {
                                    Text("Approve")
                                }
                                OutlinedButton(onClick = { vm.reject(payment.id, "Invalid reference") }) {
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