package news.yet.web;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "questions", path = "questions")
public interface QuestionRepository extends MongoRepository<Question, String> {

    List<Question> findBySubdomain(@Param("subdomain") String subdomain);

}