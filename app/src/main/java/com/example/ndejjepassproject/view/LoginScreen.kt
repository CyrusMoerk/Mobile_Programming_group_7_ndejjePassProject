package com.example.ndejjepassproject.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.School
import com.example.ndejjepassproject.ui.theme.Green600
import androidx.compose.ui.tooling.preview.Preview
import com.example.ndejjepassproject.ui.theme.NdejjeClearPassTheme

@Composable
fun LoginScreen(
    onLoginClick: (String, String, String) -> Unit,  // role, username, password
    onForgotPassword: () -> Unit = {}
) {
    // ── Local state ───────────────────────────────────────────
    var studentId       by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole    by remember { mutableStateOf("Student") }
    var roleMenuOpen    by remember { mutableStateOf(false) }

    val roles = listOf("Student", "Admin", "Security Staff")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 28.dp),
        verticalArrangement   = Arrangement.Center,
        horizontalAlignment   = Alignment.CenterHorizontally
    ) {

        // ── Logo / icon ───────────────────────────────────────
        Icon(
            imageVector        = Icons.Filled.School,
            contentDescription = "NdejjeClearPass",
            tint               = Green600,
            modifier           = Modifier.size(72.dp)
        )
        Spacer(Modifier.height(8.dp))

        // ── App name ──────────────────────────────────────────
        Text(
            text  = "NdejjeClearPass",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text  = "Ndejje University Kampala Campus",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(36.dp))

        // ── Role selector ─────────────────────────────────────
        Text(
            text  = "Sign in as",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { roleMenuOpen = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text     = selectedRole,
                    modifier = Modifier.weight(1f),
                    style    = MaterialTheme.typography.bodyLarge,
                    color    = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector        = Icons.Filled.School,
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.primary,
                    modifier           = Modifier.size(16.dp)
                )
            }
            DropdownMenu(
                expanded        = roleMenuOpen,
                onDismissRequest = { roleMenuOpen = false },
                modifier        = Modifier.fillMaxWidth()
            ) {
                roles.forEach { role ->
                    DropdownMenuItem(
                        text    = { Text(role) },
                        onClick = {
                            selectedRole = role
                            roleMenuOpen = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        // ── Student ID / username field ───────────────────────
        OutlinedTextField(
            value         = studentId,
            onValueChange = { studentId = it },
            label         = {
                Text(if (selectedRole == "Student") "Registration Number" else "Username")
            },
            modifier      = Modifier.fillMaxWidth(),
            singleLine    = true,
            shape         = RoundedCornerShape(8.dp)
        )
        Spacer(Modifier.height(12.dp))

        // ── Password field ────────────────────────────────────
        OutlinedTextField(
            value               = password,
            onValueChange       = { password = it },
            label               = { Text("Password") },
            modifier            = Modifier.fillMaxWidth(),
            singleLine          = true,
            shape               = RoundedCornerShape(8.dp),
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions     = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon        = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible)
                            "Hide password" else "Show password",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
        Spacer(Modifier.height(6.dp))

        // ── Forgot password ───────────────────────────────────
        TextButton(
            onClick  = onForgotPassword,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text  = "Forgot password?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.height(16.dp))

        // ── Sign in button ────────────────────────────────────
        Button(
            onClick  = { onLoginClick(selectedRole, studentId, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape    = RoundedCornerShape(8.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text  = "Sign In",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(Modifier.height(32.dp))

        // ── Footer ────────────────────────────────────────────
        Text(
            text  = "Ndejje University — Official Clearance System",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    NdejjeClearPassTheme {
        LoginScreen(
            onLoginClick = { _, _, _ -> }
        )
    }
}