package net.mahdilamb.dataviz.fx;

import net.mahdilamb.dataviz.figure.FigureBase;
import net.mahdilamb.dataviz.figure.FigureViewer;

public class FXViewer implements FigureViewer {
    @Override
    public int priority() {
        return 10;
    }

    @Override
    public void show(FigureBase figure) {
        FXLauncher.launch(figure);
    }
}
