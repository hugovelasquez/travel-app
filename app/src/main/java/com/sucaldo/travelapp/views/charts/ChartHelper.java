package com.sucaldo.travelapp.views.charts;

import android.content.Context;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.BubbleDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.TagCloud;
import com.anychart.core.cartesian.series.Area;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.ui.Crosshair;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.scales.OrdinalColor;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.R;

import java.util.Arrays;
import java.util.List;

public class ChartHelper {

    private DatabaseHelper myDB;
    private Context context;

    private int axisLabelFontSize = 15;
    private int axisTitleFontSize = 20;

    private String[] colorSchema = new String[] {
        "#fa70b5", "#8e44ad", "#00c3ff", "#009175", "#34eb31", "#ffd000", "#ff0404", "#a8a8a8"
    };

    public ChartHelper(DatabaseHelper myDB, Context context) {
        this.myDB = myDB;
        this.context = context;
    }

    /*
     ********* BAR CHART **********************
     */

    public void initTop10PlacesChart(AnyChartView anyChartView, boolean fullscreen) {
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = myDB.getTop10Places();

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);

        if (!fullscreen) {
            cartesian.title(context.getString(R.string.top_10_places));
        }

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title(context.getString(R.string.charts_top10_x_axis));
        cartesian.yAxis(0).title(context.getString(R.string.charts_top10_y_axis));

        if (fullscreen) {
            cartesian.xAxis(0).title().fontSize(axisTitleFontSize);
            cartesian.yAxis(0).title().fontSize(axisTitleFontSize);
            cartesian.xAxis(0).staggerMode(true);
            cartesian.xAxis(0).staggerLines(2);
            cartesian.xAxis(0).labels().fontSize(axisLabelFontSize);
            cartesian.yAxis(0).labels().fontSize(axisLabelFontSize);
        }

        anyChartView.setChart(cartesian);
    }

    /*
     ********* CLOUD CHART **********************
     */

    public void initCountriesCloudChart(AnyChartView anyChartView, boolean fullscreen) {
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        TagCloud tagCloud = AnyChart.tagCloud();

        if (!fullscreen) {
            tagCloud.title(context.getString(R.string.countries_cloud));
        }

        OrdinalColor ordinalColor = OrdinalColor.instantiate();
        ordinalColor.colors(colorSchema.clone());
        tagCloud.colorScale(ordinalColor);

        tagCloud.angles(new Double[] {-90d, 0d, 90d});

        if (fullscreen) {
            tagCloud.colorRange().labels().width(50);
            tagCloud.colorRange().enabled(true);
            tagCloud.colorRange().colorLineSize(15d);

        }

        List<DataEntry> data = myDB.getVisitedCountries();

        tagCloud.data(data);
        tagCloud.tooltip().format("Trips: {%value}");

        anyChartView.setChart(tagCloud);
    }

    /*
     ********* AREA CHART **********************
     */

    public void initKmsAreaChart (AnyChartView anyChartView, boolean fullscreen) {
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        Cartesian areaChart = AnyChart.area();

        if (!fullscreen){
            areaChart.title(context.getString(R.string.kms_area_chart));
        }

        areaChart.animation(true);

        Crosshair crosshair = areaChart.crosshair();
        crosshair.enabled(true);
        crosshair.yLabel(0).enabled(true);

        areaChart.yScale().stackMode(ScaleStackMode.VALUE);

        List<DataEntry> seriesData = myDB.getKmsPerContinentPerYear();

        Set set = Set.instantiate();
        set.data(seriesData);

        for(int i = 0; i < DatabaseHelper.CONTINENTS.size(); i++) {
            int valuePostfix = i + 1;
            Mapping seriesDataMap = set.mapAs("{ x: 'x', value: '" + "value" + valuePostfix + "' }");

            Area series = areaChart.area(seriesDataMap);
            series.name(DatabaseHelper.CONTINENTS.get(i));
            series.stroke("3 #fff");
            series.hovered().stroke("3 #fff");
            series.hovered().markers().enabled(true);
            series.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d)
                    .stroke("1.5 #fff");
            series.markers().zIndex(100d);
        }

        if (fullscreen) {
            areaChart.legend().enabled(true);
            areaChart.legend().fontSize(13d);
            areaChart.legend().padding(0d, 0d, 20d, 0d);
        }

        areaChart.xAxis(0).title(false);
        areaChart.xAxis(0).ticks(false);
        areaChart.xAxis(0).labels().fontSize(axisLabelFontSize);

        areaChart.yAxis(0).title(context.getString(R.string.kms_area_chart_y_axis));
        areaChart.yAxis(0).title().fontSize(axisTitleFontSize);
        areaChart.yAxis(0).labels().fontSize(axisLabelFontSize);
        areaChart.yScale().ticks().interval(10000);

        areaChart.interactivity().hoverMode(HoverMode.BY_X);
        areaChart.tooltip()
                .valuePostfix(context.getString(R.string.kms_area_chart_y_axis))
                .displayMode(TooltipDisplayMode.UNION);

        anyChartView.setChart(areaChart);
    }

    public static class CustomDataEntry extends ValueDataEntry {
        public CustomDataEntry(String x, List<Integer> values) {
            super(x, 0);
            int i = 1;
            for (Integer value : values) {
                String key = "value" + i++;
                setValue(key, value);
            }
        }
    }

    /*
     ********* BUBBLE CHART **********************
     */

    //TODO copy code for bubble chart here

    public static class CustomBubbleDataEntry extends BubbleDataEntry {

        CustomBubbleDataEntry(Integer training, Integer x, Integer value, String data, Integer size) {
            super(x, value, size);
            setValue("training", training);
            setValue("data", data);
        }
    }
}
