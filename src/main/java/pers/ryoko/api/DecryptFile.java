package pers.ryoko.api;

import pers.ryoko.utils.SecretKeyUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DecryptFile {

    public static void decryptFile(String encryptedPath, String targetPath, String keyPath) {
        try (InputStream is = new FileInputStream(encryptedPath);
             OutputStream os = new FileOutputStream(targetPath)) {

            SecretKey key = loadKey(keyPath);

            IvParameterSpec iv = readIv(is);

            Cipher cipher = initCipher(key, iv);

            decryptStream(is, os, cipher);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // 1. 读取密钥
    private static SecretKey loadKey(String keyPath) {
        try {
            String keyString = Files.readString(Paths.get(keyPath));
            return SecretKeyUtil.stringToKey(keyString);
        } catch (Exception e) {
            throw new RuntimeException("密钥加载失败: " + e.getMessage(), e);
        }
    }


    // 2. 从文件前 16 字节读取 IV
    private static IvParameterSpec readIv(InputStream is) {
        try {
            byte[] ivBytes = new byte[16];
            int read = is.read(ivBytes);
            if (read != 16) {
                throw new RuntimeException("文件格式错误：IV 长度不足 16 字节");
            }
            return new IvParameterSpec(ivBytes);
        } catch (Exception e) {
            throw new RuntimeException("读取 IV 失败", e);
        }
    }


    // 3. 初始化 Cipher（解密模式）
    private static Cipher initCipher(SecretKey key, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance(SecretKeyUtil.ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException("解密 Cipher 初始化失败", e);
        }
    }


    // 4. 解密剩余字节流
    private static void decryptStream(InputStream is, OutputStream os, Cipher cipher) {
        try {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                byte[] decrypted = cipher.update(buffer, 0, bytesRead);
                if (decrypted != null) {
                    os.write(decrypted);
                }
            }

            byte[] finalBytes = cipher.doFinal();
            if (finalBytes != null) {
                os.write(finalBytes);
            }

        } catch (Exception e) {
            throw new RuntimeException("解密流处理失败: " + e.getMessage(), e);
        }
    }
}
