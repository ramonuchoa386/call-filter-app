package com.example.callfilter.adapter.input.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Config") },
                    label = { Text("Config") },
                    selected = currentRoute == "settings",
                    onClick = { navController.navigate("settings") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Phone, contentDescription = "Histórico") },
                    label = { Text("Histórico") },
                    selected = currentRoute == "history",
                    onClick = { navController.navigate("history") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Warning, contentDescription = "Bloqueados") },
                    label = { Text("Bloqueados") },
                    selected = currentRoute == "blocked",
                    onClick = { navController.navigate("blocked") }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "settings",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("settings") { SettingsScreen() }
            composable("history") { HistoryScreen() }
            composable("blocked") { BlockedContactsScreen() }
        }
    }
}