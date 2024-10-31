package news.yet.web.questions;

import org.springframework.beans.factory.annotation.Autowired;
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

}
