package ru.project.CardManagementService.security;

import org.springframework.stereotype.Service;

import java.util.Base64;
@Service
public class Md5CryptoServiceImpl implements CryptoService {

    @Override
    public String encrypt(String input) {
        try {
            return Base64.getEncoder().encodeToString(input.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String decrypt(String input) {
        try {
            return new String(Base64.getDecoder().decode(input));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
