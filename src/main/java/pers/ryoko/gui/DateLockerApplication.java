package pers.ryoko.gui;
/**
 * @author 网云2304 542307280411 李润东
 * DateLocker 主程序入口，用GUI方便用户操作
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DateLockerApplication extends Application{
    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("文件加密工具");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
