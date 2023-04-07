package com.jeff.lim.wimk.screen.register

import androidx.compose.runtime.mutableStateOf
import com.jeff.lim.wimk.WimkRoutes
import com.jeff.lim.wimk.common.snack_bar.SnackBarManager
import com.jeff.lim.wimk.database.RelationType
import com.jeff.lim.wimk.model.service.AccountService
import com.jeff.lim.wimk.model.service.DatabaseService
import com.jeff.lim.wimk.model.service.LogService
import com.jeff.lim.wimk.screen.WimkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject
import com.jeff.lim.wimk.R.string as AppText

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val accountService: AccountService,
    private val databaseService: DatabaseService,
    logService: LogService
) : WimkViewModel(logService) {
    private val logTag = "[WIMK]${this::class.java.simpleName}"

    var uiState = mutableStateOf(RegisterUiState())
    private val relation
        get() = uiState.value.relation
    private val name
        get() = uiState.value.name
    private val phoneNumber
        get() = uiState.value.phoneNumber
    private val authKey
        get() = uiState.value.authKey

    fun onNameChange(newValue: String) {
        uiState.value = uiState.value.copy(name = newValue)
    }

    fun onPhoneNumberChange(newValue: String) {
        uiState.value = uiState.value.copy(phoneNumber = newValue)
    }

    fun onAuthKeyChange(newValue: String) {
        uiState.value = uiState.value.copy(authKey = newValue)
    }

    fun onRelationChange(newValue: String) {
        uiState.value = uiState.value.copy(relation = newValue)
    }

    fun onRegisterClick(openAndPopUp: (String, String, String) -> Unit) {
        launchCatching {
            when (relation) {
                RelationType.Dad.relation,
                RelationType.Mom.relation -> {
                    // TODO : 등록된 가족이 있을 경우 다른 부모를 등록할때??
                    databaseService.register("", name, phoneNumber, relation)
                    databaseService.getCurrentFamily().collect { family ->
                        Timber.tag(logTag).d("onLoginClick - currentFamily $relation $family")

                        family?.let { fam ->
                            when (fam.users[accountService.currentUserId]?.relation) {
                                RelationType.Dad.relation,
                                RelationType.Mom.relation -> {
                                    openAndPopUp(WimkRoutes.AuthKeyScreen.name, WimkRoutes.RegisterScreen.name, fam.familyUid ?: "")
                                }
                                RelationType.Son.relation,
                                RelationType.Daughter.relation -> {

                                }
                            }
                        }
                    }
                }
                RelationType.Son.relation,
                RelationType.Dad.relation -> {
                    databaseService.getCurrentFamilyWithAuthKey(authKey).collect { family ->
                        Timber.tag(logTag).d("onLoginClick - currentFamily $relation $family")

                        family?.let { fam ->
                            fam.familyUid?.let { uid ->
                                databaseService.register(uid, name, phoneNumber, relation)
                            }

                            // TODO : 자녀 화면으로 이동...
                            openAndPopUp(WimkRoutes.LogInScreen.name, WimkRoutes.RegisterScreen.name, "")
                        }

                    }
                }
                else -> {
                    SnackBarManager.showMessage(AppText.register_error)
                }
            }
        }
    }
}