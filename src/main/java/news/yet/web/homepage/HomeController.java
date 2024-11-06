package news.yet.web.homepage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import news.yet.web.questions.QuestionService;

@Controller
public class HomeController {
    @Autowired
    QuestionService questionService;

    @GetMapping()
    public String home(HttpServletRequest request, Model model) {
        var host = (String) model.getAttribute("host");
        var mainHost = (String) model.getAttribute("mainHost");
        var mainSite = (String) model.getAttribute("mainSite");
        var hostExtension = (String) model.getAttribute("hostExtension");

        if (host != null && !host.equals(mainHost)) {
            var subdomain = host.replaceAll(hostExtension, "");

            var question = questionService.getQuestionBySubdomain(subdomain, true);
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

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }
}