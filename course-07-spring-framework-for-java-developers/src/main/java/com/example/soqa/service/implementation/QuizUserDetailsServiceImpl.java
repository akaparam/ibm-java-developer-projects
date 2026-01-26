package com.example.soqa.service;

import com.example.soqa.model.User;

import java.util.List;
import java.util.ArrayList;
import com.example.soqa.service.QuizUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class QuizUserDetailsServiceImpl implements QuizUserDetailsService, UserDetailsService {

    private static List<User> users = new ArrayList<>();

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public void register(User user) {
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        user.setRole("ROLE_" + user.getRole());
        users.add(user);
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
