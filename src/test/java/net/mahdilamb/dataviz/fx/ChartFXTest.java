package net.mahdilamb.dataviz.fx;


import net.mahdilamb.dataframe.DataFrame;
import net.mahdilamb.dataviz.plots.Scatter;

import java.io.File;
import java.util.Objects;


public class ChartFXTest {
    public static DataFrame loadDataFromResource(final String resourcePath) {
        return DataFrame.from(new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(resourcePath)).getFile()));
    }

    /*
        static void density2d() {
            DataFrame df = loadDataFromResource("tips.csv");
            final String x = "total_bill";
            final String y = "tip";
            new Density2D(df, x, y)
                    .getFigure()
                    .addTrace(
                            new Scatter(df, x, y)
                                    .setSize(4)
                    )
                    .show(FXChart::launch)
            ;
        }*/
    static final DataFrame iris = loadDataFromResource("iris.csv");

    public static void main(String[] args) {

       new Scatter(iris, "petal_length", "petal_width")
               .setColors("species")
                .show();
        //   density2d();
        /*int N = 1_000;
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
                .show(FXChart::launch)
        ;
*/
    }
}
