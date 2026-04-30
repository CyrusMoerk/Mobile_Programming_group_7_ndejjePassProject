package com.example.ndejjepassproject.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ndejjepassproject.ui.theme.*

@Composable
fun ProfileScreen(
    studentName: String = "NABAYA NESTROY",
    regNumber: String = "24/2/306/D/053",
    programme: String = "BSc. Computer Science",
    yearOfStudy: String = "Year 2",
    hall: String = "NJUKI",
    nationality: String = "Ugandan",
    studyMode: String = "Day",
    intake: String = "August",
    email: String = "nestroy.nabaya@stud.ndejjeuniversity.ac.ug",
    onBack: () -> Unit = {},
    onChangePhoto: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val headerGradient = Brush.verticalGradient(listOf(Green800, Green600))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header with avatar ────────────────────────────────
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

                // Avatar
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Green400)
                            .border(3.dp, White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (studentName.isNotEmpty()) studentName.first().toString() else "?",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                    // Camera button
                    IconButton(
                        onClick = onChangePhoto,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Green600)
                    ) {
                        Icon(Icons.Filled.CameraAlt, null,
                            tint = White,
                            modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text(studentName,
                    style = MaterialTheme.typography.titleLarge,
                    color = White,
                    fontWeight = FontWeight.Bold)
                Text(regNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Green100)
                Text(programme,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Green100)
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Personal details ──────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Personal Details",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileDetailRow(Icons.Filled.Person, "Full Name", studentName)
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    ProfileDetailRow(Icons.Filled.Badge, "Reg. Number", regNumber)
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    ProfileDetailRow(Icons.Filled.Email, "Email", email)
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    ProfileDetailRow(Icons.Filled.Flag, "Nationality", nationality)
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Academic Details",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileDetailRow(Icons.Filled.School, "Programme", programme)
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    ProfileDetailRow(Icons.Filled.Grade, "Year of Study", yearOfStudy)
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    ProfileDetailRow(Icons.Filled.MeetingRoom, "Hall / Room", hall)
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    ProfileDetailRow(Icons.Filled.WbSunny, "Study Mode", studyMode)
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    ProfileDetailRow(Icons.Filled.CalendarMonth, "Intake", intake)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Photo update note
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Green50),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Info, null,
                        tint = Green600, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Your passport photo can be updated once per semester. " +
                                "It appears on your Examination Permit and QR code.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Green800
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onChangePhoto,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Filled.CameraAlt, null, tint = Green600)
                Spacer(Modifier.width(8.dp))
                Text("Update Passport Photo", color = Green600)
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
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