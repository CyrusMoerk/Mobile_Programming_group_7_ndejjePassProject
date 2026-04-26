package com.example.ndejjepassproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ndejjepassproject.data.db.ClearanceDatabase
import com.example.ndejjepassproject.ui.navigation.AppNavGraph
import com.example.ndejjepassproject.ui.theme.Mobile_Programming_group_7_ndejjePassProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = ClearanceDatabase.getInstance(applicationContext)
        enableEdgeToEdge()
        setContent {
            Mobile_Programming_group_7_ndejjePassProjectTheme {
                AppNavGraph(db = db)
            }
        }
    }
}