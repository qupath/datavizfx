package net.mahdilamb.dataviz.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.mahdilamb.dataviz.figure.FigureBase;

/**
 * Launcher for FX charts
 */
public final class FXLauncher extends Application {

    private static FXRenderer renderer;

    @Override
    public void start(Stage stage) {
        initStage(stage, renderer)
                .show();
    }

    /**
     * Show the given figure
     *
     * @param fig the figure to show
     */
    public static void launch(FigureBase<?> fig) {
        final FXRenderer renderer = new FXRenderer(fig);
        try {
            Platform.runLater(() -> initStage(new Stage(), renderer).show());
        } catch (IllegalStateException e) {
            FXLauncher.renderer = renderer;
            launch(FXLauncher.class, (String) null);
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
        final Scene scene = new Scene(renderer.getNode(), renderer.getFigure().getWidth(), renderer.getFigure().getHeight());
        stage.setTitle(renderer.getFigure().getTitle());
        stage.setScene(scene);
        return stage;
    }
}
