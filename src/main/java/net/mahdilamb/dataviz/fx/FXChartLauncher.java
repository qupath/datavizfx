package net.mahdilamb.dataviz.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.mahdilamb.dataviz.Figure;

/**
 * Launcher for FX charts
 */
public final class FXChartLauncher extends Application {

    private static FXRenderer chart;


    @Override
    public void start(Stage stage) {
        final StackPane root = new StackPane();
        final Scene scene = new Scene(root, chart.getFigure().getWidth(), chart.getFigure().getHeight());
        chart.addTo(root);
        // stage.setTitle(chart.getTitle().getText());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Show the given chart
     *
     * @param chart the chart to show
     * @return the chart
     */
    public static FXRenderer launch(FXRenderer chart) {
        FXChartLauncher.chart = chart;
        launch(FXChartLauncher.class, (String) null);
        return chart;
    }


    public static FXRenderer launch(Figure fig) {
        return launch(new FXRenderer(fig));
    }

}
