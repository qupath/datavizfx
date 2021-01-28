package net.mahdilamb.charts;


import javafx.scene.text.Font;
import net.mahdilamb.charts.plots.Plot;
import net.mahdilamb.charts.series.PlotSeries;
import net.mahdilamb.charts.styles.Text;

public class ChartFX<P extends Plot<S>, S extends PlotSeries<S>> extends Chart<P, S> {


    private static final class TextFx extends Chart.TextImpl {
        private static javafx.scene.text.Text testText = new javafx.scene.text.Text();

        @Override
        protected void calculateSize() {
            updateSize(testText.getLayoutBounds().getWidth(), testText.getLayoutBounds().getHeight());
        }
    }

    private static final class ImageFX extends Chart.ImageImpl {

        @Override
        byte[] calculateBytes() {
            //TODO
            return new byte[0];
        }
    }

    protected ChartFX(String title, double width, double height, P plot) {
        super(title, width, height, plot);
    }

    private static javafx.scene.text.Text testText = new javafx.scene.text.Text();


    @Override
    protected double getTextWidth(Text text) {
        if (testText.getFont().getSize() != text.getFontSize()) {
            testText.setFont(new Font(text.getFontSize()));
        }
        return testText.getLayoutBounds().getWidth();
    }

    @Override
    protected double getTextHeight(Text text) {
        if (testText.getFont().getSize() != text.getFontSize()) {
            testText.setFont(new Font(text.getFontSize()));
        }
        return testText.getLayoutBounds().getHeight();
    }

    @Override
    protected void draw() {
//TODO
    }

}
