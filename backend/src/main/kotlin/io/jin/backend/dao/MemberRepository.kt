package io.jin.backend.dao

import io.jin.backend.domain.Member
import io.jin.backend.dto.MemberView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?

//    @Query("""
//        select
//            new io.eddie.backend.dto.MemberDescription(m.name, m.email, m.role)
//        from
//            Member m
//    """)
//    fun findAllDescription()

    @Query("""
        select
            m.name as name, 
            m.email as email, 
            m.role as role
        from 
            Member m
    """)
    fun findAllMemberView(): List<MemberView>


}