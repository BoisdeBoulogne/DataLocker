package pers.ryoko.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import pers.ryoko.api.EncryptFile;
import pers.ryoko.api.DecryptFile;
import pers.ryoko.api.KeyManage;

import java.io.File;
import java.util.List;
import java.util.Set;

public class MainUI {

    private VBox root;

    public MainUI() {


        root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("DateLocker 文件加密工具");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        List<Button> buttons = List.of(
                new Button("查看使用说明"),
                new Button("选择文件并加密"),
                new Button("选择文件并解密"),
                new Button("生成密钥文件"),
                new Button("查看密钥列表")
        );

        int btnCount = buttons.size();

        for (Button btn : buttons) {
            btn.setMaxWidth(Double.MAX_VALUE);

            btn.prefHeightProperty().bind(
                    root.heightProperty().multiply(0.8 / btnCount)
            );
        }

        buttons.get(0).setOnAction(e -> handleShowNote());
        buttons.get(1).setOnAction(e -> handleEncrypt());
        buttons.get(2).setOnAction(e -> handleDecrypt());
        buttons.get(3).setOnAction(e -> handleCreateKey());
        buttons.get(4).setOnAction(e -> handleShowKeys());

        root.getChildren().add(title);
        root.getChildren().addAll(buttons);

    }



    public Pane getRoot() {
        return root;
    }



    private void handleShowNote() {
        showInfo("使用须知","支持密钥生成，以及文件的AES加密、解密，使用时请务必保管好密钥\n本应用会在用户目录下创建key.txt、DataLocker.log分别用于保存密钥位置以及日志记录");
    }

    // ===================================
    // 加密
    // ===================================
    private void handleEncrypt() {
        FileChooser fc = new FileChooser();
        fc.setTitle("选择需要加密的文件");
        File file = fc.showOpenDialog(null);
        if (file == null) return;

        FileChooser keyChooser = new FileChooser();
        keyChooser.setTitle("选择密钥文件");
        keyChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("密钥文件 (*.key)", "*.key")
        );
        File key = keyChooser.showOpenDialog(null);
        if (key == null) return;

        try {
            EncryptFile.encryptFile(file.getAbsolutePath(), key.getAbsolutePath());
            showInfo("加密成功", "文件已加密:\n" + file.getAbsolutePath()+"使用的密钥文件:\n"+key.getAbsolutePath());
        } catch (Exception ex) {
            showError("加密失败", ex.getMessage());
        }
    }

    // ===================================
    // 解密
    // ===================================
    private void handleDecrypt() {
        // 选择加密文件
        FileChooser encChooser = new FileChooser();
        encChooser.setTitle("选择已加密文件");
        encChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("加密文件 (*.enc)", "*.enc")
        );
        File enc = encChooser.showOpenDialog(null);
        if (enc == null) return;

        // 选择密钥文件
        FileChooser keyChooser = new FileChooser();
        keyChooser.setTitle("选择密钥文件");
        keyChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("密钥文件 (*.key)", "*.key")
        );
        File key = keyChooser.showOpenDialog(null);
        if (key == null) return;

        // 选择目标目录
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("选择解密后保存的文件夹");
        File targetDir = dirChooser.showDialog(null);
        if (targetDir == null) return;

        try {
            DecryptFile.decryptFile(
                    enc.getAbsolutePath(),
                    targetDir.getAbsolutePath(),
                    key.getAbsolutePath()
            );

            showInfo("解密成功", "文件已保存到目录:\n" + targetDir.getAbsolutePath());
        } catch (Exception ex) {
            showError("解密失败", ex.getMessage());
        }
    }



    // ===================================
    // 生成密钥文件
    // ===================================
    private void handleCreateKey() {
        FileChooser fc = new FileChooser();
        fc.setTitle("选择保存密钥的位置");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Key File", "*.key"));
        fc.setInitialFileName("mykey.key");
        File file = fc.showSaveDialog(null);
        if (file == null) return;

        try {
            KeyManage.createKeyFile(file.getAbsolutePath());
            showInfo("密钥生成成功", "已生成密钥文件:\n" + file.getAbsolutePath());
        } catch (Exception ex) {
            showError("创建密钥失败", ex.getMessage());
        }
    }

    // ===================================
    // 查看密钥列表
    // ===================================
    private void handleShowKeys() {
        try {
            Set<String> keys = KeyManage.getKeyList();
            StringBuilder sb = new StringBuilder();
            keys.forEach(k -> sb.append(k).append("\n"));

            showInfo("密钥列表", sb.length() == 0 ? "暂无密钥" : sb.toString());
        } catch (Exception ex) {
            showError("读取密钥失败", ex.getMessage());
        }
    }

    // ===================================
    // 公共弹窗
    // ===================================
    private void showError(String header, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("错误");
        a.setHeaderText(header);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setPrefWidth(450);
        textArea.setPrefHeight(300);

        // 使用可滚动的大文本区
        alert.getDialogPane().setContent(textArea);

        alert.showAndWait();
    }

    private String autoGuessDecryptedName(String encryptedPath) {
        // 假设加密文件名形如 xxx.xxx.enc
        if (encryptedPath.endsWith(".enc")) {
            return encryptedPath.substring(0, encryptedPath.length() - 4);
        }
        // 否则默认追加 "_dec"
        return encryptedPath + "_dec";
    }

}
