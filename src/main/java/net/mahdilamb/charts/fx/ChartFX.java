package net.mahdilamb.charts.fx;


import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import net.mahdilamb.charts.Chart;
import net.mahdilamb.charts.graphics.*;
import net.mahdilamb.charts.layouts.Plot;
import net.mahdilamb.charts.plots.PlotSeries;
import net.mahdilamb.geom2d.geometries.Ellipse;

public class ChartFX<P extends Plot<S>, S extends PlotSeries<S>> extends Chart<P, S> {


    private final ChartPanel canvas = new ChartPanel();
    private Pane parent;

    public ChartFX(String title, double width, double height, P plot) {
        super(title, width, height, plot);
        canvas.setWidth(width);
        canvas.setHeight(height);
    }

    @Override
    protected ChartCanvas<Image> getCanvas() {
        return canvas;
    }

    @Override
    protected double getTextBaselineOffset(Font font) {
        canvas.testText.setFont(FXUtils.convert(font));
        return canvas.testText.getBaselineOffset();
    }

    @Override
    protected double getTextWidth(Font font, String text) {
        canvas.testText.setFont(FXUtils.convert(font));
        canvas. testText.setText(text);
        return  canvas.testText.getLayoutBounds().getWidth();
    }

    @Override
    protected double getImageWidth(Object image) {
        return ((Image)image).getWidth();
    }

    @Override
    protected double getImageHeight(Object image) {
        return ((Image)image).getHeight();
    }

    @Override
    protected byte[] bytesFromImage(Object image) {
        return FXUtils.convert(((Image)image));
    }

    @Override
    public void setBackgroundColor(String colorName) {
        super.setBackgroundColor(colorName);
        if (getBackgroundColor() != null) {
            parent.setBackground(new Background(new BackgroundFill(FXUtils.convert(getBackgroundColor()), null, null)));
        }
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
        setBackgroundColor(getBackgroundColor());
        layout();
    }

    private static final class ChartPanel extends Canvas implements ChartCanvas<Image> {
        private final Text testText = new Text("Test");
        Fill currentFill = Fill.BLACK_FILL;
        Stroke currentStroke = Stroke.BLACK_STROKE;

        ChartPanel() {
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
        public void setFill(Fill fill) {
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
                            cx + a, cy - (Ellipse.MORTENSEN_CONSTANT * b),
                            cx + (Ellipse.MORTENSEN_CONSTANT * a), cy - b,
                            cx, cy - b
                    );
                    getGraphicsContext2D().bezierCurveTo(
                            cx - (Ellipse.MORTENSEN_CONSTANT * a), cy - b,
                            cx - a, cy - (Ellipse.MORTENSEN_CONSTANT * b),
                            cx - a, cy
                    );
                    getGraphicsContext2D().bezierCurveTo(
                            cx - a, cy + (Ellipse.MORTENSEN_CONSTANT * b),
                            cx - (Ellipse.MORTENSEN_CONSTANT * a), cy + b,
                            cx, cy + b
                    );
                    getGraphicsContext2D().bezierCurveTo(
                            cx + (Ellipse.MORTENSEN_CONSTANT * a), cy + b,
                            cx + a, cy + (Ellipse.MORTENSEN_CONSTANT * b),
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
            getGraphicsContext2D().setStroke(FXUtils.convert(stroke.getColor()));
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
