package ch.uzh.ifi.seal.soprafs19.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "user doesnt exist yet")
public class UserDoesntExist extends RuntimeException {
}
