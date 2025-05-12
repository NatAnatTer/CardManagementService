package ru.project.CardManagementService.security;


public interface CryptoService {
    String encrypt(String original);

    String decrypt(String encrypted);
}
