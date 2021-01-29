package net.mahdilamb.charts.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Launcher for FX charts
 */
public final class FXChartLauncher extends Application {

    private static FXChart<?, ?> chart;

    @Override
    public void start(Stage stage) {
        final StackPane root = new StackPane();
        final Scene scene = new Scene(root, chart.getWidth(), chart.getHeight());
        chart.addTo(root);
        stage.setTitle(chart.getTitle().getText());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Show the given chart
     *
     * @param chart the chart to show
     */
    public static void launch(FXChart<?, ?> chart) {
        FXChartLauncher.chart = chart;
        Application.launch(FXChartLauncher.class, (String) null);

    }

}
