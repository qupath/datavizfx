package net.mahdilamb.charts;


import net.mahdilamb.charts.Chart;
import net.mahdilamb.charts.Plot;
import net.mahdilamb.charts.styles.Text;

public class ChartFX<L extends Plot> extends Chart<L> {

    @Override
    protected double getTextWidth(Text text) {
        //TODO
        return 0;
    }

    @Override
    protected double getTextHeight(Text text) {
        //TODO
        return 0;
    }

    @Override
    protected void draw() {

    }

}
