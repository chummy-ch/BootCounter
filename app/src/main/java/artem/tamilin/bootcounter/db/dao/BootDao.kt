package artem.tamilin.bootcounter.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import artem.tamilin.bootcounter.db.entity.Boot

@Dao
interface BootDao {

    @Query("SELECT * FROM boot")
    suspend fun getAll(): List<Boot>

    @Insert
    suspend fun insertBoot(boot: Boot)
}