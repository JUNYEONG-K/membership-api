package hello.tdd.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class Membership {

    @Id
    @GeneratedValue
    @Column(name = "membership_id")
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

//    @Setter
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer point;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 연관관계의 주인
    @ManyToOne(fetch = FetchType.LAZY)  // 기본 = EAGER, N+1 문제 등의 이슈 때문에 가급적 지연로딩을 사용한다.
    @JoinColumn(name = "member_id") // 외래 키 지정(디비 컬럼명)
    private Member member;  // 참조 객체

    public void accumulatePoint(Integer point) {
        this.point = point;
    }
}
