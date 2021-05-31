package hr.dice.coronavirus.app.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import hr.dice.coronavirus.app.common.DATA_STORE_USER_SELECTION_NAME
import hr.dice.coronavirus.app.common.EMPTY_STRING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class DataStoreSelectionManager(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_USER_SELECTION_NAME)

    companion object {
        val PREF_KEY_NAME = stringPreferencesKey("user_selection")
    }

    suspend fun storeUserSelection(selection: String) {
        context.dataStore.edit {
            it[PREF_KEY_NAME] = selection
        }
    }

    val userSelection: Flow<String> = context.dataStore.data
        .map {
            it[PREF_KEY_NAME] ?: EMPTY_STRING
        }.catch {
            emit(EMPTY_STRING)
        }
}
