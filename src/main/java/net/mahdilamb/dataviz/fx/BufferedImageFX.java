package net.mahdilamb.dataviz.fx;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import net.mahdilamb.dataviz.graphics.*;
import net.mahdilamb.dataviz.graphics.Font;
import net.mahdilamb.dataviz.graphics.Stroke;
import net.mahdilamb.dataviz.swing.BufferedImageExtended;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.nio.IntBuffer;

class BufferedImageFX extends ImageView implements GraphicsBuffer<BufferedImage> {
    final BufferedImageExtended bufferedImage;
    private final WritableImage image;
    boolean hasChanges = true;

    BufferedImageFX(double width, double height) {
        bufferedImage = new BufferedImageExtended(width, height, 0, 0, 0, 0, 0, 0);
        image = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
        setImage(image);
    }

    public BufferedImageFX(double width, double height, double x, double y, int overflowTop, int overflowLeft, int overflowBottom, int overflowRight) {
        bufferedImage = new BufferedImageExtended(width, height, x, y, overflowTop, overflowLeft, overflowBottom, overflowRight);
        image = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
        setImage(image);
    }

    /**
     * @return the writable image after syncing with buffered image
     * @implNote the method is modified from the JavaFX SDK module javafx.swing (javafx.embed.swing.SwingFXUtils#toFXImage)
     */
    WritableImage sync() {
        if (hasChanges) {
            final PixelWriter pw = image.getPixelWriter();
            final DataBufferInt db = (DataBufferInt) bufferedImage.getRaster().getDataBuffer();
            final SampleModel sm = bufferedImage.getRaster().getSampleModel();
            final int scan = sm instanceof SinglePixelPackedSampleModel ?
                    ((SinglePixelPackedSampleModel) sm).getScanlineStride() :
                    0;
            final PixelFormat<IntBuffer> pf = (bufferedImage.isAlphaPremultiplied() ?
                    PixelFormat.getIntArgbPreInstance() :
                    PixelFormat.getIntArgbInstance());
            pw.setPixels(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pf, db.getData(), bufferedImage.getRaster().getDataBuffer().getOffset(), scan);
            hasChanges = false;
        }
        return image;
    }

    void markOld() {
        hasChanges = true;
    }

    @Override
    public void reset() {
        bufferedImage.reset();
        markOld();
    }

    @Override
    public void done() {
        bufferedImage.done();
        markOld();

    }

    @Override
    public void strokeRect(double x, double y, double width, double height) {
        bufferedImage.strokeRect(x, y, width, height);
        markOld();
    }

    @Override
    public void fillRect(double x, double y, double width, double height) {
        bufferedImage.fillRect(x, y, width, height);
        markOld();
    }

    @Override
    public void strokeRoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight) {
        bufferedImage.strokeRoundRect(x, y, width, height, arcWidth, arcHeight);
        markOld();
    }

    @Override
    public void fillRoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight) {
        bufferedImage.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
        markOld();
    }

    @Override
    public void strokeOval(double x, double y, double width, double height) {
        bufferedImage.strokeOval(x, y, width, height);
        markOld();
    }

    @Override
    public void fillOval(double x, double y, double width, double height) {
        bufferedImage.fillOval(x, y, width, height);
        markOld();
    }

    @Override
    public void strokeLine(double x0, double y0, double x1, double y1) {
        bufferedImage.strokeLine(x0, y0, x1, y1);
        markOld();
    }

    @Override
    public void setFill(Color color) {
        bufferedImage.setFill(color);

    }

    @Override
    public void setFill(Gradient gradient) {
        bufferedImage.setFill(gradient);
    }

    @Override
    public void setStroke(Stroke stroke) {
        bufferedImage.setStroke(stroke);
    }

    @Override
    public void setStroke(Color color) {
        bufferedImage.setStroke(color);
    }

    @Override
    public void beginPath() {
        bufferedImage.beginPath();
    }

    @Override
    public void moveTo(double endX, double endY) {
        bufferedImage.moveTo(endX, endY);
    }

    @Override
    public void lineTo(double endX, double endY) {
        bufferedImage.lineTo(endX, endY);
    }

    @Override
    public void quadTo(double cpX, double cpY, double endX, double endY) {
        bufferedImage.quadTo(cpX, cpY, endX, endY);
    }

    @Override
    public void curveTo(double cp1X, double cp1Y, double cp2X, double cp2Y, double endX, double endY) {
        bufferedImage.curveTo(cp1X, cp1Y, cp2X, cp2Y, endX, endY);
    }

    @Override
    public void arcTo(double rx, double ry, double xAxisRotationDegrees, boolean largeArc, boolean sweepFlag, double endX, double endY) {
        bufferedImage.arcTo(rx, ry, xAxisRotationDegrees, largeArc, sweepFlag, endX, endY);
    }

    @Override
    public void closePath() {
        bufferedImage.closePath();
    }

    @Override
    public void fill() {
        bufferedImage.fill();
        markOld();
    }

    @Override
    public void stroke() {
        bufferedImage.stroke();
        markOld();
    }

    @Override
    public void fillText(String text, double x, double y) {
        bufferedImage.fillText(text, x, y);
        markOld();
    }

    @Override
    public void fillText(String text, double x, double y, double rotationDegrees, double pivotX, double pivotY) {
        bufferedImage.fillText(text, x, y, rotationDegrees, pivotX, pivotY);
        markOld();
    }

    @Override
    public void setFont(Font font) {
        bufferedImage.setFont(font);
    }

    @Override
    public void setClip(ClipShape shape, double x, double y, double width, double height) {
        bufferedImage.setClip(shape, x, y, width, height);
    }

    @Override
    public void clearClip() {
        bufferedImage.clearClip();
    }

    @Override
    public void drawImage(BufferedImage bufferedImage, double x, double y) {
        this.bufferedImage.drawImage(bufferedImage, x, y);
        markOld();
    }

    @Override
    public void setGlobalAlpha(double alpha) {
        bufferedImage.setGlobalAlpha(alpha);
    }

    @Override
    public void strokePolygon(double[] xPoints, double[] yPoints, int numPoints) {
        bufferedImage.strokePolygon(xPoints, yPoints, numPoints);
        markOld();
    }

    @Override
    public void strokePolyline(double[] xPoints, double[] yPoints, int numPoints) {
        bufferedImage.strokePolyline(xPoints, yPoints, numPoints);
        markOld();
    }

    @Override
    public void fillPolygon(double[] xPoints, double[] yPoints, int numPoints) {
        bufferedImage.fillPolygon(xPoints, yPoints, numPoints);
        markOld();
    }
}
