package pers.ryoko.api;

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
 * 密钥相关
 */
public class KeyManage {

    public static final Path KEY_LIST = Paths.get(System.getProperty("user.home"), "key.txt");

    // 生成密钥文件，并将密钥写入 KEY_LIST 文件进行管理
    public static void createKeyFile(String targetPath) {
        try {
            String key = SecretKeyUtil.keyToString(SecretKeyUtil.getSecretKey());

            // 写入目标 key 文件
            Files.writeString(Paths.get(targetPath), key, StandardOpenOption.CREATE);

            // 写入 keyList
            writeKeyList(targetPath);

        } catch (Exception e) {
            throw new RuntimeException("生成密钥文件失败", e);
        }
    }

    // 线程安全写入 keyList
    private static synchronized void writeKeyList(String path) throws IOException {
        // 确保父目录存在
        Path parent = KEY_LIST.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(
                KEY_LIST,
                "path:" + path + "\n",
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
    // 获取各KEY文件地址
    public static synchronized Set<String> getKeyList() throws IOException {
        return Files.readAllLines(KEY_LIST).stream()
                .map(line -> line.replaceFirst("^path:", ""))
                .collect(Collectors.toSet());
    }

}

