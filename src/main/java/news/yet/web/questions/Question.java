package news.yet.web.questions;

import java.time.OffsetDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
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

    @Version
    private Long version;

    @Size(min = 2, max = 30)
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

}