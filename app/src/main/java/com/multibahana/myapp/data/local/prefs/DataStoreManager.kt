package com.multibahana.myapp.data.local.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton


private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    val accessToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[ACCESS_TOKEN]
    }

    val refreshToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[REFRESH_TOKEN]
    }

    suspend fun saveTokens(access: String, refresh: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = access
            prefs[REFRESH_TOKEN] = refresh
        }
    }

    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }
}