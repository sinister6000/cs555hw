package imageprocessor.model;

import javafx.scene.chart.XYChart;

public class GrayHistogram {

    private XYChart.Series<String, Number> seriesGray;

    public GrayHistogram(GrayImage gimg) {

        long[] grayPixelCounts = gimg.getHistogram();

        seriesGray = new XYChart.Series<>();
        seriesGray.setName("seriesGray");

        // load grayPixelCounts[] into seriesGray
        for (int i = 0; i < grayPixelCounts.length; ++i) {
            seriesGray.getData().add(new XYChart.Data<>(String.valueOf(i), grayPixelCounts[i]));
        }
    }

    public XYChart.Series<String, Number> getSeriesGray() {
        return seriesGray;
    }
}
