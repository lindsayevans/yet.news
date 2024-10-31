package news.yet.web.homepage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import news.yet.web.questions.QuestionRepository;

@Controller
public class HomeController {
    @Autowired
    QuestionRepository repository;

    @Value("${hostExtension:.yet.news}")
    private String hostExtension;

    @Value("${mainHost:yet.news}")
    private String mainHost;

    @Value("${mainSite:https://yet.news/}")
    private String mainSite;

    private String getAnswer(String answerCode) {
        return answerCode.substring(0, 1).toUpperCase() + answerCode.substring(1).toLowerCase();
    }

    @GetMapping()
    public String home(HttpServletRequest request,
            Model model) {
        model.addAttribute("host", request.getServerName());
        model.addAttribute("mainSite", mainSite);
        var host = request.getServerName();

        if (!host.equals(mainHost)) {
            var subdomain = host.replaceAll(hostExtension, "");
            var questions = repository.findBySubdomain(subdomain);
            if (questions.size() == 0) {
                return "redirect:" + mainSite + "create?subdomain=" + subdomain;
            }

            var question = questions.get(0);

            model.addAttribute("subdomain", subdomain);
            model.addAttribute("question", question);
            model.addAttribute("answer", getAnswer(question.getAnswer()));

            return "subdomain";
        }

        return "home";
    }

}