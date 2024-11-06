package news.yet.web.questions;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class QuestionService {
    @Autowired
    QuestionRepository repository;

    @Autowired
    private Validator validator;

    public String getDisplayAnswer(String answerCode) {
        return answerCode.substring(0, 1).toUpperCase() + answerCode.substring(1).toLowerCase();
    }

    @Cacheable("question")
    public Question getQuestionBySubdomain(String subdomain) {
        return getQuestionBySubdomain(subdomain, false);
    }

    public Question getQuestionBySubdomain(String subdomain, boolean bypassCache) {
        var questions = repository.findBySubdomain(subdomain);
        if (questions.size() == 0) {
            return null;
        }

        var question = questions.get(0);

        return question;
    }

    public boolean checkSubdomainValidity(String subdomain) {
        var question = new Question(subdomain);

        Set<ConstraintViolation<Question>> violations = validator.validate(question);
        if (!violations.isEmpty()) {
            return false;
        }

        return true;
    }

    @CacheEvict("latestQuestions")
    public void createQuestion(Question question) {
        repository.insert(question);
    }

    @CacheEvict(value = { "question", "mostUpdatedQuestions" }, allEntries = true)
    public void updateQuestion(Question question) {
        repository.save(question);
    }

    @Cacheable("latestQuestions")
    public List<Question> getLatest(int count) {
        return repository.findAll(PageRequest.of(0, count, Sort.by("created").descending())).toList();
    }

    @Cacheable("mostUpdatedQuestions")
    public List<Question> getMostUpdated(int count) {
        return repository.findAll(PageRequest.of(0, count, Sort.by("version").descending())).toList();
    }

}
