package io.jin.backend.api;

import io.jin.backend.app.MemberService
import io.jin.backend.dto.GeneralResponse
import io.jin.backend.dto.MemberDescription
import io.jin.backend.dto.MemberView
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class MemberApiController(
    private val service: MemberService
) {

    @GetMapping
    fun getMemberViews(): GeneralResponse<List<MemberView>> {
        return GeneralResponse(
            data = service.getAllDescView(),
            message = "회원 목록을 정상적으로 조회했습니다!"
        )
    }

    // CRUD
    @PostMapping
    fun saveMember(
        @Valid @RequestBody saveRequest: MemberDescription
    ): GeneralResponse<MemberDescription> {
        return GeneralResponse(
            data = service.save(saveRequest),
            message = "회원이 성공적으로 등록되었습니다!"
        )
    }

    @GetMapping("/{email}")
    fun getMemberDescByEmail(
        @PathVariable email: String
    ): GeneralResponse<MemberDescription> {
        return GeneralResponse(
            data = service.getDescByEmail(email),
            message = "회원을 성공적으로 조회했습니다."
        )
    }


}