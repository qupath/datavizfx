package net.mahdilamb.dataviz.fx;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import net.mahdilamb.dataviz.graphics.Paint;
import net.mahdilamb.dataviz.graphics.Stroke;
import net.mahdilamb.dataviz.utils.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

final class FXUtils {
    private FXUtils() {

    }

    /**
     * Convert abstract color to FX color
     *
     * @param color the source color
     * @return FX color
     */
    public static javafx.scene.paint.Color convert(final net.mahdilamb.colormap.Color color) {
        return new javafx.scene.paint.Color(color.red(), color.green(), color.blue(), color.alpha());
    }

    public static byte[] convert(final Image i) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(convertToBufferedImage(i), "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();

    }

    public static BufferedImage convertToBufferedImage(final Image i) {
        PixelReader pr = i.getPixelReader();
        int iw = (int) i.getWidth();
        int ih = (int) i.getHeight();
        final BufferedImage bufferedImage = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < ih; ++y) {
            for (int x = 0; x < iw; ++x) {
                final int argb = pr.getArgb(x, y);
                bufferedImage.setRGB(x, y, argb);
            }
        }
        return bufferedImage;
    }

    public static void saveAsPNG(final File file, final Image img) throws IOException {
        ImageIO.write(convertToBufferedImage(img), "png", file);
    }

    private static double distance(final double ax, final double ay, final double bx, final double by) {
        final double deltaX = ax - bx;
        final double deltaY = ay - by;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Convert a fill to a javafx paint
     *
     * @param fill the fill to convert
     * @return the javafx paint
     */
    public static javafx.scene.paint.Paint convert(Paint fill) {
        if (fill.isGradient()) {
            final Paint.Gradient g = fill.getGradient();
            final Stop[] stops = new Stop[g.getColorMap().size()];
            int i = 0;
            for (final Map.Entry<Float, net.mahdilamb.colormap.Color> entry : g.getColorMap().entrySet()) {
                stops[i] = new Stop(entry.getKey(), convert(entry.getValue()));
                ++i;
            }
            if (g.getType() == Paint.GradientType.LINEAR) {
                return new LinearGradient(g.getStartX(), g.getStartY(), g.getEndX(), g.getEndY(), false, CycleMethod.NO_CYCLE, stops);
            } else {
                return new RadialGradient(g.getStartX(), g.getStartY(), g.getEndX(), g.getEndY(), distance(g.getStartX(), g.getStartY(), g.getEndX(), g.getEndY()), false, CycleMethod.NO_CYCLE, stops);
            }
        } else {
            return new javafx.scene.paint.Color(fill.getColor().red(), fill.getColor().green(), fill.getColor().blue(), fill.getColor().alpha());
        }
    }

    public static Font convert(net.mahdilamb.dataviz.graphics.Font font) {
        final FontPosture posture;
        switch (font.getStyle()) {
            case ITALIC:
                posture = FontPosture.ITALIC;
                break;
            case NORMAL:
                posture = FontPosture.REGULAR;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        final FontWeight weight;
        switch (font.getWeight()) {
            case NORMAL:
                weight = FontWeight.NORMAL;
                break;
            case BOLD:
                weight = FontWeight.BOLD;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return Font.font(StringUtils.snakeToTitleCase(font.getFamily().name()), weight, posture, font.getSize());

    }

    public static StrokeLineCap convert(Stroke.EndCap endCap) {
        switch (endCap) {
            case BUTT:
                return StrokeLineCap.BUTT;
            case ROUND:
                return StrokeLineCap.ROUND;
            case SQUARE:
                return StrokeLineCap.SQUARE;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static StrokeLineJoin convert(Stroke.LineJoin lineJoin) {
        switch (lineJoin) {
            case ROUND:
                return StrokeLineJoin.ROUND;
            case BEVEL:
                return StrokeLineJoin.BEVEL;
            case MITER:
                return StrokeLineJoin.MITER;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
