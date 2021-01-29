package net.mahdilamb.charts;

import net.mahdilamb.charts.series.Dataset;

import java.io.File;
import java.util.Objects;

import static net.mahdilamb.charts.PlotFactory.scatter;
import static net.mahdilamb.charts.fx.FXChart.show;

public class ChartFXTest {

    public static void main(String[] args) {
        final File source = new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("iris.csv")).getFile());
        final Dataset iris = Dataset.from(source);

        show("Sepal length vs width",
                800, 640,
                scatter(iris, "sepal_length", "sepal_width", "petal_length")
                        .setMarker('.')
                        .setName("sepal: length v width")
                        .setGroups(iris.getStringSeries("species"))
        );


    }

}
