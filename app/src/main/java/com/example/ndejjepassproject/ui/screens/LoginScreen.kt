// ui/screens/LoginScreen.kt
// Collects AuthUiState. Shows error from ViewModel. Loading spinner
// replaces the button while isLoading = true.

@Composable
fun LoginScreen(vm: AuthViewModel, nav: NavController) {
    val state by vm.state.collectAsStateWithLifecycle()
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ndejje University", style = MaterialTheme.typography.headlineSmall)
        Text("Student Clearance", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("University email") },
            placeholder = { Text("you@stud.ndejjeuniversity.ac.ug") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        // Show error from ViewModel underneath the fields
        state.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { vm.login(email, password) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
            else Text("Log in")
        }
    }
}