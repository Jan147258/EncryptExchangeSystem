package com.jan;


import javax.crypto.SecretKey;

import com.jan.encryptor.AesEncryptor;
import com.jan.manager.KeyManager;
import org.junit.Test;
import static org.junit.Assert.*;

public class AesEncryptorTest {
    private KeyManager keyManager = new KeyManager();
    private AesEncryptor aesEncryptor = new AesEncryptor();

    @Test
    public void testAesTextEncryptAndDecrypt() throws Exception {
        // 生成AES密钥
        SecretKey aesKey = keyManager.generateAesKey();
        // 测试明文
        String plainText = "20233001373lxj";
        // 加密
        String cipherText = aesEncryptor.encryptText(plainText, aesKey);
        System.out.println("明文："+ plainText +",加密后：" +  cipherText);
        // 解密
        String decryptedText = aesEncryptor.decryptText(cipherText, aesKey);
        System.out.println("加密后："+ cipherText +", 解密后：" +  decryptedText);
        // 断言：解密后与明文一致
        assertEquals(plainText, decryptedText);
        System.out.println("AES文本加解密单元测试通过！");
    }
}