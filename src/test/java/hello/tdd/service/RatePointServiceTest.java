package hello.tdd.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RatePointServiceTest {

    @InjectMocks
    private RatePointService target;

    @Test
    void _10000원의적립은100원() {
        // given
        int price = 10000;
        // when
        int result = target.calculateAmount(price);
        // then
        assertThat(result).isEqualTo(100);
    }
}
