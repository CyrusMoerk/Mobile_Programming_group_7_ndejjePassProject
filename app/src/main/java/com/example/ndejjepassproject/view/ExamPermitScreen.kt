package com.example.ndejjepassproject.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ndejjepassproject.ui.theme.*

@Composable
fun ExamPermitScreen(
    studentName: String = "NABAYA NESTROY",
    regNumber: String = "24/2/306/D/053",
    paymentCode: String = "1003480",
    hall: String = "NJUKI",
    nationality: String = "Ugandan",
    programme: String = "BSc. Computer Science",
    studyMode: String = "Day",
    semester: String = "II",
    academicYear: String = "2025/2026",
    dateOfIssue: String = "28 April 2026",
    isFullyCleared: Boolean = true,
    courses: List<CourseItem> = listOf(
        CourseItem("BCS2201", "Mobile Programming", 4),
        CourseItem("BCS2202", "Database Systems II", 3),
        CourseItem("BCS2203", "Software Engineering", 3),
        CourseItem("BCS2204", "Computer Networks", 3),
    ),
    retakes: List<CourseItem> = listOf(
        CourseItem("BCS2101", "Introduction to Programming", 3, true)
    ),
    onBack: () -> Unit = {},
    onPrint: () -> Unit = {}
) {
    val headerGradient = Brush.verticalGradient(listOf(Green800, Green600))

    if (!isFullyCleared) {
        // ── Locked state ──────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Filled.Lock, contentDescription = null,
                tint = Red600, modifier = Modifier.size(72.dp))
            Spacer(Modifier.height(16.dp))
            Text("Exam Permit Locked",
                style = MaterialTheme.typography.headlineMedium,
                color = Red600)
            Spacer(Modifier.height(8.dp))
            Text(
                "You must clear 100% of your tuition before\n" +
                        "your Examination Permit is generated.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(Modifier.height(24.dp))
            OutlinedButton(
                onClick = onBack,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Go Back", color = Green600)
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            // ── Header ────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = headerGradient)
                    .padding(top = 40.dp, bottom = 20.dp,
                        start = 16.dp, end = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Filled.ArrowBack, null, tint = White)
                        }
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("Examination Permit",
                                style = MaterialTheme.typography.titleLarge,
                                color = White)
                            Text("Academic Year $academicYear — Semester $semester",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Green100)
                        }
                    }
                    IconButton(onClick = onPrint) {
                        Icon(Icons.Filled.Print, null, tint = White)
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                // ── Permit card ───────────────────────────────
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Green200),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        // University header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("NDEJJE UNIVERSITY",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Green800)
                                Text("Kampala Campus",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Green600)
                                Spacer(Modifier.height(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Green600
                                ) {
                                    Text(
                                        "EXAMINATION PERMIT",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = White,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        Divider(color = Green200)
                        Spacer(Modifier.height(16.dp))

                        // Student photo + details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Photo placeholder
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Green50)
                                    .border(1.dp, Green200, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Filled.Person, null,
                                        tint = Green400,
                                        modifier = Modifier.size(40.dp))
                                    Text("Photo",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Green400)
                                }
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                PermitField("Name", studentName)
                                PermitField("Reg. Number", regNumber)
                                PermitField("Payment Code", paymentCode)
                                PermitField("Hall / Room", hall)
                            }
                        }

                        Spacer(Modifier.height(12.dp))
                        Divider(color = Green200)
                        Spacer(Modifier.height(12.dp))

                        // More details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                PermitField("Nationality", nationality)
                                PermitField("Programme", programme)
                            }
                            Column(modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                PermitField("Study Mode", studyMode)
                                PermitField("Date of Issue", dateOfIssue)
                            }
                        }

                        Spacer(Modifier.height(12.dp))
                        Divider(color = Green200)
                        Spacer(Modifier.height(12.dp))

                        // Courses table
                        Text("Registered Courses",
                            style = MaterialTheme.typography.titleMedium,
                            color = Green800,
                            fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))

                        // Table header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Green50, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text("Code", style = MaterialTheme.typography.labelSmall,
                                color = Green800, modifier = Modifier.weight(0.8f))
                            Text("Course Name", style = MaterialTheme.typography.labelSmall,
                                color = Green800, modifier = Modifier.weight(2f))
                            Text("CU", style = MaterialTheme.typography.labelSmall,
                                color = Green800, modifier = Modifier.weight(0.4f),
                                textAlign = TextAlign.End)
                        }

                        courses.forEach { course ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(course.code,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(0.8f))
                                Text(course.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(2f))
                                Text("${course.creditUnits}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Green600,
                                    modifier = Modifier.weight(0.4f),
                                    textAlign = TextAlign.End)
                            }
                            Divider(color = Green50)
                        }

                        // Retakes table
                        if (retakes.isNotEmpty()) {
                            Spacer(Modifier.height(12.dp))
                            Text("Retake Units",
                                style = MaterialTheme.typography.titleMedium,
                                color = Amber600,
                                fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Amber50, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text("Code", style = MaterialTheme.typography.labelSmall,
                                    color = Amber600, modifier = Modifier.weight(0.8f))
                                Text("Course Name", style = MaterialTheme.typography.labelSmall,
                                    color = Amber600, modifier = Modifier.weight(2f))
                                Text("CU", style = MaterialTheme.typography.labelSmall,
                                    color = Amber600, modifier = Modifier.weight(0.4f),
                                    textAlign = TextAlign.End)
                            }
                            retakes.forEach { course ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(course.code,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(0.8f))
                                    Text(course.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(2f))
                                    Text("${course.creditUnits}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Amber600,
                                        modifier = Modifier.weight(0.4f),
                                        textAlign = TextAlign.End)
                                }
                                Divider(color = Amber50)
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        Divider(color = Green200)
                        Spacer(Modifier.height(12.dp))

                        // Cleared stamp
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Green50,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.5.dp, Green600)
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 20.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Filled.Verified, null,
                                        tint = Green600,
                                        modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("FULLY CLEARED — 100%",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Green800,
                                        fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                // Print button
                Button(
                    onClick = onPrint,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Filled.Print, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Print Exam Permit",
                        style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun PermitField(label: String, value: String) {
    Column {
        Text(label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExamPermitPreview() {
    NdejjeClearPassTheme {
        ExamPermitScreen()
    }
}