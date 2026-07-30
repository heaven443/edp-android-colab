package com.example.lab_activity_5

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    private object PreferencesKeys {
        val COUNT = intPreferencesKey("count")
        val NAME = stringPreferencesKey("name")
    }

    val countFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.COUNT] ?: 0
    }

    val nameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.NAME] ?: ""
    }

    suspend fun saveCount(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.COUNT] = count
        }
    }

    suspend fun saveName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NAME] = name
        }
    }
}
