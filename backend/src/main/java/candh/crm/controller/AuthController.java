package candh.crm.controller;

import candh.crm.model.User;
import candh.crm.service.AuthService;
import candh.crm.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class AuthController
{
    @Autowired
    private UserDataService userDataService;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/signup")
    public ResponseEntity<?> signup() {
        return ResponseEntity.ok("This is the signup page.");
    }

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok("This is the login page.");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody User user) {
        
        if (!authService.vaildEmail(user.getEmail())) {
            return ResponseEntity.ok("Email is not valid.");
        }

        if (!authService.vaildPassword(user.getPassword())) {
            return ResponseEntity.ok("Password is not valid.");
        }
        
        User _user = userDataService.findUserByEmail(user.getEmail());
        if (_user != null) {
            if (_user.isEnabled()) {
                return ResponseEntity.ok("Email is already taken.");
            } else {   // email taken but not confirmed
                userDataService.deleteUserByEmail(_user.getEmail());
            }
        }
        
        try {
            authService.signupUser(new User(user.getEmail(), user.getPassword(), user.getFirst_name(),
                    user.getLast_name()));
        } catch (Exception e) {
            return ResponseEntity.ok("Error during user signup.");
        }
        return ResponseEntity.ok("You just successfully submit a signup request.");
    }

    @GetMapping("/signup/{email}/{signupConfirmPath}")
    public ResponseEntity<?> confirmUser(@PathVariable() String email, @PathVariable() String signupConfirmPath) {
        User user = userDataService.findUserByEmail(email);
        if (user != null && !user.isEnabled()) {
            user.setEnabled(true);   // confirm
            userDataService.saveUser(user);
            return ResponseEntity.ok("Signup confirm success.");
        } else {
            return ResponseEntity.ok("Signup confirm invalid or deprecated.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.ok("Error during user authentication.");
        }
        return ResponseEntity.ok("You just successfully logged in.");
    }
}