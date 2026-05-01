package com.example.ndejjepassproject.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.ndejjepassproject.ui.theme.Green800
import com.example.ndejjepassproject.ui.theme.Green600
import com.example.ndejjepassproject.ui.theme.White

@Composable
fun AdminDashboardScreen(
    pendingCount: Int = 0,
    totalStudents: Int = 0,
    onApprovePayments: () -> Unit = {},
    onViewStudents: () -> Unit = {},
    onTuitionConfig: () -> Unit = {},
    onLogout: () -> Unit = {}
) {

    val headerGradient = Brush.verticalGradient(
        colors = listOf(Green800, Green600)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {

        // ── Header ─────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerGradient)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Admin Panel",
                    style = MaterialTheme.typography.titleLarge,
                    color = White
                )

                IconButton(onClick = onLogout) {
                    Icon(Icons.Filled.Logout, contentDescription = "Logout", tint = White)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Summary Card ───────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "System Overview",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(10.dp))

                Text("Pending Receipts: $pendingCount")
                Text("Total Students: $totalStudents")
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Quick Actions ─────────────────────
        Text(
            text = "Admin Actions",
            style = MaterialTheme.typography.titleMedium,
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
                icon = Icons.Filled.CheckCircle,
                label = "Approve Payments",
                onClick = onApprovePayments,
                modifier = Modifier.weight(1f)
            )

            QuickActionCard(
                icon = Icons.Filled.People,
                label = "Students",
                onClick = onViewStudents,
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
                icon = Icons.Filled.Settings,
                label = "Tuition Config",
                onClick = onTuitionConfig,
                modifier = Modifier.weight(1f)
            )

        }

        Spacer(Modifier.height(24.dp))
    }
}