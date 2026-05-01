package com.example.ndejjepassproject.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ndejjepassproject.ui.theme.*
import com.example.ndejjepassproject.viewmodel.ProfileEditState
import com.example.ndejjepassproject.viewmodel.PasswordState

@Composable
fun ProfileScreen(
    studentName: String = "Nakato Sarah",
    regNumber: String = "24/2/306/D/001",
    programme: String = "BSc. Computer Science",
    email: String = "sarah@students.ndejje.ac.ug",
    photoPath: String = "",
    editState: ProfileEditState = ProfileEditState(),
    pwState: PasswordState = PasswordState(),
    onNameChanged: (String) -> Unit = {},
    onYearChanged: (Int) -> Unit = {},
    onSemesterChanged: (Int) -> Unit = {},
    onHallChanged: (String) -> Unit = {},
    onNationalityChanged: (String) -> Unit = {},
    onStudyModeChanged: (String) -> Unit = {},
    onIntakeChanged: (String) -> Unit = {},
    onSaveProfile: () -> Unit = {},
    onPhotoSelected: (String) -> Unit = {},
    onCurrentPwChanged: (String) -> Unit = {},
    onNewPwChanged: (String) -> Unit = {},
    onConfirmPwChanged: (String) -> Unit = {},
    onChangePassword: () -> Unit = {},
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val headerGradient = Brush.verticalGradient(listOf(Green800, Green600))
    var isEditing by remember { mutableStateOf(false) }
    var showPasswordSection by remember { mutableStateOf(false) }
    var studyModeMenuOpen by remember { mutableStateOf(false) }
    var intakeMenuOpen by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onPhotoSelected(it.toString()) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header ────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = headerGradient)
                .padding(top = 40.dp, bottom = 32.dp,
                    start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, null, tint = White)
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Filled.Logout, null, tint = White)
                    }
                }
                Spacer(Modifier.height(8.dp))

                // ── Avatar / Photo ────────────────────────────
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Green400)
                            .border(3.dp, White, CircleShape)
                            .clickable { imagePicker.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (photoPath.isNotEmpty()) {
                            AsyncImage(
                                model = Uri.parse(photoPath),
                                contentDescription = "Profile photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text = if (studentName.isNotEmpty())
                                    studentName.first().toString() else "?",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Green600)
                            .clickable { imagePicker.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.CameraAlt, null,
                            tint = White,
                            modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text(studentName,
                    style = MaterialTheme.typography.titleLarge,
                    color = White, fontWeight = FontWeight.Bold)
                Text(regNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Green100)
                Text(programme,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Green100)
            }
        }

        Spacer(Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            // ── Edit toggle ───────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Personal Details",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)
                TextButton(onClick = { isEditing = !isEditing }) {
                    Icon(
                        if (isEditing) Icons.Filled.Close else Icons.Filled.Edit,
                        null, tint = Green600,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(if (isEditing) "Cancel" else "Edit", color = Green600)
                }
            }

            Spacer(Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    if (isEditing) {
                        // Editable fields
                        OutlinedTextField(
                            value = editState.name,
                            onValueChange = onNameChanged,
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = editState.hall,
                            onValueChange = onHallChanged,
                            label = { Text("Hall / Room") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = editState.nationality,
                            onValueChange = onNationalityChanged,
                            label = { Text("Nationality") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        // Study Mode dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { studyModeMenuOpen = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Study Mode: ${editState.studyMode}",
                                    modifier = Modifier.weight(1f),
                                    color = Green600)
                                Icon(Icons.Filled.ArrowDropDown, null, tint = Green600)
                            }
                            DropdownMenu(
                                expanded = studyModeMenuOpen,
                                onDismissRequest = { studyModeMenuOpen = false }
                            ) {
                                listOf("Day", "Weekend").forEach { mode ->
                                    DropdownMenuItem(
                                        text = { Text(mode) },
                                        onClick = { onStudyModeChanged(mode); studyModeMenuOpen = false }
                                    )
                                }
                            }
                        }
                        // Intake dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { intakeMenuOpen = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Intake: ${editState.intake}",
                                    modifier = Modifier.weight(1f),
                                    color = Green600)
                                Icon(Icons.Filled.ArrowDropDown, null, tint = Green600)
                            }
                            DropdownMenu(
                                expanded = intakeMenuOpen,
                                onDismissRequest = { intakeMenuOpen = false }
                            ) {
                                listOf("August", "January").forEach { intake ->
                                    DropdownMenuItem(
                                        text = { Text(intake) },
                                        onClick = { onIntakeChanged(intake); intakeMenuOpen = false }
                                    )
                                }
                            }
                        }

                        if (editState.error != null) {
                            Text(editState.error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall)
                        }
                        if (editState.saveSuccess) {
                            Text("Profile saved successfully",
                                color = Green600,
                                style = MaterialTheme.typography.bodySmall)
                        }

                        Button(
                            onClick = onSaveProfile,
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !editState.isSaving
                        ) {
                            if (editState.isSaving)
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = White)
                            else Text("Save Changes")
                        }

                    } else {
                        // Read-only view
                        ProfileDetailRow(Icons.Filled.Person, "Full Name", studentName)
                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                        ProfileDetailRow(Icons.Filled.Email, "Email", email)
                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                        ProfileDetailRow(Icons.Filled.School, "Programme", programme)
                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                        ProfileDetailRow(Icons.Filled.MeetingRoom, "Hall / Room",
                            editState.hall.ifEmpty { "Not set" })
                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                        ProfileDetailRow(Icons.Filled.Flag, "Nationality",
                            editState.nationality)
                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                        ProfileDetailRow(Icons.Filled.WbSunny, "Study Mode",
                            editState.studyMode)
                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                        ProfileDetailRow(Icons.Filled.CalendarMonth, "Intake",
                            editState.intake)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Change Password ───────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Security",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)
                TextButton(onClick = { showPasswordSection = !showPasswordSection }) {
                    Text(if (showPasswordSection) "Hide" else "Change Password",
                        color = Green600)
                }
            }

            if (showPasswordSection) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = pwState.current,
                            onValueChange = onCurrentPwChanged,
                            label = { Text("Current Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        OutlinedTextField(
                            value = pwState.newPw,
                            onValueChange = onNewPwChanged,
                            label = { Text("New Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        OutlinedTextField(
                            value = pwState.confirm,
                            onValueChange = onConfirmPwChanged,
                            label = { Text("Confirm New Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        if (pwState.error != null) {
                            Text(pwState.error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall)
                        }
                        if (pwState.saveSuccess) {
                            Text("Password changed successfully",
                                color = Green600,
                                style = MaterialTheme.typography.bodySmall)
                        }
                        Button(
                            onClick = onChangePassword,
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !pwState.isSaving
                        ) {
                            Text("Update Password")
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Red600)
            ) {
                Icon(Icons.Filled.Logout, null, tint = Red600)
                Spacer(Modifier.width(8.dp))
                Text("Sign Out", color = Red600)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null,
            tint = Green600,
            modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePreview() {
    NdejjeClearPassTheme {
        ProfileScreen()
    }
}