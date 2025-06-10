package io.jin.backend.domain

import io.jin.backend.dto.MemberUpdateDescription
import io.jin.backend.dto.Role
import io.jin.backend.global.TimeStamp
import jakarta.persistence.*


@Entity
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,

    var email: String,

    @Enumerated(EnumType.STRING)
    var role: Role,

    ) : TimeStamp() {

    fun update(desc: MemberUpdateDescription) {

        this.name = desc.name
        this.role = desc.role

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (id != other.id) return false
        if (name != other.name) return false
        if (email != other.email) return false
        if (role != other.role) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + role.hashCode()
        return result
    }

}