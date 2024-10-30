package news.yet.web;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "questions")
public class Question {

    @Id
    private String id;

    private String subdomain;

    private String question;

    private String answer;

    private Boolean editable;

    private String password;

}