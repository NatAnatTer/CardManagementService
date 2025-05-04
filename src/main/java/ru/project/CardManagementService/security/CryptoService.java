package ru.project.CardManagementService.security;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface CryptoService {

    public String encrypt(String original) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    public String decrypt(String encrypted);
}
