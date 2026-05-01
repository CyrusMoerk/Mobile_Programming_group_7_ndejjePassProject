package com.example.ndejjepassproject.view

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjepassproject.ui.theme.*

data class StudentSummary(
    val id: String,
    val name: String,
    val regNumber: String,
    val programme: String,
    val percentage: Int,
    val nchePaid: Boolean,
    val permitGenerated: Boolean
)

@Composable
fun StudentListScreen(
    students: List<StudentSummary> = listOf(
        StudentSummary("1","Nakato Sarah","24/2/306/D/001",
            "BSc. Computer Science",100,true,true),
        StudentSummary("2","Okello Brian","24/2/306/D/002",
            "BSc. Information Technology",60,true,false),
        StudentSummary("3","Namukasa Fatuma","24/2/306/D/003",
            "BSc. Computer Science",40,false,false),
    ),
    onBack: () -> Unit = {}
) {
    var searchQuery    by remember { mutableStateOf("") }
    var filterStatus   by remember { mutableStateOf("All") }
    var filterMenuOpen by remember { mutableStateOf(false) }

    val headerGradient = Brush.verticalGradient(listOf(Green800, Green600))

    val filtered = students.filter { s ->
        val matchSearch = s.name.contains(searchQuery, ignoreCase = true) ||
                s.regNumber.contains(searchQuery, ignoreCase = true)
        val matchFilter = when (filterStatus) {
            "Cleared"     -> s.percentage >= 100
            "Partial"     -> s.percentage in 60..99
            "Not Cleared" -> s.percentage < 60
            else          -> true
        }
        matchSearch && matchFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = headerGradient)
                .padding(top = 40.dp, bottom = 20.dp,
                    start = 16.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, null, tint = White)
                }
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("Student Clearance List",
                        style = MaterialTheme.typography.titleLarge,
                        color = White)
                    Text("${students.size} students this semester",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Green100)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatMiniCard("Cleared",
                        students.count { it.percentage >= 100 }.toString(),
                        Green600, Modifier.weight(1f))
                    StatMiniCard("Partial",
                        students.count { it.percentage in 60..99 }.toString(),
                        Amber600, Modifier.weight(1f))
                    StatMiniCard("Below 60%",
                        students.count { it.percentage < 60 }.toString(),
                        Red600, Modifier.weight(1f))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search name or reg number") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        leadingIcon = {
                            Icon(Icons.Filled.Search, null, tint = Green600)
                        }
                    )
                    Box {
                        IconButton(
                            onClick = { filterMenuOpen = true },
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Green50)
                        ) {
                            Icon(Icons.Filled.FilterList, null, tint = Green600)
                        }
                        DropdownMenu(
                            expanded = filterMenuOpen,
                            onDismissRequest = { filterMenuOpen = false }
                        ) {
                            listOf("All", "Cleared", "Partial", "Not Cleared")
                                .forEach { f ->
                                    DropdownMenuItem(
                                        text = { Text(f) },
                                        onClick = { filterStatus = f; filterMenuOpen = false }
                                    )
                                }
                        }
                    }
                }
            }

            items(filtered, key = { it.regNumber }) { student ->
                StudentSummaryCard(student = student)
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun StudentSummaryCard(student: StudentSummary) {
    val statusColor = when {
        student.percentage >= 100 -> Green600
        student.percentage >= 60  -> Amber600
        else                      -> Red600
    }
    val statusBg = when {
        student.percentage >= 100 -> Green50
        student.percentage >= 60  -> Amber50
        else                      -> Red50
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Green100),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            if (student.name.isNotEmpty())
                                student.name.first().toString() else "?",
                            style = MaterialTheme.typography.titleMedium,
                            color = Green800,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(student.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface)
                        Text(student.regNumber,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(student.programme,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Surface(shape = RoundedCornerShape(12.dp), color = statusBg) {
                    Text(
                        "${student.percentage}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { (student.percentage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = statusColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusChip("NCHE", student.nchePaid, Green600,
                    MaterialTheme.colorScheme.onSurfaceVariant)
                StatusChip("Permit", student.permitGenerated, Green600,
                    MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StudentListPreview() {
    NdejjeClearPassTheme { StudentListScreen() }
}