package artem.tamilin.bootcounter.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

class NotificationDismissRepository(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val NOTIFICATION_DISMISS_KEY = booleanPreferencesKey("notification_dismiss")
    }

    suspend fun isNotificationDismissed(): Boolean {
        return dataStore.data.first()[NOTIFICATION_DISMISS_KEY] ?: false
    }

    suspend fun setNotificationDismissStatus(isDismissed: Boolean) {
        dataStore.edit { pref ->
            pref[NOTIFICATION_DISMISS_KEY] = isDismissed
        }
    }
}