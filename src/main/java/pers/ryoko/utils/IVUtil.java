package pers.ryoko.utils;

import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

/**
 * @author 网云2304 542307280411 李润东
 * 与IV(初始化向量)相关的工具类
 * 流程：加密时，生成IV，写入文件中，解密时，从文件中读取IV，用于解密
 */
public class IVUtil {

    // 生成 16字节 IV（AES block size = 16 bytes）
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * 从文件读取 IV
     * 要求文件内容至少 16 字节
     */
    public static IvParameterSpec readIvFromStream(InputStream is) {
        try {
            byte[] iv = new byte[16];
            int read = is.read(iv);

            if (read != 16) {
                throw new IOException("加密向量缺失");
            }

            return new IvParameterSpec(iv);
        } catch (IOException e) {
            throw new RuntimeException("读取 IV 失败", e);
        }
    }

}
