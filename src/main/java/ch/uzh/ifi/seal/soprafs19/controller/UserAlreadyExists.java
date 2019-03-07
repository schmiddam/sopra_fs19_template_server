package ch.uzh.ifi.seal.soprafs19.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "username is already taken")
public class UserAlreadyExists extends RuntimeException {
}
