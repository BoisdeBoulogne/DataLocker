package pers.ryoko.utils;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author 网云2304 542307280411 李润东
 * 用于密钥相关的工具类(使用AES算法)
 */
public class SecretKeyUtil {

    // 加密算法（用于 Cipher）
    public static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    // AES 密钥长度
    private static final int AES_KEY_SIZE = 128;

    // 生成 AES 密钥
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(AES_KEY_SIZE);
        return keyGenerator.generateKey();
    }

    // 密钥转 Base64 字符串
    public static String keyToString(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // Base64 字符串转回 AES 密钥
    public static SecretKey stringToKey(String keyString) {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(keyBytes, "AES"); // ★必须是 AES
    }
}

