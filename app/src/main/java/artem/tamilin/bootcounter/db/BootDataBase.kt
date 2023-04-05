package artem.tamilin.bootcounter.db

import androidx.room.Database
import androidx.room.RoomDatabase
import artem.tamilin.bootcounter.db.dao.BootDao
import artem.tamilin.bootcounter.db.entity.Boot


@Database(entities = [Boot::class], version = 1)
abstract class BootDataBase : RoomDatabase() {
    abstract fun bootDao(): BootDao
}