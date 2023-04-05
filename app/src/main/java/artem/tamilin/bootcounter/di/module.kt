package artem.tamilin.bootcounter.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import artem.tamilin.bootcounter.MainViewModel
import artem.tamilin.bootcounter.db.BootDataBase
import artem.tamilin.bootcounter.db.NotificationDismissRepository
import artem.tamilin.bootcounter.db.dao.BootDao
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("local_db")

val appModule = module {
    single<BootDataBase> {
        Room.databaseBuilder(
            context = androidApplication(),
            klass = BootDataBase::class.java,
            name = "boot_database"
        ).build()
    }
    single<BootDao> {
        val db = get<BootDataBase>()
        db.bootDao()
    }
    viewModel { MainViewModel(bootDao = get()) }
    single { NotificationDismissRepository(dataStore = androidApplication().dataStore) }
}