package com.jeff.lim.wimk.screen.auth_key

data class AuthKeyUiState(
    val authKey: String = key(),
    val kidAdded: Boolean = false
)

private fun key(): String {
    val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..8)
        .map { charset.random() }
        .joinToString("")
}
