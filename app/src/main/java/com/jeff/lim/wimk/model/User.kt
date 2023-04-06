package com.jeff.lim.wimk.model

import com.jeff.lim.wimk.database.RelationType

data class User(
    val id: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val relation: String = RelationType.Init.relation,
    val email: String = "",
)
