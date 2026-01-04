package com.jan;


import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.jan.encryptor.RsaEncryptor;
import com.jan.manager.KeyManager;
import org.junit.Test;
import static org.junit.Assert.*;

public class RsaEncryptorTest {
    private KeyManager keyManager = new KeyManager();
    private RsaEncryptor rsaEncryptor = new RsaEncryptor();

    @Test
    public void testRsaEncryptAndDecryptAesKey() throws Exception {
        // 生成AES密钥与RSA密钥对
        SecretKey aesKey = keyManager.generateAesKey();
        KeyPair rsaKeyPair = keyManager.generateRsaKeyPair();
        PublicKey publicKey = rsaKeyPair.getPublic();
        PrivateKey privateKey = rsaKeyPair.getPrivate();

        // RSA加密AES密钥
        String encryptedAesKey = rsaEncryptor.encryptAesKey(aesKey.getEncoded(), publicKey);
        System.out.println("rsa加密前："+aesKey+ " ras加密后："+encryptedAesKey);


        // RSA解密AES密钥
        byte[] decryptedAesKeyBytes = rsaEncryptor.decryptAesKey(encryptedAesKey, privateKey);
        SecretKey decryptedAesKey = new javax.crypto.spec.SecretKeySpec(decryptedAesKeyBytes, "AES");
        System.out.println("rsa解密前："+encryptedAesKey+ " ras解密后："+decryptedAesKey);

        // 断言：解密后AES密钥与原密钥一致
        assertArrayEquals(aesKey.getEncoded(), decryptedAesKey.getEncoded());
        System.out.println("RSA加解密AES密钥单元测试通过！");
    }
}