package com.example.ndejjepassproject.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjepassproject.ui.theme.*
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.Send

data class CourseItem(
    val code: String,
    val name: String,
    val creditUnits: Int,
    val isRetake: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeansRegScreen(
    studentName: String = "Nakato Sarah",
    semester: String = "Semester II — 2025/2026",
    availableCourses: List<CourseItem> = listOf(
        CourseItem("BCS2201", "Mobile Programming", 4),
        CourseItem("BCS2202", "Database Systems II", 3),
        CourseItem("BCS2203", "Software Engineering", 3),
        CourseItem("BCS2204", "Computer Networks", 3),
        CourseItem("BCS2205", "Operating Systems", 3),
    ),
    onSubmit: (selected: List<CourseItem>, retakes: List<CourseItem>) -> Unit = { _, _ -> },
    onBack: () -> Unit = {}
) {
    val selectedCourses = remember { mutableStateListOf<String>() }
    val retakeCourses   = remember { mutableStateListOf<String>() }
    var hasRetakes      by remember { mutableStateOf(false) }

    val headerGradient = Brush.verticalGradient(listOf(Green800, Green600))

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = headerGradient)
                    .padding(top = 40.dp, bottom = 16.dp,
                        start = 16.dp, end = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                            tint = White)
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("Dean's Registration Form",
                            style = MaterialTheme.typography.titleLarge,
                            color = White)
                        Text(semester,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Green100)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                // Info card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Green50),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Info, contentDescription = null,
                            tint = Green600, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Select the courses you are taking this semester. " +
                                    "Retake fees will be added to your balance automatically.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Green800
                        )
                    }
                }
            }

            item {
                Text("Select Your Courses",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)
            }

            items(availableCourses) { course ->
                CourseSelectionCard(
                    course = course,
                    isSelected = selectedCourses.contains(course.code),
                    onToggle = {
                        if (selectedCourses.contains(course.code))
                            selectedCourses.remove(course.code)
                        else
                            selectedCourses.add(course.code)
                    }
                )
            }

            item { Spacer(Modifier.height(8.dp)) }

            item {
                // Retakes toggle
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasRetakes) Amber50
                        else MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Do you have retakes?",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface)
                            Text("Retake fees will be added to your balance",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = hasRetakes,
                            onCheckedChange = { hasRetakes = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Amber600,
                                checkedTrackColor = Amber50
                            )
                        )
                    }
                }
            }

            if (hasRetakes) {
                item {
                    Text("Select Retake Units",
                        style = MaterialTheme.typography.titleMedium,
                        color = Amber600)
                }
                items(availableCourses) { course ->
                    CourseSelectionCard(
                        course = course,
                        isSelected = retakeCourses.contains(course.code),
                        isRetakeMode = true,
                        onToggle = {
                            if (retakeCourses.contains(course.code))
                                retakeCourses.remove(course.code)
                            else
                                retakeCourses.add(course.code)
                        }
                    )
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        val selected = availableCourses.filter {
                            selectedCourses.contains(it.code) }
                        val retakes = availableCourses.filter {
                            retakeCourses.contains(it.code) }
                        onSubmit(selected, retakes)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = selectedCourses.isNotEmpty()
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Submit Registration Form",
                        style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun CourseSelectionCard(
    course: CourseItem,
    isSelected: Boolean,
    isRetakeMode: Boolean = false,
    onToggle: () -> Unit
) {
    val borderColor = when {
        isSelected && isRetakeMode -> Amber600
        isSelected                 -> Green600
        else                       -> MaterialTheme.colorScheme.outline
    }
    val bgColor = when {
        isSelected && isRetakeMode -> Amber50
        isSelected                 -> Green50
        else                       -> MaterialTheme.colorScheme.surface
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 1.5.dp else 0.5.dp, borderColor),
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggle
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = if (isRetakeMode) Amber600 else Green600
                )
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(course.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface)
                Text(course.code,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isRetakeMode) Amber50 else Green50
            ) {
                Text(
                    text = "${course.creditUnits} CU",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isRetakeMode) Amber600 else Green600,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DeansRegPreview() {
    NdejjeClearPassTheme {
        DeansRegScreen()
    }
}