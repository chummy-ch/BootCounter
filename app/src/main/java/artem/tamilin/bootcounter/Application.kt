package artem.tamilin.bootcounter

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import artem.tamilin.bootcounter.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : android.app.Application() {
    companion object {
        const val CHANNEL_ID = "main_chanel"
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        startKoin {
            androidContext(this@Application)
            modules(appModule)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}