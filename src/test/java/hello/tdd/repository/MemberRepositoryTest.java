package hello.tdd.repository;

import hello.tdd.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void MemberRepository가Null아님() {
        assertThat(memberRepository).isNotNull();
    }

    @Test
    void 멤버등록() {
        // given
        Member member = Member.builder()
                .name("test")
                .email("test@naver.com")
                .password("1234")
                .build();
        // when
        Member result = memberRepository.save(member);
        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("test");
        assertThat(result.getEmail()).isEqualTo("test@naver.com");
        assertThat(result.getPassword()).isEqualTo("1234");
    }

    @Test
    void 멤버존재여부() {
        // given
        Member member = Member.builder()
                .name("test")
                .email("test@naver.com")
                .password("1234")
                .build();
        // when
        Member savedResult = memberRepository.save(member);
        Member findResult = memberRepository.findByEmail("test@naver.com");
        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getId()).isNotNull();
        assertThat(findResult).isEqualTo(savedResult);
    }
}
