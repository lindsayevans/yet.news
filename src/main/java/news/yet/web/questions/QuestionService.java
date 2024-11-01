package news.yet.web.questions;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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

    public Question getQuestionBySubdomain(String subdomain) {
        var questions = repository.findBySubdomain(subdomain);
        if (questions.size() == 0) {
            return null;
        }

        var question = questions.get(0);

        return question;
    }

    public boolean checkSubdomainValidity(String subdomain) {
        var question = new Question();
        question.setSubdomain(subdomain);

        Set<ConstraintViolation<Question>> violations = validator.validate(question);
        if (!violations.isEmpty()) {
            return false;
        }

        return true;
    }

    public void createQuestion(Question question) {
        repository.insert(question);
    }

    public void updateQuestion(Question question) {
        repository.save(question);
    }

    public List<Question> getLatest(int count) {
        return repository.findAll(PageRequest.of(0, count, Sort.by("created").ascending())).toList();
    }

    public List<Question> getMostUpdated(int count) {
        return repository.findAll(PageRequest.of(0, count, Sort.by("version").descending())).toList();
    }

}
