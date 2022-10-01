package vttp2022.sff.batch2_mini_project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/search")
public class SearchController {

    @PostMapping
    public String postCart(
            @RequestBody MultiValueMap<String, String> form, 
            Model model) {
        
        String dateNow = java.time.LocalDate.now().toString();
        String dateLater = java.time.LocalDate.now().plusYears(1).toString();
        String name = form.getFirst("name");

        model.addAttribute("name", name.toUpperCase());
        model.addAttribute("dateNow", dateNow);
        model.addAttribute("dateLater", dateLater);

        return "search";
    }

    @GetMapping
    public String getMethod() {
        return null;
    }
}
