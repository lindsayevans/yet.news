package news.yet.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    @Value("${hostExtension:.bx.local}")
    private String hostExtension;

    @GetMapping()
    public String home(HttpServletRequest request,
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        model.addAttribute("host", request.getServerName());
        var host = request.getServerName();

        if (!host.equals("localhost")) {
            var subdomain = host.replaceAll(hostExtension, "");
            // TODO:
            // - get question/answer from DB based on subdomain
            // - redirect to create form if not exist
            var question = "Is this working yet?";
            var answer = "Maybe";

            model.addAttribute("subdomain", subdomain);
            model.addAttribute("question", question);
            model.addAttribute("answer", answer);

            return "subdomain";
        }

        return "home";
    }

}