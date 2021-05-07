import net.mahdilamb.dataviz.figure.FigureViewer;
import net.mahdilamb.dataviz.fx.FXViewer;
import net.mahdilamb.dataviz.io.FigureExporter;

module net.mahdilamb.dataviz.fx {
    uses FigureViewer;
    uses FigureExporter;
    requires net.mahdilamb.dataviz;
    requires net.mahdilamb.colormap;
    requires net.mahdilamb.dataframe;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.controls;
    provides FigureViewer
            with FXViewer;

}