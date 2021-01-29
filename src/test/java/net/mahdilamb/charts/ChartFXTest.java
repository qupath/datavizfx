package net.mahdilamb.charts;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.mahdilamb.charts.fx.ChartFX;
import net.mahdilamb.charts.graphics.Font;

import java.io.File;

public class ChartFXTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        final StackPane root = new StackPane();
        final ChartFX<?, ?> chart = new ChartFX<>("Chart", 200, 200, null);
        final Scene scene = new Scene(root, chart.getWidth(), chart.getHeight());
        chart.addTo(root);
        stage.setScene(scene);
        stage.show();
        chart.saveAsSVG(new File("D:\\mahdi\\Desktop\\12121.svg"));
    }
}
