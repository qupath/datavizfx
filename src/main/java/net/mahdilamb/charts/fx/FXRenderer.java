package net.mahdilamb.charts.fx;


import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;

import net.mahdilamb.charts.Figure;
import net.mahdilamb.charts.Renderer;
import net.mahdilamb.charts.Title;
import net.mahdilamb.charts.graphics.*;
import net.mahdilamb.charts.utils.Numbers;
import net.mahdilamb.colormap.Color;

public class FXRenderer extends Renderer< Image> {

    private final ChartPanel canvas = new ChartPanel();
    private Pane parent;

    public FXRenderer(Figure figure) {
        super(figure);
        canvas.setWidth(figure.getWidth());
        canvas.setHeight(figure.getHeight());
    }

    @Override
    protected ChartCanvas<Image> getCanvas() {
        return canvas;
    }

    @Override
    protected double getTextLineHeight(Title title, double maxWidth, double lineSpacing) {
        FontMetrics fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(FXUtils.convert(title.getFont()));
        final String text = title.getText();
        int lineCount = 0;
        int i = 0;
        double currentWidth = 0;
        while (i < text.length()) {
            char c = text.charAt(i++);
            currentWidth += fontMetrics.getCharWidth(c);
            if (Character.isWhitespace(c) && i != 0 && currentWidth > maxWidth) {
                ++lineCount;
                currentWidth = 0;

            }
        }
        if (currentWidth > 0) {
            ++lineCount;
        }
        return lineSpacing * lineCount * fontMetrics.getLineHeight();
    }

    @Override
    protected double getTextBaselineOffset(Font font) {
        canvas.testText.setFont(FXUtils.convert(font));
        return canvas.testText.getBaselineOffset();
    }

    @Override
    protected double getTextWidth(Font font, String text) {
        canvas.testText.setFont(FXUtils.convert(font));
        canvas.testText.setText(text);
        return canvas.testText.getLayoutBounds().getWidth();
    }

    @Override
    protected double getCharWidth(Font font, char character) {
        FontMetrics fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(FXUtils.convert(font));
        return fontMetrics.getCharWidth(character);
    }

    @Override
    protected double getTextLineHeight(Font font) {
        FontMetrics fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(FXUtils.convert(font));
        return fontMetrics.getLineHeight();
    }

    @Override
    protected double getImageWidth(Image image) {
        return image.getWidth();
    }

    @Override
    protected double getImageHeight(Image image) {
        return image.getHeight();
    }

    @Override
    protected byte[] bytesFromImage(Image image) {
        return FXUtils.convert(image);
    }

    @Override
    protected int argbFromImage(Image image, int x, int y) {
        return image.getPixelReader().getArgb(x, y);
    }
/* todo
    @Override
    protected void backgroundChanged() {
        if (getBackgroundColor() != null) {
            parent.setBackground(new Background(new BackgroundFill(FXUtils.convert(getBackgroundColor()), null, null)));
        }
    }
*/
    @Override
    protected double getTextLineHeight(Title title) {
        return Toolkit.getToolkit().getFontLoader().getFontMetrics(FXUtils.convert(title.getFont())).getLineHeight();
    }


    /**
     * Add this chart to a node
     *
     * @param node the node to add to
     */
    public void addTo(Pane node) {
        this.parent = node;
        node.getChildren().add(canvas);
        StackPane.setAlignment(canvas, Pos.TOP_LEFT);
        refresh();
       /* todo setBackgroundColor(getBackgroundColor());
        redraw();*/
    }

    private static final class ChartPanel extends Canvas implements ChartCanvas<Image> {
        private final Text testText = new Text("Test");
        Paint currentFill = Paint.BLACK_FILL;
        Stroke currentStroke = Stroke.SOLID;
        final Affine affine = new Affine();

        ChartPanel() {
        }

        @Override
        public void resetRect(double x, double y, double width, double height) {
            getGraphicsContext2D().clearRect(x, y, width, height);
        }

        @Override
        public void reset() {
            getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        }

        @Override
        public void done() {
            //ignored
        }

        @Override
        public void strokeRect(double x, double y, double width, double height) {
            getGraphicsContext2D().strokeRect(x, y, width, height);
        }

        @Override
        public void fillRect(double x, double y, double width, double height) {
            getGraphicsContext2D().fillRect(x, y, width, height);

        }

        @Override
        public void strokeRoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight) {
            getGraphicsContext2D().strokeRoundRect(x, y, width, height, arcWidth, arcHeight);
        }

        @Override
        public void fillRoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight) {
            getGraphicsContext2D().fillRoundRect(x, y, width, height, arcWidth, arcHeight);
        }

        @Override
        public void strokeOval(double x, double y, double width, double height) {
            getGraphicsContext2D().strokeOval(x, y, width, height);
        }

        @Override
        public void fillOval(double x, double y, double width, double height) {
            getGraphicsContext2D().fillOval(x, y, width, height);
        }

        @Override
        public void strokeLine(double x0, double y0, double x1, double y1) {
            getGraphicsContext2D().strokeLine(x0, y0, x1, y1);
        }

        @Override
        public void setFill(Paint fill) {
            this.currentFill = fill;
            getGraphicsContext2D().setFill(FXUtils.convert(fill));
        }

        @Override
        public void strokePolygon(double[] xPoints, double[] yPoints, int numPoints) {
            getGraphicsContext2D().strokePolygon(xPoints, yPoints, numPoints);
        }

        @Override
        public void strokePolyline(double[] xPoints, double[] yPoints, int numPoints) {
            getGraphicsContext2D().strokePolyline(xPoints, yPoints, numPoints);
        }

        @Override
        public void fillPolygon(double[] xPoints, double[] yPoints, int numPoints) {
            getGraphicsContext2D().fillPolygon(xPoints, yPoints, numPoints);
        }

        @Override
        public void fillText(String text, double x, double y) {
            getGraphicsContext2D().fillText(text, x, y);

        }

        @Override
        public void fillText(String text, double x, double y, double rotationDegrees, double pivotX, double pivotY) {
            affine.setToIdentity();
            affine.appendRotation(rotationDegrees, pivotX, pivotY);
            getGraphicsContext2D().setTransform(affine);
            getGraphicsContext2D().fillText(text, x, y);
            affine.setToIdentity();
            getGraphicsContext2D().setTransform(affine);
        }

        @Override
        public void setFont(Font font) {
            testText.setFont(FXUtils.convert(font));
            getGraphicsContext2D().setFont(testText.getFont());

        }

        @Override
        public void setClip(ClipShape shape, double x, double y, double width, double height) {
            getGraphicsContext2D().save();
            switch (shape) {
                case ELLIPSE:
                    double a = width * .5, b = height * .5;
                    double cx = x + a, cy = y + b;
                    getGraphicsContext2D().beginPath();
                    getGraphicsContext2D().moveTo(cx + a, cy);
                    getGraphicsContext2D().bezierCurveTo(
                            cx + a, cy - (Numbers.MORTENSEN_CONSTANT * b),
                            cx + (Numbers.MORTENSEN_CONSTANT * a), cy - b,
                            cx, cy - b
                    );
                    getGraphicsContext2D().bezierCurveTo(
                            cx - (Numbers.MORTENSEN_CONSTANT * a), cy - b,
                            cx - a, cy - (Numbers.MORTENSEN_CONSTANT * b),
                            cx - a, cy
                    );
                    getGraphicsContext2D().bezierCurveTo(
                            cx - a, cy + (Numbers.MORTENSEN_CONSTANT * b),
                            cx - (Numbers.MORTENSEN_CONSTANT * a), cy + b,
                            cx, cy + b
                    );
                    getGraphicsContext2D().bezierCurveTo(
                            cx + (Numbers.MORTENSEN_CONSTANT * a), cy + b,
                            cx + a, cy + (Numbers.MORTENSEN_CONSTANT * b),
                            cx + a, cy
                    );
                    getGraphicsContext2D().closePath();
                    getGraphicsContext2D().clip();
                    break;
                case RECTANGLE:
                    getGraphicsContext2D().rect(x, y, width, height);
                    getGraphicsContext2D().clip();
                    break;
                default:
                    throw new UnsupportedOperationException();

            }
        }

        @Override
        public void clearClip() {
            getGraphicsContext2D().restore();
            setFill(currentFill);
            setStroke(currentStroke);
        }

        @Override
        public void drawImage(Image image, double x, double y) {
            getGraphicsContext2D().drawImage(image, x, y);
        }

        @Override
        public void setStroke(Stroke stroke) {
            this.currentStroke = stroke;
            getGraphicsContext2D().setLineWidth(stroke.getWidth());
            getGraphicsContext2D().setLineCap(FXUtils.convert(stroke.getEndCap()));
            getGraphicsContext2D().setLineJoin(FXUtils.convert(stroke.getLineJoin()));
            getGraphicsContext2D().setMiterLimit(stroke.getMiterLimit());
            getGraphicsContext2D().setLineDashOffset(stroke.getDashOffset());
            getGraphicsContext2D().setLineDashes(stroke.getDashes());
        }

        @Override
        public void setStroke(Color color) {
            getGraphicsContext2D().setStroke(FXUtils.convert(color));
        }

        @Override
        public void beginPath() {
            getGraphicsContext2D().beginPath();
        }

        @Override
        public void moveTo(double endX, double endY) {
            getGraphicsContext2D().moveTo(endX, endY);
        }

        @Override
        public void lineTo(double endX, double endY) {
            getGraphicsContext2D().lineTo(endX, endY);
        }

        @Override
        public void quadTo(double cpX, double cpY, double endX, double endY) {
            getGraphicsContext2D().quadraticCurveTo(cpX, cpY, endX, endY);
        }

        @Override
        public void curveTo(double cp1X, double cp1Y, double cp2X, double cp2Y, double endX, double endY) {
            getGraphicsContext2D().bezierCurveTo(cp1X, cp1Y, cp2X, cp2Y, endX, endY);
        }

        @Override
        public void closePath() {
            getGraphicsContext2D().closePath();
        }

        @Override
        public void fill() {
            getGraphicsContext2D().fill();
        }

        @Override
        public void stroke() {
            getGraphicsContext2D().stroke();
        }
    }

}
