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

    fun onKidListChange() {
        // TODO : DB 로 부터 등록된 자녀를 불러 온다.
    }
}