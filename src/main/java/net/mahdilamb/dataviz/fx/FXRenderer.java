package net.mahdilamb.dataviz.fx;


import javafx.geometry.Pos;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import net.mahdilamb.dataviz.figure.FigureBase;
import net.mahdilamb.dataviz.figure.Renderer;
import net.mahdilamb.dataviz.graphics.Font;
import net.mahdilamb.dataviz.graphics.GraphicsBuffer;
import net.mahdilamb.dataviz.graphics.GraphicsContext;
import net.mahdilamb.dataviz.swing.SwingUtils;
import net.mahdilamb.dataviz.utils.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class FXRenderer extends Renderer<BufferedImage> {
    private final Layer canvas;
    private final Layer overlay;
    double scrollFactor = -0.001;
    private final FileChooser chooser = new FileChooser();
    private final Pane node;

    public FXRenderer(final FigureBase<?> figure) {
        super();
        canvas = new Layer(this, figure.getWidth(), figure.getHeight());
        overlay = new Layer(this, figure.getWidth(), figure.getHeight());
        init(figure);
        node = new StackPane(canvas, overlay);
        node.setBackground(
                new Background(
                        new BackgroundFill(
                                new javafx.scene.paint.Color(figure.getBackgroundColor().getRed() / 255d, figure.getBackgroundColor().getGreen() / 255d, figure.getBackgroundColor().getBlue() / 255d, figure.getBackgroundColor().getAlpha() / 255d),
                                null,
                                null
                        )));
        StackPane.setAlignment(canvas, Pos.TOP_LEFT);
        StackPane.setAlignment(overlay, Pos.TOP_LEFT);
        node.setOnScroll(e -> mouseScrolled(e.isControlDown(), e.isShiftDown(), e.getX(), e.getY(), e.getDeltaY() * scrollFactor));//TODO
        node.setOnMousePressed(e -> mousePressed(e.isControlDown(), e.isShiftDown(), e.getX(), e.getY()));
        node.setOnMouseReleased(e -> mouseReleased(e.isControlDown(), e.isShiftDown(), e.getX(), e.getY()));
        node.setOnMouseExited(e -> mouseExited());
        node.setOnMouseDragged(e -> mouseMoved(e.isControlDown(), e.isShiftDown(), e.getX(), e.getY()));
        node.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (e.getClickCount() == 2) {
                    mouseDoubleClicked(e.isControlDown(), e.isShiftDown(), e.getX(), e.getY());
                } else {
                    mouseClicked(e.isControlDown(), e.isShiftDown(), e.getX(), e.getY());
                }
            }
        });
        node.setOnMouseMoved(e -> mouseMoved(e.isControlDown(), e.isShiftDown(), e.getX(), e.getY()));

    }

    public Pane getNode() {
        return node;

    }

    @Override
    protected double getTextBaselineOffset(Font font) {
        return canvas.bufferedImage.getFontMetrics(SwingUtils.convert(font)).getAscent();
    }

    @Override
    protected double getTextWidth(Font font, String text) {
        return SwingUtils.getTextWidth(canvas.bufferedImage.getFontMetrics(SwingUtils.convert(font)), text);
    }

    @Override
    protected double getCharWidth(Font font, char character) {
        return canvas.bufferedImage.getFontMetrics(SwingUtils.convert(font)).charWidth(character);
    }

    @Override
    protected double getTextLineHeight(Font font, String text) {
        return SwingUtils.getLineHeight(canvas.bufferedImage.getFontMetrics(SwingUtils.convert(font)), text, 1);
    }

    @Override
    protected BufferedImage loadImage(InputStream stream) {
        try {
            return ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected GraphicsBuffer<BufferedImage> createBuffer(double width, double height, double translateX, double translateY, int overflowTop, int overflowLeft, int overflowBottom, int overflowRight) {
        return new BufferedImageFX(width, height, translateX, translateY, overflowTop, overflowLeft, overflowBottom, overflowRight);
    }

    @Override
    protected void drawBuffer(GraphicsBuffer<BufferedImage> context, GraphicsBuffer<BufferedImage> buffer, double x, double y) {
        final BufferedImageFX canvasBuffer = (BufferedImageFX) buffer;
        context.drawImage(canvasBuffer.bufferedImage, x - canvasBuffer.bufferedImage.overflowLeft, y - canvasBuffer.bufferedImage.overflowTop);
    }

    @Override
    protected boolean bufferSizeChanged(GraphicsBuffer<BufferedImage> buffer, double x, double y, double width, double height) {
        final BufferedImageFX canvasBuffer = (BufferedImageFX) buffer;//TODO full size changed
        return (canvasBuffer.bufferedImage.width) != width || (canvasBuffer.bufferedImage.height) != height;
    }

    @Override
    protected String getFromClipboard() {
        //TODO check
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            return clipboard.getString();
        }
        return null;
    }

    @Override
    protected void addToClipboard(String text) {
        //TODO check
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }


    @Override
    protected BufferedImage cropImage(BufferedImage source, int x, int y, int width, int height) {
        return source.getSubimage(x, y, width, height);
    }

    @Override
    protected double getImageWidth(BufferedImage image) {
        return image.getWidth();
    }

    @Override
    protected double getImageHeight(BufferedImage image) {
        return image.getHeight();
    }

    @Override
    protected byte[] bytesFromImage(BufferedImage image) {
        return SwingUtils.convertToByteArray(image);
    }

    @Override
    protected int argbFromImage(BufferedImage image, int x, int y) {
        return image.getRGB(x, y);
    }

    @Override
    protected File getOutputPath(List<String> fileTypes, String defaultExtension) {
        if (fileTypes == null) {
            chooser.setSelectedExtensionFilter(null);
        } else {
            chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(String.format("Image types %s", fileTypes), fileTypes));
        }
        File selectedFile;
        if ((selectedFile = chooser.showSaveDialog(overlay.getScene().getWindow())) != null) {
            if (!StringUtils.hasFileExtension(selectedFile.toString())) {
                return new File(selectedFile + defaultExtension);
            }
            return selectedFile;
        }
        return null;
    }

    @Override
    protected GraphicsContext<BufferedImage> getFigureContext() {
        return canvas;
    }

    @Override
    protected GraphicsContext<BufferedImage> getOverlayContext() {
        return overlay;
    }

    @Override
    protected void done() {
        canvas.sync();
        overlay.sync();
    }

    private static final class Layer extends BufferedImageFX implements GraphicsContext<BufferedImage> {
        private final FXRenderer renderer;

        Layer(FXRenderer renderer, double width, double height) {
            super(width, height);
            this.renderer = renderer;
        }

        @Override
        public Renderer<BufferedImage> getRenderer() {
            return renderer;
        }

    }

}
