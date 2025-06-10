package io.jin.backend.dto

enum class Role {
    BRONZE, SILVER, GOLD, PLATINUM, DIAMOND
}

interface MemberView {
    val name: String
    val email: String
    val role: Role
}