package artem.tamilin.bootcounter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import artem.tamilin.bootcounter.db.dao.BootDao
import artem.tamilin.bootcounter.db.entity.Boot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val bootDao: BootDao
) : ViewModel() {

    private val _boots = MutableStateFlow(
        listOf<Boot>()
    )
    val boots = _boots.asStateFlow()

    init {
        viewModelScope.launch {
            _boots.emit(bootDao.getAll())
        }
    }

}