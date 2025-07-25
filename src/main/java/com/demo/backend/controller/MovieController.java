package com.demo.backend.controller;


import com.demo.backend.model.Movie;
import com.demo.backend.model.User;
import com.demo.backend.repository.UserRepository;
import com.demo.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://192.168.1.122:3000") // Remplacez par l'adresse de votre frontend
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final UserRepository userRepository;

    @PostMapping
    public Movie addMovie(@RequestBody Movie movie, Authentication authentication) {
        String username = authentication.getName(); // identifiant (souvent l'email ou le username)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        movie.setAddedBy(user);
        return movieService.addMovie(movie);
    }


    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }
}