package com.jan;



import com.jan.encryptor.AesEncryptor;
import com.jan.encryptor.RsaEncryptor;
import com.jan.manager.KeyManager;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

/**
 * 系统入口类
 * 提供控制台交互菜单，整合所有模块功能
 */
public class Main {
    // 初始化模块实例
    private static KeyManager keyManager = new KeyManager();
    private static AesEncryptor aesEncryptor = new AesEncryptor();
    private static RsaEncryptor rsaEncryptor = new RsaEncryptor();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==================== 加密交换系统 ====================");
        showMainMenu();
    }

    /**
     * 显示主菜单
     */
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n请选择功能：");
            System.out.println("1. 密钥管理");
            System.out.println("2. 文本加密解密");
            System.out.println("3. 文件加密解密");
            System.out.println("4. 退出系统");
            System.out.print("输入选择（1-4）：");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showKeyMenu();
                    break;
                case "2":
                    showTextMenu();
                    break;
                case "3":
                    showFileMenu();
                    break;
                case "4":
                    System.out.println("感谢使用，系统退出！");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("无效选择，请输入1-4之间的数字！");
            }
        }
    }

    /**
     * 显示密钥管理子菜单
     */
    private static void showKeyMenu() {
        while (true) {
            System.out.println("\n=== 密钥管理菜单 ===");
            System.out.println("1. 生成并保存所有密钥");
            System.out.println("2. 加载AES密钥");
            System.out.println("3. 加载RSA密钥对");
            System.out.println("4. 返回主菜单");
            System.out.print("输入选择（1-4）：");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        try {
                            // 生成AES密钥
                            SecretKey aesKey = keyManager.generateAesKey();
                            // 生成RSA密钥对
                            KeyPair rsaKeyPair = keyManager.generateRsaKeyPair();
                            PublicKey publicKey = rsaKeyPair.getPublic();
                            PrivateKey privateKey = rsaKeyPair.getPrivate();
                            // 保存密钥
                            keyManager.saveAesKey(aesKey);
                            keyManager.saveRsaPublicKey(publicKey);
                            keyManager.saveRsaPrivateKey(privateKey);
                            System.out.println("所有密钥生成并保存成功！");
                        } catch (Exception e) {
                            // 优化：打印完整异常信息（包括异常类型和堆栈），便于定位问题
                            System.out.println("操作失败：" + e.getClass().getSimpleName()); // 打印异常类型
                            System.out.println("异常详情：" + (e.getMessage() == null ? "无明确消息，查看堆栈：" : e.getMessage()));
                            e.printStackTrace(); // 打印完整堆栈，关键！
                        }
                        break;
                    case "2":
                        SecretKey loadedAesKey = keyManager.loadAesKey();
                        System.out.println("AES密钥加载成功！");
                        break;
                    case "3":
                        PublicKey loadedPublicKey = keyManager.loadRsaPublicKey();
                        PrivateKey loadedPrivateKey = keyManager.loadRsaPrivateKey();
                        System.out.println("RSA密钥对加载成功！");
                        break;
                    case "4":
                        return;
                    default:
                        System.out.println("无效选择，请输入1-4之间的数字！");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    /**
     * 显示文本加密解密子菜单
     */
    private static void showTextMenu() {
        while (true) {
            System.out.println("\n=== 文本加密解密菜单 ===");
            System.out.println("1. AES文本加密");
            System.out.println("2. AES文本解密");
            System.out.println("3. RSA加密AES密钥");
            System.out.println("4. RSA解密AES密钥");
            System.out.println("5. 返回主菜单");
            System.out.print("输入选择（1-5）：");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        System.out.print("请输入待加密文本：");
                        String plainText = scanner.nextLine();
                        SecretKey aesKey = keyManager.loadAesKey();
                        String cipherText = aesEncryptor.encryptText(plainText, aesKey);
                        System.out.println("AES加密成功，密文：" + cipherText);
                        break;
                    case "2":
                        System.out.print("请输入待解密密文（Base64格式）：");
                        String cipherText2 = scanner.nextLine();
                        SecretKey aesKey2 = keyManager.loadAesKey();
                        String plainText2 = aesEncryptor.decryptText(cipherText2, aesKey2);
                        System.out.println("AES解密成功，明文：" + plainText2);
                        break;
                    case "3":
                        SecretKey aesKey3 = keyManager.loadAesKey();
                        PublicKey publicKey3 = keyManager.loadRsaPublicKey();
                        String encryptedAesKey = rsaEncryptor.encryptAesKey(aesKey3.getEncoded(), publicKey3);
                        System.out.println("RSA加密AES密钥成功，加密后密钥：" + encryptedAesKey);
                        break;
                    case "4":
                        System.out.print("请输入加密后的AES密钥（Base64格式）：");
                        String encryptedAesKey2 = scanner.nextLine();
                        PrivateKey privateKey4 = keyManager.loadRsaPrivateKey();
                        byte[] decryptedAesKeyBytes = rsaEncryptor.decryptAesKey(encryptedAesKey2, privateKey4);
                        SecretKey decryptedAesKey = new javax.crypto.spec.SecretKeySpec(decryptedAesKeyBytes, "AES");
                        System.out.println("RSA解密AES密钥成功！");
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println("无效选择，请输入1-5之间的数字！");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    /**
     * 显示文件加密解密子菜单
     */
    private static void showFileMenu() {
        while (true) {
            System.out.println("\n=== 文件加密解密菜单 ===");
            System.out.println("1. AES文件加密");
            System.out.println("2. AES文件解密");
            System.out.println("3. 返回主菜单");
            System.out.print("输入选择（1-3）：");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        System.out.print("请输入明文文件路径（.txt）：");
                        String plainFilePath = scanner.nextLine();
                        SecretKey aesKey = keyManager.loadAesKey();
                        aesEncryptor.encryptFile(plainFilePath, aesKey);
                        break;
                    case "2":
                        System.out.print("请输入密文文件路径（.encrypt）：");
                        String cipherFilePath = scanner.nextLine();
                        System.out.print("请输入解密后明文文件保存路径（.txt）：");
                        String plainFilePath2 = scanner.nextLine();
                        SecretKey aesKey2 = keyManager.loadAesKey();
                        aesEncryptor.decryptFile(cipherFilePath, aesKey2, plainFilePath2);
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println("无效选择，请输入1-3之间的数字！");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }
}