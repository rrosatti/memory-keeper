package com.example.rrosatti.memorykeeper.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by root on 25/10/17.
 */

public class EncryptPassword {
    private String encryptedPass;


    public String getEncryptedPass(String password) throws  Exception{
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            BigInteger hash = new BigInteger(1, md.digest(password.getBytes()));
            this.encryptedPass = hash.toString();
        }catch (NoSuchAlgorithmException ex){
            throw new Exception("Error encrypting password");
        }
        return this.encryptedPass;

    }

}
