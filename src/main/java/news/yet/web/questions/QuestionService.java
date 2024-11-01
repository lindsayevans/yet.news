package news.yet.web.questions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    @Autowired
    QuestionRepository repository;

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
