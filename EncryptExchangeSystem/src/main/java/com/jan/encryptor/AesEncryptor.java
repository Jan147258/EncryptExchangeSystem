package com.jan.encryptor;



import com.jan.constant.EncryptConstant;
import com.jan.util.EncryptUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * AES加密解密模块
 * 负责文本与小文件的AES加密和解密实现
 */
public class AesEncryptor {
    /**
     * AES文本加密
     * @param plainText 明文文本
     * @param aesKey AES密钥
     * @return  Base64编码密文
     * @throws Exception 加密异常
     */
    public String encryptText(String plainText, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance(EncryptConstant.AES_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey); // 初始化加密模式
        byte[] encryptBytes = cipher.doFinal(plainText.getBytes("UTF-8")); // 执行加密
        return EncryptUtil.byteToBase64(encryptBytes); // 转Base64便于展示
    }

    /**
     * AES文本解密
     * @param cipherText Base64编码密文
     * @param aesKey AES密钥
     * @return 明文文本
     * @throws Exception 解密异常
     */
    public String decryptText(String cipherText, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance(EncryptConstant.AES_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, aesKey); // 初始化解密模式
        byte[] cipherBytes = EncryptUtil.base64ToByte(cipherText); // Base64解码
        byte[] plainBytes = cipher.doFinal(cipherBytes); // 执行解密
        return new String(plainBytes, "UTF-8"); // 转字符串
    }

    /**
     * AES文件加密
     * @param plainFilePath 明文txt文件路径
     * @param aesKey AES密钥
     * @throws Exception 加密异常
     */
    public void encryptFile(String plainFilePath, SecretKey aesKey) throws Exception {
        // 构建加密文件路径
        String cipherFilePath = plainFilePath + EncryptConstant.ENCRYPT_FILE_SUFFIX;
        File plainFile = new File(plainFilePath);

        // 校验文件有效性
        if (!plainFile.exists()) {
            throw new IOException("明文文件不存在：" + plainFilePath);
        }
        if (!plainFilePath.endsWith(".txt")) {
            throw new IOException("仅支持.txt格式文件加密");
        }
        if (plainFile.length() > 1024 * 1024) {
            throw new IOException("仅支持1MB以内文件加密");
        }

        // 初始化加密器
        Cipher cipher = Cipher.getInstance(EncryptConstant.AES_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);

        // 文件读写与加密
        FileInputStream fis = new FileInputStream(plainFile);
        FileOutputStream fos = new FileOutputStream(cipherFilePath);
        byte[] buffer = new byte[1024]; // 缓冲区
        int len;
        while ((len = fis.read(buffer)) != -1) {
            byte[] encryptBytes = cipher.update(buffer, 0, len);
            if (encryptBytes != null) {
                fos.write(encryptBytes);
            }
        }
        // 完成最后一段数据加密
        byte[] finalBytes = cipher.doFinal();
        if (finalBytes != null) {
            fos.write(finalBytes);
        }

        // 关闭流
        fis.close();
        fos.close();
        System.out.println("文件加密完成，加密文件路径：" + new File(cipherFilePath).getAbsolutePath());
    }

    /**
     * AES文件解密
     * @param cipherFilePath 加密文件路径（.encrypt）
     * @param aesKey AES密钥
     * @param plainFilePath 解密后明文文件路径
     * @throws Exception 解密异常
     */
    public void decryptFile(String cipherFilePath, SecretKey aesKey, String plainFilePath) throws Exception {
        File cipherFile = new File(cipherFilePath);

        // 校验文件有效性
        if (!cipherFile.exists()) {
            throw new IOException("密文文件不存在：" + cipherFilePath);
        }
        if (!cipherFilePath.endsWith(EncryptConstant.ENCRYPT_FILE_SUFFIX)) {
            throw new IOException("仅支持.encrypt格式文件解密");
        }

        // 初始化解密器
        Cipher cipher = Cipher.getInstance(EncryptConstant.AES_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, aesKey);

        // 文件读写与解密
        FileInputStream fis = new FileInputStream(cipherFile);
        FileOutputStream fos = new FileOutputStream(plainFilePath);
        byte[] buffer = new byte[1024]; // 缓冲区
        int len;
        while ((len = fis.read(buffer)) != -1) {
            byte[] decryptBytes = cipher.update(buffer, 0, len);
            if (decryptBytes != null) {
                fos.write(decryptBytes);
            }
        }
        // 完成最后一段数据解密
        byte[] finalBytes = cipher.doFinal();
        if (finalBytes != null) {
            fos.write(finalBytes);
        }

        // 关闭流
        fis.close();
        fos.close();
        System.out.println("文件解密完成，明文文件路径：" + new File(plainFilePath).getAbsolutePath());
    }
}