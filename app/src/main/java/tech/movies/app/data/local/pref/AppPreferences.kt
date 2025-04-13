package tech.movies.app.data.local.pref

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    private val prefs: SharedPreferences
) {
    companion object {
        private const val KEY_LAST_SYNC = "last_sync_epoch_ms"

    }

    suspend fun saveLastSyncTime(epochMillis: Long = System.currentTimeMillis()) {
        withContext(Dispatchers.IO) {
            prefs.edit().putLong(KEY_LAST_SYNC, epochMillis).apply()
        }
    }

    suspend fun lastSyncTime(): Long {
        return withContext(Dispatchers.IO) {
            prefs.getLong(KEY_LAST_SYNC, 0L)
        }
    }
}