package com.expensetracker.service;

import com.expensetracker.entity.UserEntity;
import com.expensetracker.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import org.springframework.stereotype.Service;

/** Simple name + PIN login. Unknown names are registered on first login. PINs are stored hashed. */
@Service
public class AuthService {
    private final UserRepository users;
    public AuthService(UserRepository users) { this.users = users; }

    public UserEntity loginOrRegister(String name, String pin) {
        if (name == null || name.isBlank() || pin == null || !pin.matches("\\d{4,6}"))
            throw new IllegalArgumentException("Enter a name and a 4-6 digit PIN");
        String hash = hash(pin);
        return users.findByNameIgnoreCase(name.trim()).map(u -> {
            if (!u.getPinHash().equals(hash)) throw new IllegalArgumentException("Wrong PIN for this name");
            return u;
        }).orElseGet(() -> users.save(new UserEntity(name.trim(), hash)));
    }

    private String hash(String pin) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(pin.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) { throw new IllegalStateException(e); }
    }
}
