package io.jin.backend.api

import com.fasterxml.jackson.databind.ObjectMapper
import io.jin.backend.app.MemberService
import io.jin.backend.dto.Role
import io.jin.backend.exeptions.MemberNotFoundException
import io.jin.backend.util.genMemberDesc
import io.jin.backend.util.genMemberDescList
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post


@WebMvcTest(MemberApiController::class)
class MemberApiControllerSliceTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var service: MemberService

    @Autowired
    lateinit var om: ObjectMapper

    @Test
    fun `회원 저장 요청을 보내면 성공적으로 저장하고 회원 정보가 포함되어있는 응답을 200 OK로 내린다`() {

        val expectedName = "member1"
        val expectedEmail = "member1@email.com"
        val expectedRole = Role.BRONZE
        val expectedMsg = "회원이 성공적으로 등록되었습니다!"

        val expectedDesc = genMemberDesc(expectedName, expectedEmail, expectedRole)

        `when`(service.save(expectedDesc)).thenReturn(expectedDesc)

        mockMvc.post("/api/members") {
            contentType = MediaType.APPLICATION_JSON
            content = om.writeValueAsString(expectedDesc)
        }.andExpect {
            status { isOk() }
            jsonPath("$.message") { value(expectedMsg) }
            jsonPath("$.data.name") { value(expectedName) }
            jsonPath("$.data.email") { value(expectedEmail) }
            jsonPath("$.data.role") { value(expectedRole.name) }
        }

    }

    @Test
    fun `존재하는 email로 회원을 조회하면 규격에 맞는 응답을 반환한다`() {

        val expectedName = "member1"
        val expectedEmail = "member1"
        val expectedRole = Role.BRONZE
        val expectedMsg = "회원을 성공적으로 조회했습니다."

        val expectedDesc = genMemberDesc(expectedName, expectedEmail, expectedRole)

        `when`(service.getDescByEmail(expectedEmail)).thenReturn(expectedDesc)

        mockMvc.get("/api/members/${expectedEmail}")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.message") { value(expectedMsg) }
                jsonPath("$.data.name") { value(expectedName) }
                jsonPath("$.data.email") { value(expectedEmail) }
                jsonPath("$.data.role") { value(expectedRole.name) }
            }

    }

    @Test
    fun `존재하지 않는 email로 회원을 조회하면 규격에 맞는 오류 응답을 반환한다`() {

        val targetEmail = "UNAVAILABLE_EMAIL"
        val expectedMsg = "조건에 맞는 회원을 찾을 수 없습니다."

        `when`(service.getDescByEmail(targetEmail)).thenThrow(MemberNotFoundException())

        mockMvc.get("/api/members/$targetEmail")
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.message") { value(expectedMsg)}
            }
            .andDo { print() }


    }

    @Test
    fun `회원 목록을 요청하면 MemberView로 목록을 반환한다`() {

        val size = 10

        val memberDescList = genMemberDescList(size)
        val expectedMsg = "회원 목록을 정상적으로 조회했습니다!"

        `when`(service.getAllDescView())
            .thenReturn(memberDescList)

        val result = mockMvc.get("/api/members")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.message") { value(expectedMsg) }
                jsonPath("$.data.size()") { value(size) }
            }
            .andDo{ print() }
            .andReturn()

        val respStr = result.response.contentAsString
        val respJson = om.readTree(respStr)

        val respData = respJson["data"]

        for ( i in 0 until size ) {

            val expected = memberDescList[i]
            val actual = respData[i]

            Assertions.assertThat(expected.name).isEqualTo(actual["name"].asText())
            Assertions.assertThat(expected.email).isEqualTo(actual["email"].asText())
            Assertions.assertThat(expected.role.name).isEqualTo(actual["role"].asText())

        }

    }

}