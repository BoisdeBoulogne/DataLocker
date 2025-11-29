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
    public static final String ALGORITHM = "AES";
    // 生成密钥
    public static SecretKey getSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    //将密钥转换为字符串形式，用于存储
    public static String keyToString(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    //从字符串恢复密钥

    public static SecretKey stringToKey(String keyString) {
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }


}
