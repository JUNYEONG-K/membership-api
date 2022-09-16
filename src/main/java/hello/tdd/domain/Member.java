package hello.tdd.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String email;
    private String password;

    // 연관 관계 주인 X -> 값이 등록되지 않 => 양쪽에 값을 설정해주는 연관관계 메서드 필요
    @OneToMany(mappedBy = "member") // 참조 객체명
    private List<Membership> membershipList;
}
