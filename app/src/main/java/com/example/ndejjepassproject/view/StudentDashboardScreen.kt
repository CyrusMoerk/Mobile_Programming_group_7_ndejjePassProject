package com.example.ndejjepassproject.view

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ndejjepassproject.ui.theme.*

@Composable
fun StudentDashboardScreen(
    studentName: String = "NABAYA NESTROY",
    regNumber: String = "24/2/306/D/053",
    programme: String = "BCS — Year 2, Semester II",
    tuitionPaid: Double = 1_080_000.0,
    totalTuition: Double = 1_800_000.0,
    nchePaid: Boolean = true,
    onMakePayment: () -> Unit = {},
    onViewCourses: () -> Unit = {},
    onViewPermit: () -> Unit = {},
    onViewHistory: () -> Unit = {},
    onProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val percentage = ((tuitionPaid / totalTuition) * 100).toInt()
    val balance = totalTuition - tuitionPaid

    val headerGradient = Brush.verticalGradient(
        colors = listOf(Green800, Green600)
    )

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
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NdejjeClearPass",
                        style = MaterialTheme.typography.titleMedium,
                        color = Green50
                    )
                    Row {
                        IconButton(onClick = onProfile) {
                            Icon(Icons.Filled.Person, contentDescription = "Profile",
                                tint = Green50)
                        }
                        IconButton(onClick = onLogout) {
                            Icon(Icons.Filled.Logout, contentDescription = "Logout",
                                tint = Green50)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Green400),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = studentName.first().toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = White
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = studentName,
                            style = MaterialTheme.typography.titleLarge,
                            color = White
                        )
                        Text(
                            text = regNumber,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Green100
                        )
                        Text(
                            text = programme,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Green100
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Clearance status card ─────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tuition Clearance",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$percentage% Paid",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            percentage >= 100 -> Green600
                            percentage >= 60  -> Amber600
                            else              -> Red600
                        }
                    )
                    Text(
                        text = "UGX ${String.format("%,.0f", balance)} balance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { (percentage / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = when {
                        percentage >= 100 -> Green600
                        percentage >= 60  -> Amber600
                        else              -> Red600
                    },
                    trackColor = Green50
                )
                Spacer(Modifier.height(12.dp))
                // Threshold indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ThresholdChip(label = "60% Reg", reached = percentage >= 60)
                    ThresholdChip(label = "80% Lectures", reached = percentage >= 80)
                    ThresholdChip(label = "100% Permit", reached = percentage >= 100)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ── NCHE status card ──────────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (nchePaid) Green50 else Red50
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (nchePaid) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                    contentDescription = null,
                    tint = if (nchePaid) Green600 else Red600,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "NCHE / National Council Fee",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (nchePaid) Green800 else Red600
                    )
                    Text(
                        text = if (nchePaid) "Paid — clearance not blocked"
                        else "Not paid — registration card will be withheld",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (nchePaid) Green600 else Red600
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Quick actions ─────────────────────────────────────
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionCard(
                icon = Icons.Filled.Payment,
                label = "Make Payment",
                onClick = onMakePayment,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                icon = Icons.Filled.MenuBook,
                label = "My Courses",
                onClick = onViewCourses,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionCard(
                icon = Icons.Filled.Article,
                label = "Exam Permit",
                onClick = onViewPermit,
                modifier = Modifier.weight(1f),
                enabled = percentage >= 100
            )
            QuickActionCard(
                icon = Icons.Filled.History,
                label = "Payment History",
                onClick = onViewHistory,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun ThresholdChip(label: String, reached: Boolean) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (reached) Green50 else MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp, if (reached) Green400 else MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (reached) Icons.Filled.Check else Icons.Filled.Close,
                contentDescription = null,
                tint = if (reached) Green600 else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(12.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (reached) Green800 else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(if (enabled) 3.dp else 0.dp),
        onClick = { if (enabled) onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (enabled) Green600
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            if (!enabled) {
                Text(
                    text = "100% required",
                    style = MaterialTheme.typography.labelSmall,
                    color = Red600,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StudentDashboardPreview() {
    NdejjeClearPassTheme {
        StudentDashboardScreen()
    }
}