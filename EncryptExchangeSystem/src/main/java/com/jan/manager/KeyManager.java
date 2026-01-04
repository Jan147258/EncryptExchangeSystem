package com.jan.manager;

import com.jan.constant.EncryptConstant;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 密钥管理模块
 * 负责AES密钥与RSA密钥对的生成、保存与加载
 */
public class KeyManager {
    /**
     * 生成AES对称密钥
     * @return SecretKey AES密钥对象
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public SecretKey generateAesKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(EncryptConstant.AES_ALGORITHM);
        keyGen.init(EncryptConstant.AES_KEY_SIZE, new SecureRandom()); // 初始化密钥长度与随机数
        return keyGen.generateKey();
    }

    /**
     * 生成RSA非对称密钥对
     * @return KeyPair RSA公钥/私钥对
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(EncryptConstant.RSA_ALGORITHM);
        keyPairGen.initialize(EncryptConstant.RSA_KEY_SIZE, new SecureRandom()); // 初始化密钥长度与随机数
        return keyPairGen.generateKeyPair();
    }

    /**
     * 私有方法：将密钥字节数组保存到本地文件
     * @param keyBytes 密钥字节数组
     * @param filePath 保存路径
     * @throws IOException 文件读写异常
     */
    private void saveKey(byte[] keyBytes, String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IOException("密钥保存路径为空，无法保存密钥");
        }
        if (EncryptConstant.KEY_SAVE_PATH == null || EncryptConstant.KEY_SAVE_PATH.trim().isEmpty()) {
            throw new IOException("密钥保存根目录为空，请先配置有效的KEY_SAVE_PATH");
        }

        File file = new File(filePath);
        // 自动创建密钥保存目录（若不存在）
        File keyDir = new File(EncryptConstant.KEY_SAVE_PATH);
        if (!keyDir.exists()) {
            boolean isDirCreated = keyDir.mkdirs(); // 递归创建多级目录
            if (isDirCreated) {
                System.out.println("密钥保存目录不存在，已自动创建：" + keyDir.getAbsolutePath());
            } else {
                throw new IOException("无法创建密钥保存目录：" + keyDir.getAbsolutePath());
            }
        }

        // 写入文件
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(keyBytes);
        fos.close();
        System.out.println("密钥已保存到：" + file.getAbsolutePath());
    }

    /**
     * 保存AES密钥到本地
     * @param aesKey AES密钥对象
     * @throws IOException 文件读写异常
     */
    public void saveAesKey(SecretKey aesKey) throws IOException {
        saveKey(aesKey.getEncoded(), EncryptConstant.AES_KEY_PATH);
    }

    /**
     * 保存RSA公钥到本地
     * @param publicKey RSA公钥对象
     * @throws IOException 文件读写异常
     */
    public void saveRsaPublicKey(PublicKey publicKey) throws IOException {
        saveKey(publicKey.getEncoded(), EncryptConstant.RSA_PUBLIC_KEY_PATH);
    }

    /**
     * 保存RSA私钥到本地
     * @param privateKey RSA私钥对象
     * @throws IOException 文件读写异常
     */
    public void saveRsaPrivateKey(PrivateKey privateKey) throws IOException {
        saveKey(privateKey.getEncoded(), EncryptConstant.RSA_PRIVATE_KEY_PATH);
    }

    /**
     * 私有方法：从本地文件加载密钥字节数组
     * @param filePath 密钥文件路径
     * @return 密钥字节数组
     * @throws IOException 文件读写异常
     */
    private byte[] loadKeyBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("密钥文件不存在：" + filePath);
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] keyBytes = new byte[(int) file.length()];
        fis.read(keyBytes);
        fis.close();
        return keyBytes;
    }

    /**
     * 从本地加载AES密钥
     * @return SecretKey AES密钥对象
     * @throws IOException 文件读写异常
     */
    public SecretKey loadAesKey() throws IOException {
        byte[] keyBytes = loadKeyBytes(EncryptConstant.AES_KEY_PATH);
        return new javax.crypto.spec.SecretKeySpec(keyBytes, EncryptConstant.AES_ALGORITHM);
    }

    /**
     * 从本地加载RSA公钥
     * @return PublicKey RSA公钥对象
     * @throws Exception 加载异常
     */
    public PublicKey loadRsaPublicKey() throws Exception {
        byte[] keyBytes = loadKeyBytes(EncryptConstant.RSA_PUBLIC_KEY_PATH);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(EncryptConstant.RSA_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 从本地加载RSA私钥
     * @return PrivateKey RSA私钥对象
     * @throws Exception 加载异常
     */
    public PrivateKey loadRsaPrivateKey() throws Exception {
        byte[] keyBytes = loadKeyBytes(EncryptConstant.RSA_PRIVATE_KEY_PATH);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(EncryptConstant.RSA_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }
}