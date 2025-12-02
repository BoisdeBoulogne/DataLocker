package pers.ryoko.gui;
/**
 * @author 网云2304 542307280411 李润东
 * DateLocker 主程序入口，用GUI方便用户操作
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DataLockerApplication extends Application{
    @Override
    public void start(Stage stage) {
        MainUI ui = new MainUI();
        Scene scene = new Scene(ui.getRoot(), 600, 400);

        stage.setTitle("DateLocker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
