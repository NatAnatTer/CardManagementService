package ru.project.CardManagementService.security;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5CryptoServiceImpl implements CryptoService {
    @Override
    public String encrypt(String original) throws NoSuchAlgorithmException {

//        MessageDigest md5 = MessageDigest.getInstance("MD5");
//        md5.update(StandardCharsets.UTF_8.encode(original));
//        return String.format("%032x", new BigInteger(1, md5.digest()));
        return original;
    }

    @Override
    public String decrypt(String encrypted) {
        return encrypted;
    }
}
