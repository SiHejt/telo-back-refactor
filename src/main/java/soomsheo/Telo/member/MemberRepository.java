package soomsheo.Telo.member;

import org.springframework.data.jpa.repository.JpaRepository;
import soomsheo.Telo.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByMemberID(String memberID);
}