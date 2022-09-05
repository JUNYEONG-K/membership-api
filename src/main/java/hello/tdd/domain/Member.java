package hello.tdd.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
