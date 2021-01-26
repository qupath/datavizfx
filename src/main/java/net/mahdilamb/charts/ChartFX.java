package net.mahdilamb.charts;


import javafx.scene.text.Font;
import net.mahdilamb.charts.styles.Text;

public class ChartFX<L extends Plot> extends Chart<L> {
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

    }

}
