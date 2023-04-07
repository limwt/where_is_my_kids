package com.jeff.lim.wimk.screen.parent

import com.jeff.lim.wimk.model.service.DatabaseService
import com.jeff.lim.wimk.model.service.LogService
import com.jeff.lim.wimk.screen.WimkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParentViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    logService: LogService
) : WimkViewModel(logService) {
    private val logTag = "[WIMK]${this::class.java.simpleName}"

    fun onFamilyChange(familyUid: String) {
        launchCatching {
            databaseService.getCurrentFamilyWithUid(familyUid)
        }
    }
}