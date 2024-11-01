package news.yet.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import news.yet.web.questions.QuestionService;

@SpringBootTest
public class QuestionServiceTests {

    @Autowired
    private QuestionService service;

    @Test
    void contextLoads() throws Exception {
        assertThat(service).isNotNull();
    }

    @Test
    void validSubdomainsPass() {
        var valid1 = service.checkSubdomainValidity("a1");
        assertThat(valid1).isTrue();
        var valid2 = service.checkSubdomainValidity("abc-123");
        assertThat(valid2).isTrue();
        var valid3 = service.checkSubdomainValidity("a-1");
        assertThat(valid3).isTrue();
    }

    @Test
    void invalidSubdomainsFail() {
        var valid1 = service.checkSubdomainValidity("abc-");
        assertThat(valid1).isFalse();
        var valid2 = service.checkSubdomainValidity("abc 123");
        assertThat(valid2).isFalse();
        var valid3 = service.checkSubdomainValidity("abc_123");
        assertThat(valid3).isFalse();
    }

}
