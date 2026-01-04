package com.jan.encryptor;



import com.jan.constant.EncryptConstant;
import com.jan.util.EncryptUtil;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RSA加密解密模块
 * 负责用RSA公钥加密AES密钥、RSA私钥解密AES密钥
 */
public class RsaEncryptor {
    /**
     * 用RSA公钥加密AES密钥
     * @param aesKey AES密钥字节数组
     * @param publicKey RSA公钥
     * @return Base64编码的加密后AES密钥
     * @throws Exception 加密异常
     */
    public String encryptAesKey(byte[] aesKey, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(EncryptConstant.RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey); // 初始化加密模式
        byte[] encryptBytes = cipher.doFinal(aesKey); // 执行加密
        return EncryptUtil.byteToBase64(encryptBytes); // 转Base64便于传输
    }

    /**
     * 用RSA私钥解密AES密钥
     * @param encryptedAesKey Base64编码的加密后AES密钥
     * @param privateKey RSA私钥
     * @return 解密后的AES密钥字节数组
     * @throws Exception 解密异常
     */
    public byte[] decryptAesKey(String encryptedAesKey, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(EncryptConstant.RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey); // 初始化解密模式
        byte[] cipherBytes = EncryptUtil.base64ToByte(encryptedAesKey); // Base64解码
        return cipher.doFinal(cipherBytes); // 执行解密
    }
}