package io.jin.backend.app

import io.jin.backend.dao.MemberRepository
import io.jin.backend.domain.Member
import io.jin.backend.dto.MemberDescription
import io.jin.backend.dto.MemberUpdateDescription
import io.jin.backend.dto.MemberView
import io.jin.backend.exeptions.MemberNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val repository: MemberRepository
) {

    // CRUD
    @Transactional
    fun save(desc: MemberDescription): MemberDescription {
        val member = Member(
            name = desc.name,
            email = desc.email,
            role = desc.role,
        )
        repository.save(member)
        return desc
    }

    @Transactional(readOnly = true)
    fun findByEmail(email: String): Member {
//        return repository.findByEmail(email) ?: throw NoSuchElementException("해당 회원은 존재하지 않습니다.")
        return repository.findByEmail(email) ?: throw MemberNotFoundException()
    }

    @Transactional(readOnly = true)
    fun getDescByEmail(email: String): MemberDescription {
        val find = findByEmail(email)
        return MemberDescription(
            name = find.name,
            email = find.email,
            role = find.role,
        )
    }

    @Transactional
    fun update(email: String, updateDescription: MemberUpdateDescription): MemberUpdateDescription {

        val findMember = findByEmail(email)

        findMember.update(updateDescription)

        return MemberUpdateDescription(
            name = findMember.name,
            role = findMember.role,
        )
    }

    @Transactional
    fun delete(email: String) {

        val findMember = findByEmail(email)
        repository.delete(findMember)

    }

    @Transactional(readOnly = true)
    fun getAllDescView(): List<MemberView> {
        return repository.findAllMemberView()
    }


}