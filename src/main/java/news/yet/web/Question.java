package news.yet.web;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Document(collection = "questions")
public class Question {

    @Id
    private String id;

    @Size(min = 2, max = 30)
    @Pattern(regexp = "^\\w[\\w-]+\\w$", message = "Address can only contain letters, numbers & dashes")
    private String subdomain;

    private String question;

    @Pattern(regexp = "^(yes|no|maybe)$", message = "Invalid answer")
    private String answer;

    private Boolean editable;

    private String password;

}