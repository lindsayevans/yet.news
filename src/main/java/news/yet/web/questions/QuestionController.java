package news.yet.web.questions;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.apache.commons.lang.RandomStringUtils;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @GetMapping("/availability")
    @ResponseBody
    public Map<String, Boolean> availability(
            @RequestParam(name = "subdomain", required = true) String subdomain) {

        var existing = questionService.getQuestionBySubdomain(subdomain);
        var valid = questionService.checkSubdomainValidity(subdomain);

        return Collections.singletonMap("available", existing == null && valid);
    }

    @GetMapping("/list")
    public String list(Model model) {
        var latest = questionService.getLatest(5);
        model.addAttribute("latest", latest);

        var greatest = questionService.getMostUpdated(5);
        model.addAttribute("greatest", greatest);

        return "list";
    }

    @GetMapping("/create")
    public String create(HttpServletRequest request,
            @RequestParam(name = "subdomain", required = false, defaultValue = "") String subdomain,
            @RequestParam(name = "question", required = false, defaultValue = "") String question,
            Model model) {

        var answer = "maybe";
        if (!question.equals("") && subdomain.equals("")) {
            subdomain = question.replaceAll("([^a-zA-Z]+)", "-").toLowerCase();
        }

        var password = RandomStringUtils.randomAlphanumeric(7);

        var questionModel = new Question(subdomain, question, answer, false, password);

        model.addAttribute("question", questionModel);

        return "create";
    }

    @PostMapping("/create")
    @RateLimiter(name = "basic")
    public String createPost(@Valid Question question, BindingResult bindingResult,
            Model model) {
        var existing = questionService.getQuestionBySubdomain(question.getSubdomain());
        if (existing != null) {
            bindingResult.addError(new FieldError("question", "subdomain",
                    question.getSubdomain(), true, null, null,
                    "Subdomain is not available"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "create";
        }

        questionService.createQuestion(question);

        var hostExtension = (String) model.getAttribute("hostExtension");
        var url = "https://" + question.getSubdomain() + hostExtension;

        model.addAttribute("url", url);

        return "success";
    }

    @GetMapping("/edit")
    public String edit(HttpServletRequest request,
            @RequestParam(name = "subdomain", required = false, defaultValue = "") String subdomain,
            Model model) {

        var mainSite = (String) model.getAttribute("mainSite");
        var existing = questionService.getQuestionBySubdomain(subdomain);
        if (existing == null) {
            return "redirect:" + mainSite + "create?subdomain=" + subdomain;
        }

        existing.setPassword("");

        model.addAttribute("subdomain", subdomain);
        model.addAttribute("question", existing);
        model.addAttribute("answer", questionService.getDisplayAnswer(existing.getAnswer()));

        return "edit";

    }

    @PostMapping("/edit")
    @RateLimiter(name = "basic")
    public String editPost(@Valid Question question, BindingResult bindingResult,
            Model model) {

        var existing = questionService.getQuestionBySubdomain(question.getSubdomain());

        if (existing == null || (!existing.getEditable() &&
                !existing.getPassword().equals(question.getPassword()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        existing.setAnswer(question.getAnswer());
        questionService.updateQuestion(existing);

        var hostExtension = (String) model.getAttribute("hostExtension");
        var url = "https://" + question.getSubdomain() + hostExtension;

        return "redirect:" + url;
    }

}