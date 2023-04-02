package com.jeff.lim.wimk.database

data class Users(
    val uid: String? = null,
    val name: String? = null,
    val phoneNumber: String? = null,
    val role: RoleType? = null,
    val email: String? = null
)
