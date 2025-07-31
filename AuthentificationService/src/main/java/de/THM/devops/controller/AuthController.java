package de.THM.devops.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Point de départ pour la connexion
    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/azure"); // Redirection vers Azure AD
    }

    // Redirection après l'authentification réussie
    @GetMapping("/redirect")
    public void redirectFront(HttpServletResponse response) throws IOException {
        System.out.println("Redirection réussie vers le frontend !");
        response.sendRedirect("http://localhost:3000/"); // Redirection vers le frontend
    }

    // Récupération des informations utilisateur
    @GetMapping("/home")
    public Map<String, String> home(@AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("username", principal.getFullName());
            userInfo.put("email", principal.getEmail());
            return userInfo;
        }
        throw new RuntimeException("Utilisateur non authentifié.");
    }
}

