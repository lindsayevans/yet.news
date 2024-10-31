package news.yet.web.homepage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import news.yet.web.questions.QuestionService;

@Controller
public class HomeController {
    @Autowired
    QuestionService questionService;

    @Value("${hostExtension:.yet.news}")
    private String hostExtension;

    @Value("${mainHost:yet.news}")
    private String mainHost;

    @Value("${mainSite:https://yet.news/}")
    private String mainSite;

    @GetMapping()
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("host", request.getServerName());
        model.addAttribute("mainSite", mainSite);
        var host = request.getServerName();

        if (!host.equals(mainHost)) {
            var subdomain = host.replaceAll(hostExtension, "");

            var question = questionService.getQuestionBySubdomain(subdomain);
            if (question == null) {
                return "redirect:" + mainSite + "create?subdomain=" + subdomain;
            }

            model.addAttribute("subdomain", subdomain);
            model.addAttribute("question", question);
            model.addAttribute("answer", questionService.getDisplayAnswer(question.getAnswer()));

            return "subdomain";
        }

        return "home";
    }

}