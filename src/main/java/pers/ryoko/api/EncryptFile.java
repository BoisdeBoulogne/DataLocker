package pers.ryoko.api;
import pers.ryoko.utils.IVUtil;
import pers.ryoko.utils.LoggingUtil;
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
 * 加密文件API
 */
public class EncryptFile {

    // 主方法
    public static void encryptFile(String filePath, String keyPath) {
        String targetPath = filePath + ".enc";
        try (InputStream is = new FileInputStream(filePath);
             OutputStream os = new FileOutputStream(targetPath)) {

            SecretKey key = loadKey(keyPath);

            IvParameterSpec iv = writeIv(os);

            Cipher cipher = initCipher(key, iv);

            encryptStream(is, os, cipher);
            String msg = String.format("加密成功，保存地址为: %s , 使用的密钥为: %s", targetPath, keyPath);
            LoggingUtil.info(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // 1. 加载密钥
    private static SecretKey loadKey(String keyPath) {
        try {
            String keyString = Files.readString(Paths.get(keyPath));
            return SecretKeyUtil.stringToKey(keyString);
        } catch (Exception e) {
            LoggingUtil.error("密钥加载失败: ",e);
            throw new RuntimeException("密钥加载失败: " + e.getMessage(), e);
        }
    }


    // 2. 生成 IV 并写入文件
    private static IvParameterSpec writeIv(OutputStream os) {
        try {
            IvParameterSpec iv = IVUtil.generateIv();
            os.write(iv.getIV());
            return iv;
        } catch (Exception e) {
            LoggingUtil.error("写入 IV 失败: ",e);
            throw new RuntimeException("写入 IV 失败", e);
        }
    }


    // 3. 初始化 Cipher
    private static Cipher initCipher(SecretKey key, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance(SecretKeyUtil.ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return cipher;
        } catch (Exception e) {
            LoggingUtil.error("加密 Cipher 初始化失败: ",e);
            throw new RuntimeException("加密 Cipher 初始化失败", e);
        }
    }


    // 4. 加密数据流
    private static void encryptStream(InputStream is, OutputStream os, Cipher cipher) {
        try {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                byte[] encrypted = cipher.update(buffer, 0, bytesRead);
                if (encrypted != null) {
                    os.write(encrypted);
                }
            }

            byte[] finalBytes = cipher.doFinal();
            if (finalBytes != null) {
                os.write(finalBytes);
            }

        } catch (Exception e) {
            LoggingUtil.error("加密流处理失败: ",e);
            throw new RuntimeException("加密流处理失败: " + e.getMessage(), e);
        }
    }


}
