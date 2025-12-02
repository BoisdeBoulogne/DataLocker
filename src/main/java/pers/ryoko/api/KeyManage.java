package pers.ryoko.api;

import lombok.extern.slf4j.Slf4j;
import pers.ryoko.utils.SecretKeyUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 网云2304 542307280411 李润东
 * 密钥管理相关
 */
public class KeyManage {

    // 生成密钥文件
    public static void createKeyFile(String targetPath) {
        try {
            String key = SecretKeyUtil.keyToString(SecretKeyUtil.generateKey());

            // 写入目标 key 文件
            Files.writeString(Paths.get(targetPath), key, StandardOpenOption.CREATE);

        } catch (Exception e) {
            throw new RuntimeException("生成密钥文件失败", e);
        }
    }

}

