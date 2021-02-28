package net.mahdilamb.charts;


import net.mahdilamb.charts.fx.FXChartLauncher;

import java.util.concurrent.ThreadLocalRandom;

import static net.mahdilamb.statistics.ArrayUtils.full;
import static net.mahdilamb.statistics.ArrayUtils.linearlySpaced;


public class ChartFXTest {
    public static void main(String[] args) {
        int N = 100;
        double[] random_x = linearlySpaced(0, 1, N);
        double[] random_y0 = full(() -> ThreadLocalRandom.current().nextGaussian() + 5, N);
        double[] random_y1 = full(ThreadLocalRandom.current()::nextGaussian, N);
        double[] random_y2 = full(() -> ThreadLocalRandom.current().nextGaussian() - 5, N);

        new Figure()
                .addTraces(
                        new Scatter(random_x, random_y0)
                                .setName("markers")
                                .setMarkerMode("markers")

                )
                .setTitle("Figure")
                .show(FXChartLauncher::launch);

    }
}
