package io.jin.backend.util

import io.jin.backend.domain.Member
import io.jin.backend.dto.MemberDescription
import io.jin.backend.dto.Role


fun genMember(tagetName: String, targetEmail: String, targetRole: Role = Role.BRONZE): Member = Member(null, tagetName, targetEmail, targetRole)

fun genMemberDesc(
    actualName: String,
    actualEmail: String,
    actualRole: Role
): MemberDescription = MemberDescription(actualName, actualEmail, actualRole)

fun genMemberList(size: Int): List<Member> {
    val result: MutableList<Member> = mutableListOf()

    for (i in 1..size) {
        result.add(genMember("member$i", "member${i}@email.com"))
    }

    return result
}

fun genMemberDescList(size: Int): List<MemberDescription> {
    val result: MutableList<MemberDescription> = mutableListOf()

    for (i in 1..size) {
        result.add(genMemberDesc("member$i", "member${i}@email.com", Role.BRONZE))
    }

    return result
}


