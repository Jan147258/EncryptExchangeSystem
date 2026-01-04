package com.jan.constant;

/**
 * 加密系统常量定义
 * 存储算法名称、密钥长度、文件路径等固定参数
 */
public class EncryptConstant {
    public static final String AES_ALGORITHM = "AES"; // 算法名称
    public static final String RSA_ALGORITHM = "RSA";
    public static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding"; // AES加密模式
    public static final int RSA_KEY_SIZE = 1024; // RSA密钥长度
    public static final int AES_KEY_SIZE = 128; // AES密钥长度

    // 指定有效且非空的密钥保存路径,比如桌面路径
    public static final String KEY_SAVE_PATH = "D:\\01IT\\Code\\EncryptExchangeSystem";

    // 密钥文件存储路径
    public static final String AES_KEY_PATH = "aes_key.key"; // AES密钥文件
    public static final String RSA_PUBLIC_KEY_PATH = "rsa_public.key"; // RSA公钥文件
    public static final String RSA_PRIVATE_KEY_PATH = "rsa_private.key"; // RSA私钥文件

    // 加密文件后缀
    public static final String ENCRYPT_FILE_SUFFIX = ".encrypt"; // 加密文件后缀
}