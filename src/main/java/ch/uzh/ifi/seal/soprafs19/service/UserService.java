package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ALWAYS adds the user and returns all users
    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User getUser(long id){
        return this.userRepository.findById(id);
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public void saveLogin(User user){
        this.userRepository.save(user);
    }

    public void saveLogout(User user){
        this.userRepository.save(user);
    }

    public void deleteUser(long id){
        User user = this.userRepository.findById(id);
        // TODO: check if this works like this
        user.setStatus(UserStatus.OFFLINE);
        this.userRepository.delete(user);
    }


    public User createUser(User newUser) { // TODO: must usernameAlreadyExists be checked here or in frontend?
        /**if (userRepository.existsUserByUsername(newUser.getUsername())){
            throw new ArithmeticException("Username already exists");
        } else {**/
            newUser.setToken(UUID.randomUUID().toString());
            newUser.setStatus(UserStatus.OFFLINE); //default is OFFLINE. If user loggs in, its changed to ONLINE
            newUser.setCreationDate();
            userRepository.save(newUser);
            log.debug("Created Information for User: {}", newUser);
            return newUser;
    }

    public User updateUser(long id, User updatedUser){
        //TODO: simplify using built in .update function
        User oldUser = getUser(id);
        String username = updatedUser.getUsername();

        if(!username.equals(oldUser.getUsername())  && updatedUser.getUsername() != null) {
            oldUser.setUsername(username);
        }

        String birthday = updatedUser.getBirthday();
        if (!birthday.equals(oldUser.getBirthday()) && updatedUser.getBirthday() != null) {
            oldUser.setBirthday(birthday);
        }

        this.userRepository.save(oldUser); //save the changes
        return oldUser;
    }
}




