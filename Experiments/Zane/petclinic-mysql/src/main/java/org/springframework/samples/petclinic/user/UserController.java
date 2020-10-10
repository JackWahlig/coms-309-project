package org.springframework.samples.petclinic.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@Controller
class UserController {

    @Autowired
    private UserRepository usersRepository;

    @GetMapping("/users/new")
    public String initCreationForm(Map<String, Object> model) {
        Users user = new Users();
        model.put("user", user);
        return "users/createOrUpdateUserForm";
    }

    @PostMapping("/users/new")
    public String processCreationForm(@Valid Users user, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "users/createOrUpdateUserForm";
        } else {
            usersRepository.save(user);
            return "redirect:/users";
        }
    }

    @GetMapping("/users")
    public String getAllUsers(Map<String, Object> model) {
        Collection<Users> results = usersRepository.findAll();
        model.put("selections", results);
        return "users/usersList";
    }

    @GetMapping("/users/{userId}")
    public String findUserById(@PathVariable("userId") int id, Map<String, Object> model) {
        Collection<Users> results = usersRepository.findById(id);
        model.put("selections", results);
        return "users/usersList";
    }

    @GetMapping("/users/delete/{userId}")
    public String deleteUserById(@PathVariable("userId") int id, Map<String, Object> model) {
        Users user = usersRepository.findDistinctById(id);
        usersRepository.delete(user);
        Collection<Users> results = usersRepository.findAll();
        model.put("selections", results);
        return "users/usersList";
    }

    @GetMapping("/users/find")
    public String findUser(Map<String, Object> model) {
        model.put("user", new Users());
        return "users/findUsers";
    }

    @PostMapping("/users/find")
    public String processFindUser(Map<String, Object> model) {

        return "welcome";
    }

}
