package io.jin.backend.dao

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jin.backend.domain.Member
import io.jin.backend.dto.MemberView
import io.jin.backend.util.genMember
import io.jin.backend.util.genMemberList
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime


//val logger = LoggerFactory.getLogger(MemberRepositoryTests::class.java)

private val log = KotlinLogging.logger {}

//@DataJpaTest
@SpringBootTest
class MemberRepositoryTests @Autowired constructor(
    var repository: MemberRepository
) {

    @Test
    fun `repository 주입 테스트`() {

//        println()
        log.info { repository }
        assertThat(repository).isNotNull

    }

    @Test
    fun `회원을 생성해서 저장하면 id와 생성날짜, 수정날짜가 자동으로 등록된다`() {

        val tagetName = "member1"
        val targetEmail = "member1@email.com"

        val member = genMember(tagetName, targetEmail)

        val saved: Member = repository.save(member)

        assertThat(saved.createdAt).isNotNull
        assertThat(saved.updatedAt).isNotNull
        assertThat(saved.id).isNotNull

        val now = LocalDateTime.now()

        assertThat(saved.createdAt).isBefore(now)
        assertThat(saved.updatedAt).isBefore(now)

        log.info { saved.id }
        log.info { saved.createdAt }
        log.info { saved.updatedAt }

    }

    @Test
    fun `회원 저장 후 findAllMemberView 메서드를 통해서 리스트를 불러오면 MemberView 타입으로 불러올 수 있다`() {

        val size = 10

        val memberList = genMemberList(size)
        repository.saveAll(memberList)

        val descList: List<MemberView> = repository.findAllMemberView()

        Assertions.assertThat(descList.size).isEqualTo(size)

        descList.forEachIndexed { idx, actual ->
            val expected = memberList[idx]

            Assertions.assertThat(actual.name).isEqualTo(expected.name)
            Assertions.assertThat(actual.email).isEqualTo(expected.email)
            Assertions.assertThat(actual.role).isEqualTo(expected.role)
        }

    }



}