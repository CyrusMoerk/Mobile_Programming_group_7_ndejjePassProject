// ui/screens/ProfileScreen.kt
// Locked fields shown as plain Text() — no TextField, no cursor, not tappable.
// Editable fields shown as OutlinedTextField() — fully interactive.
// The visual difference makes it obvious to the student what they can change.

@Composable
fun ProfileScreen(vm: ProfileViewModel, nav: NavController) {
    val student  by vm.studentState.collectAsStateWithLifecycle()
    val edit     by vm.editState.collectAsStateWithLifecycle()
    val pw       by vm.pwState.collectAsStateWithLifecycle()

    LaunchedEffect(edit.saveSuccess) { if (edit.saveSuccess) nav.popBackStack() }

    LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        item {
            Text("My profile", style = MaterialTheme.typography.titleLarge)
        }

        // ── LOCKED FIELDS — displayed as read-only info, not input fields ──
        item {
            SectionHeader("University details")
            LockedField(label = "Email",         value = student?.email ?: "")
            LockedField(label = "Reg number",    value = student?.regNumber ?: "")
            LockedField(label = "Program",       value = student?.programName ?: "")
            LockedField(label = "Program code",  value = student?.programCode ?: "")
        }

        // ── EDITABLE FIELDS — bound to editState via ViewModel setters ──
        item {
            SectionHeader("Edit your info")
            OutlinedTextField(
                value = edit.name,
                onValueChange = vm::onNameChanged,
                label = { Text("Full name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            // Year dropdown — 1 to 5 only
            DropdownSelector(
                label = "Year of study",
                options = (1..5).map { "Year $it" },
                selected = "Year ${edit.year}",
                onSelected = { idx -> vm.onYearChanged(idx + 1) }
            )
            Spacer(Modifier.height(8.dp))
            // Semester — only 1 or 2
            DropdownSelector(
                label = "Semester",
                options = listOf("Semester 1", "Semester 2"),
                selected = "Semester ${edit.semester}",
                onSelected = { idx -> vm.onSemesterChanged(idx + 1) }
            )
            edit.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp) }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = vm::saveProfile,
                modifier = Modifier.fillMaxWidth(),
                enabled = !edit.isSaving
            ) {
                if (edit.isSaving) CircularProgressIndicator(Modifier.size(18.dp))
                else Text("Save changes")
            }
        }

        // ── CHANGE PASSWORD — separate section, separate ViewModel state ──
        item {
            SectionHeader("Change password")
            OutlinedTextField(
                value = pw.current, onValueChange = vm::onCurrentPwChanged,
                label = { Text("Current password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = pw.newPw, onValueChange = vm::onNewPwChanged,
                label = { Text("New password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = pw.confirm, onValueChange = vm::onConfirmPwChanged,
                label = { Text("Confirm new password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            pw.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp) }
            if (pw.saveSuccess) Text("Password changed successfully", color = Color(0xFF0F6E56))
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = vm::changePassword,
                modifier = Modifier.fillMaxWidth(),
                enabled = !pw.isSaving
            ) {
                if (pw.isSaving) CircularProgressIndicator(Modifier.size(18.dp))
                else Text("Update password")
            }
        }
    }
}

// Reusable composable for locked fields — looks different from editable fields.
// No cursor, no focus, shown as label + value rows. Makes it visually clear.
@Composable
fun LockedField(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 13.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.width(4.dp))
            // Lock icon makes it obvious this field cannot be changed
            Icon(Icons.Default.Lock, contentDescription = "Locked",
                modifier = Modifier.size(13.dp), tint = Color.Gray)
        }
    }
    Divider()
}