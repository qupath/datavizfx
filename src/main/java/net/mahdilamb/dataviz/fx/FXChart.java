package net.mahdilamb.dataviz.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.mahdilamb.dataviz.Figure;

/**
 * Launcher for FX charts
 */
public final class FXChart extends Application {

    private static FXRenderer chart;

    @Override
    public void start(Stage stage) {
        initStage(stage, chart)
                .show();
    }

    /**
     * Show the given figure
     *
     * @param fig the figure to show
     */
    public static void launch(Figure fig) {
        final FXRenderer renderer = new FXRenderer(fig);
        try {
            Platform.runLater(() -> initStage(new Stage(), renderer).show());
        }catch (IllegalStateException e){
            FXChart.chart = renderer;
            launch(FXChart.class, (String) null);
        }

    }

    /**
     * Initialize a renderer into the given stage
     *
     * @param stage    the stage
     * @param renderer the fx figure renderer
     * @return the supplied stage
     */
    private static Stage initStage(final Stage stage, final FXRenderer renderer) {
        final StackPane root = new StackPane();
        final Scene scene = new Scene(root, renderer.getFigure().getWidth(), renderer.getFigure().getHeight());
        renderer.addTo(root);
        stage.setTitle(renderer.getFigure().getTitle());
        root.setBackground(new Background(new BackgroundFill(FXUtils.convert(renderer.getFigure().getBackgroundColor()), null, null)));
        stage.setScene(scene);
        return stage;
    }
}
