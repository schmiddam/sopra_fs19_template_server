package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.apache.coyote.Response;
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
 * @see UserController
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)

public class UserControllerTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Test
    public void checkCredentialsOfExistingUser(){
        Assert.assertNull(userRepository.findByUsername("testUsername1"));

        User testUser1 = new User();
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("testPassword1");

        userController.createUser(testUser1);

        User testUser2 = new User();
        testUser2.setUsername("testUsername1"); //same username as testUser1
        testUser2.setPassword("testPassword1"); //same password as testUser1

        User returnUser = userController.checkCredentials(testUser2);

        Assert.assertEquals(returnUser.getUsername(), testUser1.getUsername());
        Assert.assertEquals(returnUser.getUsername(), testUser2.getUsername());
        Assert.assertEquals(returnUser.getPassword(), testUser1.getPassword());
        Assert.assertEquals(returnUser.getPassword(), testUser2.getPassword());
    }

    @Test (expected = UserDoesntExist.class) //
    public void checkCredentialsOfNotExistingUser(){
        Assert.assertNull(userRepository.findByUsername("testUsername3"));

        User testUser1 = new User();
        testUser1.setUsername("testUsername3");
        testUser1.setPassword("testPassword3");

        userController.createUser(testUser1);

        User testUser2 = new User();

        User returnUser = userController.checkCredentials(testUser2);
        Assert.assertEquals(returnUser, null);
    }

    @Test
    public void getIdOfExistingUser(){
        Assert.assertNull(userRepository.findByUsername("testUsername4"));

        User testUser1 = new User();
        testUser1.setUsername("testUsername4");
        testUser1.setPassword("testPassword4");

        userController.createUser(testUser1);

        long id = userRepository.findByUsername("testUsername4").getId();
        String idString = ""+id;

        User returnUser = userController.getId(id); // Routine under test

        Assert.assertEquals(returnUser.getUsername(), "testUsername4");
        Assert.assertEquals(returnUser.getId().toString(), idString);
    }

    @Test (expected = UserDoesntExist.class)
    public void getIdOfNotExistingUser(){
        Assert.assertNull(userRepository.findByUsername("testUsername5"));

        User testUser1 = new User();
        testUser1.setUsername("testUsername5");
        testUser1.setPassword("testPassword5");

        userController.createUser(testUser1);

        long id = userRepository.findByUsername("testUsername5").getId()+10000;

        User returnUser = userController.getId(id);

        Assert.assertNull(userRepository.findById(id));
        Assert.assertNull(returnUser);
    }


    @Test
    public void LogOutExistingUser(){
        Assert.assertNull(userRepository.findByUsername("testUsername6"));

        User testUser1 = new User();
        testUser1.setUsername("testUsername6");
        testUser1.setPassword("testPassword6");

        userController.createUser(testUser1);

        long id = userRepository.findByUsername("testUsername6").getId();
        String idString = ""+id;

        User returnUser = userController.setIdForLogout(id); //Routine under Test

        Assert.assertEquals(returnUser.getId().toString(), idString);
        Assert.assertEquals(returnUser.getUsername(), "testUsername6");
        Assert.assertEquals(userRepository.findByUsername("testUsername6").getStatus(), UserStatus.OFFLINE);

    }

    @Test (expected = UserDoesntExist.class)
    public void LogOutNotExistingUser(){
        Assert.assertNull(userRepository.findById(0));

        User returnUser = userController.setIdForLogout(0);

        Assert.assertNull(userRepository.findById(0));
        Assert.assertNull(returnUser);
    }

    @Test (expected = UserAlreadyExists.class)
    public void createAlreadyExistingUser(){
        Assert.assertNull(userRepository.findByUsername("testUsername7"));
        Assert.assertNull(userRepository.findByUsername("testUsername8"));

        User testUser1 = new User();

        testUser1.setUsername("testUsername7");
        testUser1.setPassword("testPassword7");
        userController.createUser(testUser1);

        User testUser2 = new User();

        testUser2.setUsername("testUsername7"); // same username as testUser1
        testUser2.setPassword("testPassword8");

        String returnString1 = userController.createUser(testUser2);

        Assert.assertNull(returnString1);
        Assert.assertEquals(userRepository.findByUsername("testUsername7").getPassword(), "testPassword7");
    }

    @Test
    public void createNotExistingUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername9"));

        User testUser1 = new User();

        testUser1.setUsername("testUsername9");
        testUser1.setPassword("testPassword9");

        String returnString1 = userController.createUser(testUser1);
        Assert.assertEquals("/users/"+userRepository.findByUsername("testUsername9").getId().toString(), returnString1);
    }

    @Test
    public void updateExistingUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername10"));
        Assert.assertNull(userRepository.findByUsername("testUsername11"));

        User testUser1 = new User();

        testUser1.setUsername("testUsername10");
        testUser1.setPassword("testPassword10");
        testUser1.setBirthday("testBirthday10");
        userController.createUser(testUser1);
        long id = userRepository.findByUsername("testUsername10").getId();
        User testUser2 = new User();

        testUser2.setUsername("testUsername11"); // same username as testUser1
        testUser2.setPassword("testPassword11");
        testUser2.setBirthday("testBirthday11");

        User returnUser = userController.setId(id, testUser2);

        Assert.assertEquals(returnUser.getUsername(), "testUsername11");
        Assert.assertEquals(returnUser.getPassword(), "testPassword10");
        Assert.assertEquals(returnUser.getBirthday(), "testBirthday11");
    }

    @Test(expected = UserDoesntExist.class)
    public void updateNotExistingUser() {
        Assert.assertNull(userRepository.findById(0));

        User testUser1 = new User();

        testUser1.setUsername("testUsername1");
        testUser1.setPassword("testPassword1");
        userController.setId(0, testUser1);

        Assert.assertNull(userRepository.findById(0));
    }
}
