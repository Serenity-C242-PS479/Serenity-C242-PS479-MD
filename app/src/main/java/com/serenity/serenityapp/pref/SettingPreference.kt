package com.serenity.serenityapp.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.serenity.serenityapp.helper.getCurrentDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreference private constructor(private val dataStore: DataStore<Preferences>) {
    fun getToken(): Flow<String> {
        return dataStore.data.map { preference ->
            preference[AUTH_TOKEN_KEY] ?: ""
        }
    }

    suspend fun saveToken(authToken: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = authToken
        }
    }

    fun getUserId(): Flow<Int> {
        return dataStore.data.map { preference ->
            preference[USER_ID_TOKEN_KEY] ?: 0
        }
    }

    suspend fun saveUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[USER_ID_TOKEN_KEY] = userId
        }
    }

    fun isFocusModeEnabled(): Flow<Boolean> {
        return dataStore.data.map { preference ->
            preference[FOCUS_MODE_KEY] != null && preference[FOCUS_MODE_KEY] == true
        }
    }

    suspend fun saveFocusMode(enable: Boolean) {
        dataStore.edit { preferences ->
            preferences[FOCUS_MODE_KEY] = enable
        }
    }

    fun isAlreadyBreak(): Flow<Boolean> {
        return dataStore.data.map { preference ->
            preference[CURRENT_BREAK_DATE_KEY] == getCurrentDate()
        }
    }

    suspend fun refreshCurrentBreakDate() {
        dataStore.edit { preferences ->
            preferences[CURRENT_BREAK_DATE_KEY] = getCurrentDate()
        }
    }

    fun isCustomLimitStateEnabled(): Flow<Boolean> {
        return dataStore.data.map { preference ->
            preference[CUSTOM_LIMIT_STATE_KEY] != null && preference[CUSTOM_LIMIT_STATE_KEY] == true
        }
    }

    suspend fun saveCustomLimitState(enable: Boolean) {
        dataStore.edit { preferences ->
            preferences[CUSTOM_LIMIT_STATE_KEY] = enable
        }
    }

    fun getCustomLimitValue(): Flow<String> {
        return dataStore.data.map { preference ->
            preference[CUSTOM_LIMIT_VALUE_KEY] ?: ""
        }
    }

    suspend fun saveCustomLimitValue(value: String) {
        dataStore.edit { preferences ->
            preferences[CUSTOM_LIMIT_VALUE_KEY] = value
        }
    }

    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_TOKEN_KEY = intPreferencesKey("user_id")
        private val FOCUS_MODE_KEY = booleanPreferencesKey("focus_mode")
        private val CUSTOM_LIMIT_STATE_KEY = booleanPreferencesKey("custom_limit_state")
        private val CUSTOM_LIMIT_VALUE_KEY = stringPreferencesKey("custom_limit_value")
        private val CURRENT_BREAK_DATE_KEY = stringPreferencesKey("current_break_date")

        @Volatile
        private var INSTANCE: SettingPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}