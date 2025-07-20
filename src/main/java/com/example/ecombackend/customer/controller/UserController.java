package com.example.ecombackend.customer.controller;


import com.example.ecombackend.customer.JwtUserService;
import com.example.ecombackend.customer.models.Users;
import com.example.ecombackend.customer.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"https://dpgrabzy.netlify.app","https://dpgrabzyhub.netlify.app"}, allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final JwtUserService jwtUserService;

    public UserController(UserService userService, JwtUserService jwtUserService){
        this.userService = userService;
        this.jwtUserService = jwtUserService;
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody Users user){
        try{
            Users savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users credentials){
        try {
            Users user = userService.login(credentials);
            String token = jwtUserService.generateToken(user.getEmail(), user.getId());

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(true) // Set to true in production
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("None")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("Logged in successfully...");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true) // true in production
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }


    @GetMapping("/check-user-auth")
    public ResponseEntity<?> checkAuth(@CookieValue(name = "jwt", required = false) String token) {
        if (token == null || !jwtUserService.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String useremail = jwtUserService.extractUserEmail(token);
        Long userId = jwtUserService.extractUserId(token);

        return ResponseEntity.ok().body(Map.of(
                "useremail", useremail,
                "userId", userId
        ));
    }






    // ✅ Get all users
    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ Get user by ID
    @GetMapping("/{id}")
    public Users getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // ✅ Update user by ID
    @PutMapping("/{id}")
    public Users updateUser(@PathVariable Long id, @RequestBody Users updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    // ✅ Delete user by ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
