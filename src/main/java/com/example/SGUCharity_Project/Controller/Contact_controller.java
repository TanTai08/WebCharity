package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Model.Contact_model;
import com.example.SGUCharity_Project.Repository.Contact_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Contact_controller {
    @Autowired
    Contact_Repo contactRepo;

    @GetMapping("lien-he")
    public String contact(Model model) {
       return "page_user/Contact";
    }

    @PostMapping("/insert/contact")
    public String insertcontact(@RequestParam("name") String name,
                                @RequestParam("email") String email,
                                @RequestParam("subject") String subject,
                                @RequestParam("message") String message) {

        Contact_model contactModel = new Contact_model();
        contactModel.setUser_fullname(name);
        contactModel.setUser_email(email);
        contactModel.setUser_topic(subject);
        contactModel.setUser_comment(message);

        contactRepo.save(contactModel);

        return "redirect:/lien-he";
    }
}
