package com.example.ecombackend.deliveryagent.controller;

import com.example.ecombackend.deliveryagent.JwtAgentService;
import com.example.ecombackend.deliveryagent.models.DeliveryAgent;
import com.example.ecombackend.deliveryagent.service.DeliveryAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/delivery-agents")
@CrossOrigin(origins = {"http://localhost:3000","https://grabzy-e-com-frontend.vercel.app","https://grabzy-hub-e-com-frontend.vercel.app"}, allowCredentials = "true")
public class DeliveryAgentController {

    private final DeliveryAgentService deliveryAgentService;
    private final JwtAgentService jwtAgentService;

    public DeliveryAgentController(DeliveryAgentService deliveryAgentService, JwtAgentService jwtAgentService) {
        this.deliveryAgentService = deliveryAgentService;
        this.jwtAgentService = jwtAgentService;
    }


    @PostMapping("/agent-register")
    public ResponseEntity<?> registerAgent(@RequestBody DeliveryAgent agent) {
        try {
            DeliveryAgent savedAgent = deliveryAgentService.registerAgent(agent);
            return ResponseEntity.ok(savedAgent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/agent-login")
    public ResponseEntity<?> loginAgent(@RequestBody DeliveryAgent credentials) {
        try {
            DeliveryAgent agent = deliveryAgentService.loginAgent(credentials);
            String token = jwtAgentService.generateToken(agent.getGmail(), agent.getAgentId());

            ResponseCookie cookie = ResponseCookie.from("agentJwt", token)
                    .httpOnly(true)
                    .secure(true) // Set to true in production
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("None")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("Agent logged in successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }


    @PostMapping("/agent-logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("agentJwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }

    @GetMapping("/check-agent-auth")
    public ResponseEntity<?> checkAgentAuth(@CookieValue(name = "agentJwt", required = false) String token) {
        if (token == null || !jwtAgentService.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String userEmail = jwtAgentService.extractAgentEmail(token);
        Long agentId = jwtAgentService.extractAgentId(token);

        return ResponseEntity.ok(Map.of(
                "userEmail", userEmail,
                "agentId", agentId
        ));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryAgent>> getAllAgents() {
        return ResponseEntity.ok(deliveryAgentService.getAllAgents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryAgent> getAgentById(@PathVariable Long id) {
        Optional<DeliveryAgent> agent = deliveryAgentService.getAgentById(id);
        return agent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<DeliveryAgent> approveAgent(@PathVariable Long id) {
        DeliveryAgent approved = deliveryAgentService.approveAgent(id);
        if (approved != null) {
            return ResponseEntity.ok(approved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        deliveryAgentService.deleteAgent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/unapprove")
    public ResponseEntity<DeliveryAgent> unapproveAgent(@PathVariable Long id) {
        DeliveryAgent updated = deliveryAgentService.unapproveAgent(id);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
