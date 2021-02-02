package net.mahdilamb.charts;

import net.mahdilamb.charts.series.DataFrame;

import java.io.File;

import static net.mahdilamb.charts.PlotFactory.scatter;
import static net.mahdilamb.charts.fx.FXChart.show;

public class ChartFXTest {

    public static void main(String[] args) {
        final File source = new File(Thread.currentThread().getContextClassLoader().getResource("iris.csv").getFile());
        final DataFrame iris = DataFrame.from(source);

        show("Sepal length vs width",
                "Sepal length", "Sepal width",
                scatter(iris, "sepal_length", "sepal_width", "petal_length")
                        .setMarker('*')
                        .setName("sepal: length v width")/*,
                chart -> {
                    chart.getPlot().getXAxis().setMajorTickSpacing(1);
                    ((NumericAxis) chart.getPlot().getXAxis()).setMinorTickSpacing(0.2);
                    chart.getPlot().getYAxis().setMajorTickSpacing(1);
                    ((NumericAxis) chart.getPlot().getYAxis()).setMinorTickSpacing(0.2);
                }*/
        );


    }

}
