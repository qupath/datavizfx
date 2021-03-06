package net.mahdilamb.dataviz;


import net.mahdilamb.dataviz.fx.FXChartLauncher;
import net.mahdilamb.dataviz.plots.Scatter;

import java.util.concurrent.ThreadLocalRandom;

import static net.mahdilamb.statistics.ArrayUtils.full;
import static net.mahdilamb.statistics.ArrayUtils.linearlySpaced;


public class ChartFXTest {
    public static void main(String[] args) {
        int N = 1_000;
        double[] random_x = linearlySpaced(0, 1, N);
        double[] random_y0 = full(() -> ThreadLocalRandom.current().nextGaussian() + 7.5, N);
        double[] random_y1 = full(ThreadLocalRandom.current()::nextGaussian, N);
        double[] random_y2 = full(() -> ThreadLocalRandom.current().nextGaussian() - 5, N);

        new Figure()
                .addTraces(
                        new Scatter(random_x, random_y0)
                                .setName("markers")
                                .setMarkerMode("markers")
                                .setColormap("viridis")
                                .setColors(random_y1)
                                .showEdges(true)
                                //.setSize(4)
                                .setSizes(random_y2)
                                .setXLabel("x")
                                .setYLabel("y")

                )
                .updateLayout(
                        layout -> layout
                                .setTitle("Figure")
                                .apply(Theme.Plotly)
                )
                .show(FXChartLauncher::launch)
        ;

    }
}
