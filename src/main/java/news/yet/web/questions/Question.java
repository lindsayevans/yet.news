package news.yet.web.questions;

import java.time.OffsetDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "questions")
public class Question {

    @Id
    private String id;

    @Version
    private Long version;

    @Size(min = 2, max = 255)
    @Pattern(regexp = "^\\p{Alnum}[\\p{Alnum}-]*\\p{Alnum}$", message = "Address can only contain letters, numbers & dashes")
    private String subdomain;

    private String question;

    @Pattern(regexp = "^(yes|no|maybe)$", message = "Invalid answer")
    private String answer;

    private Boolean editable;

    private String password;

    @CreatedDate
    private OffsetDateTime created;

    @LastModifiedDate
    private OffsetDateTime updated;

    public Question(String subdomain) {
        this.subdomain = subdomain;
    }

    public Question(String subdomain, String question, String answer, boolean editable, String password) {
        this.subdomain = subdomain;
        this.question = question;
        this.answer = answer;
        this.editable = editable;
        this.password = password;
    }

}