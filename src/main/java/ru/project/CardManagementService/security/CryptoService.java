package ru.project.CardManagementService.security;


public interface CryptoService {

    String encrypt(String original);

    public String decrypt(String encrypted);
}
