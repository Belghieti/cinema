package com.demo.backend.controller;

import com.demo.backend.model.Box;
import com.demo.backend.model.User;
import com.demo.backend.repository.BoxRepository;
import com.demo.backend.repository.UserRepository;
import com.demo.backend.service.BoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/boxes")
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;
    private final BoxRepository boxRepository;
    private final UserRepository userRepository;



    @PostMapping("/create")
    public Box createBox(@RequestParam Long movieId,
                         @RequestParam String name,
                         Authentication authentication) {

        String username = authentication.getName(); // récupéré depuis le token JWT
        return boxService.createBoxFromUsername(username, movieId, name);
    }

    @GetMapping("/{boxId}")
    public ResponseEntity<?> getBoxDetails(@PathVariable Long boxId, Authentication authentication) {
        String username = authentication.getName();

        // Utilisation propre de Optional
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("Utilisateur non trouvé");
        }

        User user = optionalUser.get();
        Long userId = user.getId();

        Optional<Box> optionalBox = boxRepository.findById(boxId);
        if (optionalBox.isEmpty()) {
            return ResponseEntity.status(404).body("Box non trouvée");
        }

        Box box = optionalBox.get();


        return ResponseEntity.ok(box);
    }
    @GetMapping("/{boxId}/join")
    public ResponseEntity<Map<String, Object>> joinBox(@RequestParam Long userId, @PathVariable Long boxId) {
        // Récupérer la Box avec l'ID
        Box box = boxRepository.findById(boxId).orElseThrow();

        // Vérifier si l'utilisateur est déjà un participant
        boolean isParticipant = box.getParticipants().stream()
                .anyMatch(u -> u.getId().equals(userId));

        // Si l'utilisateur n'est pas un participant, retourner une erreur 403
        if (!isParticipant) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Access denied. You are not invited.");
            return ResponseEntity.status(403).body(response);
        }

        // Si l'utilisateur est un participant, retourner une réponse JSON avec le succès
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Access granted to box " + boxId);
        response.put("boxId", boxId);
        response.put("userId", userId);
        return ResponseEntity.ok(response);
    }


}