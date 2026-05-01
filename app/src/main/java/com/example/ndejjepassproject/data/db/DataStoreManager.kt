package com.example.ndejjepassproject.data.db

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "app_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        private val KEY_DB_SEEDED = booleanPreferencesKey("db_seeded")
    }

    suspend fun isDatabaseSeeded(): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[KEY_DB_SEEDED] == true
    }

    suspend fun setDatabaseSeeded() {
        context.dataStore.edit { prefs ->
            prefs[KEY_DB_SEEDED] = true
        }
    }
}