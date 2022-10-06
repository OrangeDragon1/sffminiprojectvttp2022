package vttp2022.sff.batch2_mini_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import vttp2022.sff.batch2_mini_project.repositories.FlightOfferRepository;

@Controller
@RequestMapping("/deleteUser")
public class DeleteUser {

    @Autowired FlightOfferRepository foRepo;

    @PostMapping
    public String postDeleteUser(
            @RequestBody MultiValueMap<String, String> form,
            Model model) {
        
        String name = form.getFirst("name");
        String upperName = name.toUpperCase();

        Boolean deleted = foRepo.deleteKey(upperName);

        model.addAttribute("name", upperName);
        model.addAttribute("deleted", deleted);

        return "deleteUser";
    }
}
