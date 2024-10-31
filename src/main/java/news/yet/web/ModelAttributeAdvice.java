package news.yet.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ModelAttributeAdvice {

    @Value("${mainHost:yet.news}")
    private String mainHost;

    @Value("${mainSite:https://yet.news/}")
    private String mainSite;

    @Value("${hostExtension:.yet.news}")
    private String hostExtension;

    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        model.addAttribute("host", request.getServerName());
        model.addAttribute("mainHost", mainHost);
        model.addAttribute("mainSite", mainSite);
        model.addAttribute("hostExtension", hostExtension);
    }

}