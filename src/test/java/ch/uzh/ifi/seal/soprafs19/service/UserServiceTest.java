package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");
        testUser.setBirthday("testBirthday");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(createdUser.getToken());
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
    }

    @Test
    public void updateUser() {

        User testUser2 = new User();
        testUser2.setPassword("testPassword");
        testUser2.setUsername("testUsername2");
        testUser2.setBirthday("testBirthday");
        userService.createUser(testUser2);


        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setBirthday("updatedBirthday");

        //testUser.getId(): Id vom alten User im Backend
        // updatedUser: updatedUser, der vom FE ins BE gegeben wird
        testUser2 = userService.updateUser(testUser2.getId(), updatedUser);


        Assert.assertEquals(testUser2.getUsername(),updatedUser.getUsername());
        Assert.assertEquals(testUser2.getBirthday(), updatedUser.getBirthday());
    }

    @Test
    public void findUserByUsername() {
        Assert.assertNull(userRepository.findByUsername("testUsername3"));

        User testUser3 = new User();
        testUser3.setUsername("testUsername3");
        testUser3.setPassword("testPassword3");

        User createdUser = userService.createUser(testUser3);

        User returnedUser = userService.getUserByUsername(createdUser.getUsername());

        Assert.assertEquals(returnedUser.getUsername(), createdUser.getUsername());
        Assert.assertEquals(returnedUser.getId(), createdUser.getId());
    }

    @Test
    public void SaveLogin() {
        Assert.assertNull(userRepository.findByUsername("testUsername4"));

        User testUser4 = new User();
        testUser4.setUsername("testUsername4");
        testUser4.setPassword("testPassword4");

        User createdUser = userService.createUser(testUser4);
        createdUser.setStatus(UserStatus.ONLINE);

        userService.saveLogin(createdUser);

        Assert.assertEquals(userRepository.findByUsername("testUsername4").getStatus(),UserStatus.ONLINE);
    }

    @Test
    public void SaveLogout() {
        Assert.assertNull(userRepository.findByUsername("testUsername5"));

        User testUser5 = new User();
        testUser5.setUsername("testUsername5");
        testUser5.setPassword("testPassword5");
        User createdUser = userService.createUser(testUser5);
        createdUser.setStatus(UserStatus.ONLINE);
        userService.saveLogin(createdUser);
        createdUser.setStatus(UserStatus.OFFLINE);

        userService.saveLogout(createdUser);

        Assert.assertEquals(userRepository.findByUsername("testUsername5").getStatus(),UserStatus.OFFLINE);
    }

    @Test
    public void getUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername6"));

        User testUser6 = new User();
        testUser6.setUsername("testUsername6");
        testUser6.setPassword("testPassword6");

        User createdUser = userService.createUser(testUser6);

        User returnedUser = userService.getUser(createdUser.getId());

        Assert.assertEquals(returnedUser.getUsername(), createdUser.getUsername());
        Assert.assertEquals(returnedUser.getId(), createdUser.getId());
    }
}
