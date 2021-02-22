package net.mahdilamb.charts;

import net.mahdilamb.charts.plots.Scatter;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import static net.mahdilamb.charts.fx.FXChart.show;
import static net.mahdilamb.charts.statistics.ArrayUtils.full;
import static net.mahdilamb.charts.statistics.ArrayUtils.linearlySpaced;


public class ChartFXTest {
    public static void main(String[] args) {
        int N = 100;
        double[] t = linearlySpaced(0, 1, N);
        double[] random_y0 = full(() -> ThreadLocalRandom.current().nextGaussian() + 5, N);
        double[] random_y1 = full(ThreadLocalRandom.current()::nextGaussian, N);
        double[] random_y2 = full(() -> ThreadLocalRandom.current().nextGaussian() - 5, N);
        show(
                new Scatter(t, random_y0).setMode(Scatter.Mode.MARKER_ONLY).setName("markers"),
                new Scatter(t, random_y1).setMode(Scatter.Mode.MARKER_AND_LINE).setName("lines+markers"),
                new Scatter(t, random_y2).setMode(Scatter.Mode.LINE_ONLY).setName("lines")

        );
    }
}
