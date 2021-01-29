package net.mahdilamb.charts.fx;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import net.mahdilamb.charts.graphics.Fill;
import net.mahdilamb.charts.utils.StringUtils;
import net.mahdilamb.colormap.Color;
import net.mahdilamb.geom2d.geometries.Geometries;

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

    /**
     * Convert Fx paint to abstract color
     *
     * @param fill the source color
     * @return abstract color
     */
    public static Color convert(Paint fill) {
        if (fill.getClass() == javafx.scene.paint.Color.class) {
            return new Color(((javafx.scene.paint.Color) fill).getRed(), ((javafx.scene.paint.Color) fill).getGreen(), ((javafx.scene.paint.Color) fill).getBlue(), ((javafx.scene.paint.Color) fill).getOpacity());
        }
        throw new UnsupportedOperationException(String.format("Cannot convert fill of type %s", fill.getClass()));
    }

    public static byte[] convert(final Image image) {
        final PixelReader pixelReader = image.getPixelReader();

        final int width = (int) Math.round(image.getWidth());
        final int height = (int) Math.round(image.getHeight());

        final byte[] buffer = new byte[4 * width * height];
        pixelReader.getPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), buffer, 0, width * 4);
        return buffer;

    }

    /**
     * Convert a fill to a javafx paint
     *
     * @param fill the fill to convert
     * @return the javafx paint
     */
    public static Paint convert(Fill fill) {
        if (fill.isGradient()) {
            final Fill.Gradient g = fill.getGradient();
            final Stop[] stops = new Stop[g.getColorMap().size()];
            int i = 0;
            for (final Map.Entry<Float, net.mahdilamb.colormap.Color> entry : g.getColorMap().entrySet()) {
                stops[i] = new Stop(entry.getKey(), convert(entry.getValue()));
                ++i;
            }
            if (g.getType() == Fill.GradientType.LINEAR) {
                return new LinearGradient(g.getStartX(), g.getStartY(), g.getEndX(), g.getEndY(), false, CycleMethod.NO_CYCLE, stops);
            } else {
                return new RadialGradient(g.getStartX(), g.getStartY(), g.getEndX(), g.getEndY(), Geometries.distance(g.getStartX(), g.getStartY(), g.getEndX(), g.getEndY()), false, CycleMethod.NO_CYCLE, stops);
            }
        } else {
            return new javafx.scene.paint.Color(fill.getColor().red(), fill.getColor().green(), fill.getColor().blue(), fill.getColor().alpha());
        }
    }

    public static Font convert(net.mahdilamb.charts.graphics.Font font) {
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
}
