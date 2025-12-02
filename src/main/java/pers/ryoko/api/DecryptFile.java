package pers.ryoko.api;

import lombok.extern.slf4j.Slf4j;
import pers.ryoko.utils.IVUtil;
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
/**
 * @author 网云2304 542307280411 李润东
 * 解密文件API
 */
@Slf4j
public class DecryptFile {

    public static void decryptFile(String encryptedPath, String targetDir, String keyPath) {
        String targetPath = targetDir + "/" + autoGuessDecryptedName(encryptedPath);
        try (InputStream is = new FileInputStream(encryptedPath);
             OutputStream os = new FileOutputStream(targetPath)) {

            SecretKey key = loadKey(keyPath);

            IvParameterSpec iv = IVUtil.readIvFromStream(is);

            Cipher cipher = initCipher(key, iv);

            decryptStream(is, os, cipher);
            log.info("{}解密完成，文件已保存为: {}所使用的密钥文件:{}", encryptedPath, targetPath, keyPath);
        } catch (Exception e) {
            log.error("解密文件失败: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    // 读取密钥
    private static SecretKey loadKey(String keyPath) {
        try {
            String keyString = Files.readString(Paths.get(keyPath));
            return SecretKeyUtil.stringToKey(keyString);
        } catch (Exception e) {
            throw new RuntimeException("密钥加载失败: " + e.getMessage(), e);
        }
    }



    // 初始化 Cipher（解密模式）
    private static Cipher initCipher(SecretKey key, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance(SecretKeyUtil.ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException("解密 Cipher 初始化失败", e);
        }
    }


    // 解密剩余字节流
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

    private static String autoGuessDecryptedName(String encryptedPath) {
        // 假设加密文件名形如 xxx.xxx.enc
        String encryptedName = Paths.get(encryptedPath).getFileName().toString();
        if (encryptedName.endsWith(".enc")) {
            return encryptedName.substring(0, encryptedName.length() - 4);
        }
        // 否则默认追加 "_dec"
        return encryptedName + "_dec";
    }
}
