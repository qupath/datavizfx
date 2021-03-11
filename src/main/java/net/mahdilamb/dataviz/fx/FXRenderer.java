package net.mahdilamb.dataviz.fx;


import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import net.mahdilamb.colormap.Color;
import net.mahdilamb.dataviz.Figure;
import net.mahdilamb.dataviz.PlotLayout;
import net.mahdilamb.dataviz.Renderer;
import net.mahdilamb.dataviz.Selection;
import net.mahdilamb.dataviz.graphics.*;
import net.mahdilamb.dataviz.utils.Numbers;
import net.mahdilamb.dataviz.utils.Variant;

import java.util.HashMap;
import java.util.Map;

public class FXRenderer extends Renderer<Image> {

    private final ChartPanel canvas = new ChartPanel();
    private final Map<PlotLayout, Selection> selections = new HashMap<>();
    private final Canvas overlay = new Canvas();
    double startX, startY;
    double scrollFactor = -0.001;
    boolean horizontalInputEnabled = true,
            verticalInputEnabled = true;
    net.mahdilamb.dataviz.Renderer.Tooltip hoverText;
    final Text testText = new Text();

    public FXRenderer(Figure figure) {
        super(figure);
        canvas.setWidth(figure.getWidth());
        canvas.setHeight(figure.getHeight());
        overlay.setHeight(figure.getHeight());
        overlay.setWidth(figure.getWidth());
        overlay.setOnScroll(this::scroll);
        overlay.setOnMousePressed(this::pressed);
        overlay.setOnMouseDragged(this::dragged);
        overlay.setOnMouseClicked(this::clicked);
        overlay.setOnMouseMoved(this::moved);
        overlay.getGraphicsContext2D().setFill(new javafx.scene.paint.Color(.2, .2, .2, .2));
        overlay.getGraphicsContext2D().setStroke(new javafx.scene.paint.Color(.8, .8, .8, 1));

        overlay.getGraphicsContext2D().setFillRule(FillRule.EVEN_ODD);
    }

    void moved(MouseEvent e) {
        hoverText = getHoverText(e.getX(), e.getY());

        updateOverlay();
    }

    void updateOverlay() {
        /*
        if (hoverText != null) {
            if (hoverText.hasChanges()) {
                overlay.getGraphicsContext2D().clearRect(0, 0, overlay.getWidth(), overlay.getHeight());

                double height = Toolkit.getToolkit().getFontLoader().getFontMetrics(overlay.getGraphicsContext2D().getFont()).getLineHeight();
                double y = hoverText.getY() - height * .5;
                double x = hoverText.getX();
                final String line = hoverText.getText();
                testText.setFont(overlay.getGraphicsContext2D().getFont());
                testText.setText(line);
                double width = testText.getLayoutBounds().getWidth();
                if ((x + width) > overlay.getWidth()) {
                    x -= width + 16;
                } else {
                    x += 16;
                }
                overlay.getGraphicsContext2D().setFill(FXUtils.convert(hoverText.getBackground()));
                overlay.getGraphicsContext2D().fillRect(x, y, width, height);
                overlay.getGraphicsContext2D().setFill(FXUtils.convert(hoverText.getForeground()));
                overlay.getGraphicsContext2D().strokeRect(x, y, width, height);
                overlay.getGraphicsContext2D().fillText(line, x, y + Toolkit.getToolkit().getFontLoader().getFontMetrics(overlay.getGraphicsContext2D().getFont()).getAscent());

                markTooltipOld(this);
            }
        } else {
            overlay.getGraphicsContext2D().clearRect(0, 0, overlay.getWidth(), overlay.getHeight());
            resetTooltip(this);

        }*/
    }

    void clicked(MouseEvent e) {
        final Selection.Polygon s = (Selection.Polygon) selections.getOrDefault(figure.getLayout(), new Selection.Polygon(overlay.getGraphicsContext2D().getFillRule() == FillRule.NON_ZERO));
        if (e.getButton() == MouseButton.SECONDARY) {
            overlay.getGraphicsContext2D().clearRect(0, 0, overlay.getWidth(), overlay.getHeight());
            s.clear(figure.getLayout());
            selections.remove(figure.getLayout());
            forceRefresh();
            return;
        }
        if (!e.isControlDown()) {
            return;
        }
        if (e.getClickCount() == 1) {
            s.add(figure.getLayout().getXAxis().getValueFromPosition(e.getX()), figure.getLayout().getYAxis().getValueFromPosition(e.getY()));
        } else {
            s.close();
            s.apply(figure.getLayout());
            forceRefresh();
        }
        drawSelection(figure.getLayout(), s);

        selections.put(figure.getLayout(), s);
    }

    void drawSelection(PlotLayout layout, Selection.Polygon selection) {
        overlay.getGraphicsContext2D().clearRect(0, 0, overlay.getWidth(), overlay.getHeight());
        if (selection == null || selection.size() <= 1) {
            return;
        }
        overlay.getGraphicsContext2D().beginPath();
        if (selection.size() > 1) {
            overlay.getGraphicsContext2D().moveTo(transformX(layout, selection.getX(0)), transformY(layout, selection.getY(0)));
            for (int i = 1; i < selection.size(); ++i) {
                overlay.getGraphicsContext2D().lineTo(transformX(layout, selection.getX(i)), transformY(layout, selection.getY(i)));
            }
            overlay.getGraphicsContext2D().closePath();
            overlay.getGraphicsContext2D().fill();
            overlay.getGraphicsContext2D().stroke();
        }

    }

    void drawSelection() {
        drawSelection(figure.getLayout(), (Selection.Polygon) selections.get(figure.getLayout()));
    }

    void dragged(MouseEvent e) {
        mouseDragged(e.getX(), e.getY());
        //TODO clip normally
        overlay.setClip(new Rectangle(getX(figure.getLayout().getXAxis()), getY(figure.getLayout().getYAxis()), getWidth(figure.getLayout().getXAxis()), getHeight(figure.getLayout().getYAxis())));
        drawSelection();
        startX = e.getX();
        startY = e.getY();
    }

    void pressed(MouseEvent e) {
        if (e.isControlDown()) {
            horizontalInputEnabled = verticalInputEnabled = false;
            return;
        }
        mouseInit(e.getX(), e.getY());

    }

    void scroll(ScrollEvent e) {
        mouseWheelMoved(e.getX(), e.getY(), (e.getDeltaY() * scrollFactor));
        //TODO clip normally
        overlay.setClip(new Rectangle(getX(figure.getLayout().getXAxis()), getY(figure.getLayout().getYAxis()), getWidth(figure.getLayout().getXAxis()), getHeight(figure.getLayout().getYAxis())));
        drawSelection();
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
        node.getChildren().addAll(canvas, overlay);
        StackPane.setAlignment(canvas, Pos.TOP_LEFT);
        StackPane.setAlignment(overlay, Pos.TOP_LEFT);
        refresh();
       /* todo setBackgroundColor(getBackgroundColor());
        redraw();*/
    }

    private static final class ChartPanel extends Canvas implements ChartCanvas<Image> {
        private final Text testText = new Text("Test");
        final Variant<Color, Gradient> currentFill = Variant.ofA(Color.BLACK);
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
        public void setFill(Color color) {
            this.currentFill.setToA(color);
            getGraphicsContext2D().setFill(FXUtils.convert(color));
        }

        @Override
        public void setFill(Gradient gradient) {
            this.currentFill.setToB(gradient);
            getGraphicsContext2D().setFill(FXUtils.convert(gradient));
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
                    getGraphicsContext2D().beginPath();
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
            currentFill.accept(this::setFill, this::setFill);
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
