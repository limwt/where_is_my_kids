package com.jeff.lim.wimk.database

data class Users(
    val uid: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val role: String = RoleType.Init.role,
    val email: String = "",
    val authNum: String = ""
)
