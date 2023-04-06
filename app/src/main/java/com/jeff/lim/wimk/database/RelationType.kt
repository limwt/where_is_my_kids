package com.jeff.lim.wimk.database

enum class RelationType(val relation: String) {
    Init("---"),
    Mom("모"),
    Dad("부"),
    Son("아들"),
    Daughter("딸");

    companion object {
        fun getByName(relation: String?): RelationType {
            values().forEach { type -> if (relation == type.relation) return type }
            return Init
        }

        fun getOptions(): List<String> {
            val options = mutableListOf<String>()
            values().forEach { type -> options.add(type.relation) }
            return options
        }
    }
}