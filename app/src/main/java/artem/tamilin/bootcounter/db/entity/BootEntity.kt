package artem.tamilin.bootcounter.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Boot(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long
)