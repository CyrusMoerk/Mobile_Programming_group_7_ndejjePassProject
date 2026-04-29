package com.example.ndejjepassproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.ndejjepassproject.data.db.entities.StudentEntity
import com.example.ndejjepassproject.ui.theme.Mobile_Programming_group_7_ndejjePassProjectTheme
import com.example.ndejjepassproject.viewmodel.PasswordState
import com.example.ndejjepassproject.viewmodel.ProfileEditState
import com.example.ndejjepassproject.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(vm: ProfileViewModel, nav: NavController) {
    val student by vm.studentState.collectAsState()
    val edit by vm.editState.collectAsState()
    val pw by vm.pwState.collectAsState()

    LaunchedEffect(edit.saveSuccess) {
        if (edit.saveSuccess) nav.popBackStack()
    }

    ProfileScreenContent(
        student = student,
        edit = edit,
        pw = pw,
        onNameChanged = vm::onNameChanged,
        onYearChanged = vm::onYearChanged,
        onSemesterChanged = vm::onSemesterChanged,
        saveProfile = vm::saveProfile,
        onCurrentPwChanged = vm::onCurrentPwChanged,
        onNewPwChanged = vm::onNewPwChanged,
        onConfirmPwChanged = vm::onConfirmPwChanged,
        changePassword = vm::changePassword
    )
}

@Composable
fun ProfileScreenContent(
    student: StudentEntity?,
    edit: ProfileEditState,
    pw: PasswordState,
    onNameChanged: (String) -> Unit,
    onYearChanged: (Int) -> Unit,
    onSemesterChanged: (Int) -> Unit,
    saveProfile: () -> Unit,
    onCurrentPwChanged: (String) -> Unit,
    onNewPwChanged: (String) -> Unit,
    onConfirmPwChanged: (String) -> Unit,
    changePassword: () -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Text("My profile", style = MaterialTheme.typography.titleLarge) }
        item {
            SectionHeader("University details")
            LockedField(label = "Email", value = student?.email ?: "")
            LockedField(label = "Reg number", value = student?.regNumber ?: "")
            LockedField(label = "Program", value = student?.programName ?: "")
            LockedField(label = "Program code", value = student?.programCode ?: "")
        }
        item {
            SectionHeader("Edit your info")
            OutlinedTextField(
                value = edit.name,
                onValueChange = onNameChanged,
                label = { Text("Full name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            SelectionRow(
                label = "Year of study",
                options = (1..5).map { "Year $it" },
                selectedIndex = (edit.year - 1).coerceIn(0, 4),
                onSelected = { idx -> onYearChanged(idx + 1) }
            )
            Spacer(Modifier.height(8.dp))
            SelectionRow(
                label = "Semester",
                options = listOf("Semester 1", "Semester 2"),
                selectedIndex = (edit.semester - 1).coerceIn(0, 1),
                onSelected = { idx -> onSemesterChanged(idx + 1) }
            )
            edit.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp) }
            Spacer(Modifier.height(12.dp))
            Button(onClick = saveProfile, modifier = Modifier.fillMaxWidth(), enabled = !edit.isSaving) {
                if (edit.isSaving) CircularProgressIndicator(Modifier.height(18.dp))
                else Text("Save changes")
            }
        }
        item {
            SectionHeader("Change password")
            OutlinedTextField(
                value = pw.current,
                onValueChange = onCurrentPwChanged,
                label = { Text("Current password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = pw.newPw,
                onValueChange = onNewPwChanged,
                label = { Text("New password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = pw.confirm,
                onValueChange = onConfirmPwChanged,
                label = { Text("Confirm new password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            pw.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp) }
            if (pw.saveSuccess) Text("Password changed successfully", color = Color(0xFF0F6E56))
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = changePassword,
                modifier = Modifier.fillMaxWidth(),
                enabled = !pw.isSaving
            ) {
                if (pw.isSaving) CircularProgressIndicator(Modifier.height(18.dp))
                else Text("Update password")
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun SelectionRow(
    label: String,
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    Text(label, style = MaterialTheme.typography.labelLarge)
    Spacer(Modifier.height(6.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEachIndexed { index, option ->
            OutlinedButton(onClick = { onSelected(index) }) {
                Text(if (index == selectedIndex) "$option *" else option)
            }
        }
    }
}

@Composable
private fun LockedField(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 13.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.width(4.dp))
            androidx.compose.material3.Icon(
                Icons.Default.Lock,
                contentDescription = "Locked",
                tint = Color.Gray
            )
        }
    }
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val sampleStudent = StudentEntity(
        name = "John Doe",
        email = "john.doe@ndejje.ac.ug",
        passwordHash = "",
        regNumber = "2023/BSIT/001",
        programCode = "306",
        programName = "Bachelor of Science in Information Technology",
        year = 2,
        semester = 1
    )
    val sampleEdit = ProfileEditState(
        name = "John Doe",
        year = 2,
        semester = 1
    )
    val samplePw = PasswordState()

    Mobile_Programming_group_7_ndejjePassProjectTheme {
        ProfileScreenContent(
            student = sampleStudent,
            edit = sampleEdit,
            pw = samplePw,
            onNameChanged = {},
            onYearChanged = {},
            onSemesterChanged = {},
            saveProfile = {},
            onCurrentPwChanged = {},
            onNewPwChanged = {},
            onConfirmPwChanged = {},
            changePassword = {}
        )
    }
}
