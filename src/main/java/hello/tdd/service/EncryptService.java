package hello.tdd.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class EncryptService {
    public String getEncrypt(String password) {
        String encryptedPassword = "";
        try {
            // SHA-256 알고리즘 객체 생성
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // SHA-256 적용
            md.update(password.getBytes());
            byte[] digest = md.digest();
            // byte to string
            StringBuilder sb = new StringBuilder();
            for (byte b: digest) {
                sb.append(String.format("%02x", b));
            }
            encryptedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedPassword;
    }
}
