package news.yet.web;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

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

        System.out.println("hostExtension: " + hostExtension);
        System.out.println("mainHost: " + mainHost);
        System.out.println("mainSite: " + mainSite);

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

    @GetMapping("/availability")
    @ResponseBody
    public Map<String, Boolean> availability(
            @RequestParam(name = "subdomain", required = true) String subdomain) {

        var existing = repository.findBySubdomain(subdomain);

        return Collections.singletonMap("available", existing.size() == 0);
    }

    @GetMapping("/create")
    public String create(HttpServletRequest request,
            @RequestParam(name = "subdomain", required = false, defaultValue = "") String subdomain,
            @RequestParam(name = "question", required = false, defaultValue = "") String question,
            Model model) {

        var answer = "maybe";
        if (!question.equals("")) {
            subdomain = question.replaceAll("([^a-zA-Z]+)", "-").toLowerCase();
        }

        var questionModel = new Question();
        questionModel.setSubdomain(subdomain);
        questionModel.setQuestion(question);
        questionModel.setAnswer(answer);
        questionModel.setEditable(false);

        model.addAttribute("question", questionModel);
        model.addAttribute("hostExtension", hostExtension);

        return "create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute Question question, BindingResult bindingResult,
            Model model) {
        var existing = repository.findBySubdomain(question.getSubdomain());
        if (existing.size() > 0) {
            bindingResult.addError(new FieldError("question", "subdomain",
                    question.getSubdomain(), true, null, null,
                    "Subdomain is not available"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            model.addAttribute("hostExtension", hostExtension);
            return "create";
        }

        repository.insert(question);

        var url = "https://" + question.getSubdomain() + hostExtension;

        model.addAttribute("url", url);
        model.addAttribute("hostExtension", hostExtension);

        return "success";
    }

}