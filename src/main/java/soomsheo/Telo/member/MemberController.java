package soomsheo.Telo.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soomsheo.Telo.member.domain.Member;
import soomsheo.Telo.member.dto.MemberDTO;
import soomsheo.Telo.member.dto.MemberTypeUpdateRequestDTO;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/signup")
    public Member signUp(@RequestBody Member member) {
        return memberService.saveMember(member);
    }

    @GetMapping("/{memberID}")
    public MemberDTO getMember(@PathVariable String memberID) throws Exception {
        Member member = memberService.findByMemberID(memberID);
        return new MemberDTO(
                member.getMemberID(),
                member.getMemberRealName(),
                member.getMemberNickName(),
                member.getPhoneNumber(),
                member.getProfile(),
                member.getProvider(),
                member.getMemberType()
        );
    }

    @PostMapping("/updateMemberType/{memberID}")
    public ResponseEntity<String> updateMemberType(
            @PathVariable String memberID,
            @RequestBody MemberTypeUpdateRequestDTO request) {
        try {
            memberService.updateMemberType(memberID, request.getMemberType());
            return new ResponseEntity<>("Member type updated successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{memberID}/memberType")
    public String getMemberType(@PathVariable String memberID) {
        Member member = memberService.findByMemberID(memberID);
        if (member != null) {
            System.out.println("memberID : " + memberID);
            return member.getMemberType();
        } else {
            return "Member not found";
        }
    }
}