package hello.tdd.service;

import hello.tdd.domain.Member;
import hello.tdd.error.MemberErrorResult;
import hello.tdd.error.MemberException;
import hello.tdd.repository.MemberRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final MemberRepository memberRepository;

    public String createToken(Member member) {
        Date now = new Date();
        JwtBuilder jwt = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .claim("id", member.getId())
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .signWith(SignatureAlgorithm.HS256, "secret")
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(60).toMillis()));
        return jwt.compact();
    }

    private Claims parseToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new MemberException(MemberErrorResult.NO_TOKEN);
        }
        String token = authorizationHeader.substring("Bearer ".length());

        try {
            return Jwts.parser()
                    .setSigningKey("secret")
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new JwtException("ExpiredToken");
        }
    }

    public Long getMemberId(String authorizationHeader) {
        Claims token = parseToken(authorizationHeader);
        return Long.valueOf(token.get("id").toString());
    }

    public Member getMember(String authorizationHeader) {
        Claims token = parseToken(authorizationHeader);
        Long id = Long.valueOf(token.get("id").toString());
        Optional<Member> optionalMember = memberRepository.findById(id);
        return optionalMember.orElseThrow(() -> new MemberException(MemberErrorResult.NO_MEMBER_ID));
    }
}
