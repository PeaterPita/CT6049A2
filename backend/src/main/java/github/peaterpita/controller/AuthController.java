
package github.peaterpita.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import github.peaterpita.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ###########################################################
    // # Creating API endpoint `/api/auth/login`
    // # Login function takes two inputs, both present within
    // # the payload
    // # username and password
    // #
    // # Use authService attemptLogin function. If login
    // # successfull return success message with token
    // # else return 401 error
    // ###########################################################
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> payload) {
        String username = payload.getOrDefault("username", "");
        String password = payload.getOrDefault("password", "");
        String token = authService.attemptLogin(username, password);

        ResponseEntity<?> res;
        if (token != null) {
            res = ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", token));
        } else {
            res = ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Invalid username or password"));
        }
        return res;
    }
}
