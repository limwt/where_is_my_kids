package com.jeff.lim.wimk.screen.register

data class RegisterUiState(
    val relation: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val authKey: String = "",
    val isShowAuthKeyView: Boolean = false,
)
