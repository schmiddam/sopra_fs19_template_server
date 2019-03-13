package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
//import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*; //includes all mappings


//communicate with db

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
    /*
    @DeleteMapping ("/users")
    @ResponseStatus (HttpStatus.ACCEPTED)
    User deleteUser(@PathVariable long id){this.service.deleteUser(id);
    return null;
    }
    */

    // This is done when we press the button
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED) // 201
    String createUser(@RequestBody User newUser) {
        String username = newUser.getUsername();
        if (this.service.getUserByUsername(username) != null)
            throw new UserAlreadyExists();
        else {
            User createdUser = this.service.createUser(newUser);
            String url = "/users/"+ createdUser.getId();
            return url;
        }
    }

    @PostMapping("/logcheck")
    User checkCredentials(@RequestBody User newUser){
        String username = newUser.getUsername();
        String password = newUser.getPassword();
        if ((this.service.getUserByUsername(username)==null)||
                (!this.service.getUserByUsername(username).getPassword().equals(password)))
            throw new UserDoesntExist();
        else {
            this.service.getUserByUsername(username).setStatus(UserStatus.ONLINE);
            this.service.saveLogin(this.service.getUserByUsername(username));
            return this.service.getUserByUsername(username);
        }
    }
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    User getId(@PathVariable long id) {
        User user = this.service.getUser(id);
        if (user == null) throw new UserDoesntExist();
        else return user;
    }


    @PutMapping("/users/{id}")
    @CrossOrigin
    @ResponseStatus(value=HttpStatus.NO_CONTENT) //204 für PutMapping
    User setId(@PathVariable long id, @RequestBody User updatedUser) {
        User user = this.service.getUser(id);
        if (user == null) throw new UserDoesntExist();
        else return this.service.updateUser(id, updatedUser);
    }

    @GetMapping("/logout/{id}")
    @CrossOrigin
    //@ResponseStatus(value=HttpStatus.NO_CONTENT) //204 für PutMapping
    User setIdForLogout(@PathVariable long id) {
        User user = this.service.getUser(id);
        if (user == null) throw new UserDoesntExist();
        else user.setStatus(UserStatus.OFFLINE);
        this.service.saveLogout(user);
        return user;
    }
    /*
    @PostMapping("/logout/{id}")
    @CrossOrigin
    User logOutUser(@PathVariable long id) {
        User user = this.service.getUser(id);
        if (user == null) throw new UserDoesntExist();
        else
        user.setStatus(UserStatus.OFFLINE);
        this.service.saveLogout(user);
        return user;
    }
    */
}