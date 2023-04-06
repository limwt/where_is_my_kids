package com.jeff.lim.wimk.database

data class UserModel(
    val uid: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val role: String = RelationType.Init.relation,
    val email: String = "",
)
