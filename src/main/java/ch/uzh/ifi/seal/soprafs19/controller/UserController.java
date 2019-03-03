package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    // This is what we get, if we call the page
    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }

    @GetMapping ("/users/{id}")
    User getUser(@PathVariable long id){return service.getUser(id);}

    @DeleteMapping ("/users")
    @ResponseStatus (HttpStatus.ACCEPTED)
    User deleteUser(@PathVariable long id){this.service.deleteUser(id);
    return null;
    }

    // This is done when we press the button
    @PostMapping("/users")
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }
}
