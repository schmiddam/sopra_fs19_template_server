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

import java.text.SimpleDateFormat;
import java.util.Date;
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

    public void deleteUser(long id){
        User user = this.userRepository.findById(id);
        this.userRepository.delete(user);
    }

    public String giveDate(){
        Date dateToday = new Date();
        SimpleDateFormat formatDateJava = new SimpleDateFormat("dd/MM/yyyy");
        String date_to_string = formatDateJava.format(dateToday);
		return date_to_string;
    }

    public User createUser(User newUser) {
        /**if (userRepository.existsUserByUsername(newUser.getUsername())){
            throw new ArithmeticException("Username already exists");
        } else {**/
            newUser.setToken(UUID.randomUUID().toString());
            newUser.setStatus(UserStatus.ONLINE);
            newUser.setDate(giveDate());
            userRepository.save(newUser);
            log.debug("Created Information for User: {}", newUser);
            return newUser;
        }
    }

