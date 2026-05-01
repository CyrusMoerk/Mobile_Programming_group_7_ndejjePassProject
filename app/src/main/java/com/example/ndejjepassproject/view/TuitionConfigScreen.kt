package com.example.ndejjepassproject.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjepassproject.ui.theme.*

@Composable
fun TuitionConfigScreen(
    onBack: () -> Unit = {},
    onSave: (programme: String, mode: String, intake: String,
             semester: String, amount: String) -> Unit = { _, _, _, _, _ -> }
) {
    var programme      by remember { mutableStateOf("") }
    var studyMode      by remember { mutableStateOf("Day") }
    var intake         by remember { mutableStateOf("August") }
    var semester       by remember { mutableStateOf("Semester I") }
    var tuitionAmount  by remember { mutableStateOf("") }
    var ncheFee        by remember { mutableStateOf("") }
    var saved          by remember { mutableStateOf(false) }

    var modeMenuOpen     by remember { mutableStateOf(false) }
    var intakeMenuOpen   by remember { mutableStateOf(false) }
    var semMenuOpen      by remember { mutableStateOf(false) }

    val headerGradient = Brush.verticalGradient(listOf(Green800, Green600))

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
                    Text("Tuition Configuration",
                        style = MaterialTheme.typography.titleLarge,
                        color = White)
                    Text("Set fees by programme, mode and intake",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Green100)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                if (saved) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Green50),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CheckCircle, null,
                                tint = Green600, modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(12.dp))
                            Text("Tuition saved successfully.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Green800)
                        }
                    }
                }
            }

            item {
                Text("Programme Details",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)
            }

            item {
                OutlinedTextField(
                    value = programme,
                    onValueChange = { programme = it },
                    label = { Text("Programme Name") },
                    placeholder = { Text("e.g. BSc. Computer Science") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(Icons.Filled.School, null, tint = Green600)
                    }
                )
            }

            item {
                // Study mode dropdown
                ConfigDropdown(
                    label = "Study Mode",
                    value = studyMode,
                    options = listOf("Day", "Weekend"),
                    expanded = modeMenuOpen,
                    onExpand = { modeMenuOpen = true },
                    onDismiss = { modeMenuOpen = false },
                    onSelect = { studyMode = it; modeMenuOpen = false },
                    icon = Icons.Filled.WbSunny
                )
            }

            item {
                ConfigDropdown(
                    label = "Intake",
                    value = intake,
                    options = listOf("August", "January"),
                    expanded = intakeMenuOpen,
                    onExpand = { intakeMenuOpen = true },
                    onDismiss = { intakeMenuOpen = false },
                    onSelect = { intake = it; intakeMenuOpen = false },
                    icon = Icons.Filled.CalendarMonth
                )
            }

            item {
                ConfigDropdown(
                    label = "Semester",
                    value = semester,
                    options = listOf("Semester I", "Semester II"),
                    expanded = semMenuOpen,
                    onExpand = { semMenuOpen = true },
                    onDismiss = { semMenuOpen = false },
                    onSelect = { semester = it; semMenuOpen = false },
                    icon = Icons.Filled.DateRange
                )
            }

            item {
                Divider(color = Green100)
                Spacer(Modifier.height(4.dp))
                Text("Fee Amounts",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)
            }

            item {
                OutlinedTextField(
                    value = tuitionAmount,
                    onValueChange = { tuitionAmount = it },
                    label = { Text("Tuition Amount (UGX)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Text("UGX",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Green600,
                            modifier = Modifier.padding(start = 12.dp))
                    }
                )
            }

            item {
                OutlinedTextField(
                    value = ncheFee,
                    onValueChange = { ncheFee = it },
                    label = { Text("NCHE / National Council Fee (UGX)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Text("UGX",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Green600,
                            modifier = Modifier.padding(start = 12.dp))
                    }
                )
            }

            item {
                // Summary card
                if (programme.isNotBlank() && tuitionAmount.isNotBlank()) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Green50),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Configuration Summary",
                                style = MaterialTheme.typography.titleMedium,
                                color = Green800, fontWeight = FontWeight.Bold)
                            Divider(color = Green100)
                            SummaryRow("Programme", programme)
                            SummaryRow("Mode", "$studyMode — $intake intake")
                            SummaryRow("Semester", semester)
                            SummaryRow("Tuition", "UGX $tuitionAmount")
                            if (ncheFee.isNotBlank())
                                SummaryRow("NCHE Fee", "UGX $ncheFee")
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        onSave(programme, studyMode, intake, semester, tuitionAmount)
                        saved = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = programme.isNotBlank() && tuitionAmount.isNotBlank()
                ) {
                    Icon(Icons.Filled.Save, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save Tuition Configuration",
                        style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ConfigDropdown(
    label: String,
    value: String,
    options: List<String>,
    expanded: Boolean,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = onExpand,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(icon, null, tint = Green600, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("$label: $value",
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.primary)
            Icon(Icons.Filled.ArrowDropDown, null, tint = Green600)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = onDismiss,
            modifier = Modifier.fillMaxWidth()) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onSelect(option) }
                )
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Green800)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TuitionConfigPreview() {
    NdejjeClearPassTheme {
        TuitionConfigScreen()
    }
}