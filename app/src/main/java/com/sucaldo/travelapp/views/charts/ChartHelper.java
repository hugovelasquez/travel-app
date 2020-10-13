package com.sucaldo.travelapp.views.charts;

import android.content.Context;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.BubbleDataEntry;
import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Scatter;
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
import com.sucaldo.travelapp.model.Trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

public class ChartHelper {

    private DatabaseHelper myDB;
    private Context context;

    private final int AXIS_LABEL_FONT_SIZE = 15;
    private final int AXIS_TITLE_FONT_SIZE = 20;

    private final String[] COLOR_SCHEMA = new String[]{
            "#fa70b5", "#8e44ad", "#00c3ff", "#009175", "#34eb31", "#ffd000", "#ff0404", "#a8a8a8"
    };

    public ChartHelper(DatabaseHelper myDB, Context context) {
        this.myDB = myDB;
        this.context = context;
    }

    /*
     ********* BAR CHART **********************
     */

    public Cartesian initTop10PlacesChart(AnyChartView anyChartView, boolean fullscreen, List<DataEntry> data) {
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        Cartesian barChart = AnyChart.column();

        Column column = barChart.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        barChart.animation(true);

        if (!fullscreen) {
            barChart.title(context.getString(R.string.title_top_10_places));
        }

        barChart.yScale().minimum(0d);
        barChart.yScale().ticks().interval(2);

        barChart.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        barChart.tooltip().positionMode(TooltipPositionMode.POINT).fontSize(15);
        barChart.interactivity().hoverMode(HoverMode.BY_X);

        barChart.yAxis(0).title(context.getString(R.string.charts_top10_y_axis));

        if (fullscreen) {
            barChart.xAxis(0).title().fontSize(AXIS_TITLE_FONT_SIZE);
            barChart.yAxis(0).title().fontSize(AXIS_TITLE_FONT_SIZE);
            barChart.xAxis(0).staggerMode(true);
            barChart.xAxis(0).staggerLines(2);
            barChart.xAxis(0).labels().fontSize(AXIS_LABEL_FONT_SIZE);
            barChart.yAxis(0).labels().fontSize(AXIS_LABEL_FONT_SIZE);
        }

        anyChartView.setChart(barChart);
        return barChart;
    }

    public void updateChart(Cartesian barChart, List<DataEntry> data) {
        barChart.data(data);
    }

    /*
     ********* CLOUD CHART **********************
     */

    public TagCloud initCountriesCloudChart(AnyChartView anyChartView, boolean fullscreen, List<DataEntry> data) {
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        TagCloud tagCloud = AnyChart.tagCloud();

        if (!fullscreen) {
            tagCloud.title(context.getString(R.string.title_countries_cloud));
        }

        OrdinalColor ordinalColor = OrdinalColor.instantiate();
        ordinalColor.colors(COLOR_SCHEMA.clone());
        tagCloud.colorScale(ordinalColor);

        tagCloud.angles(new Double[]{-90d, 0d, 90d});

        if (fullscreen) {
            tagCloud.colorRange().labels().width(50);
            tagCloud.colorRange().enabled(true);
            tagCloud.colorRange().colorLineSize(15d);
        }

        tagCloud.tooltip().useHtml(true);
        tagCloud.tooltip().format(TAG_CLOUD_COUNTRIES_TOOLTIP);
        tagCloud.data(data);

        anyChartView.setChart(tagCloud);


        return tagCloud;
    }

    private final String TAG_CLOUD_COUNTRIES_TOOLTIP = "function() {return '" +
            "<p style=\"color: #d2d2d2; font-size: 15px\"> Trips:' + this.getData('value') + '</p>" +
            " <table style=\"color: #d2d2d2; font-size: 15px\">" +
            "' + this.getData('html') + '</table>';}";

    public void updateChart(TagCloud tagCloud, boolean countries, List<DataEntry> data) {
        if (countries) {
            tagCloud.tooltip().format(TAG_CLOUD_COUNTRIES_TOOLTIP);
        } else {
            tagCloud.tooltip().format("Trips: {%value}");
        }
        tagCloud.data(data);
    }

    public static class CustomCategoryValueDataEntry extends CategoryValueDataEntry {

        public CustomCategoryValueDataEntry(String x, String category, Integer value) {
            super(x, category, value);
        }

        public void setTripsInfo(List<Trip> trips) {
            StringBuilder html = new StringBuilder();
            List<Trip> selectedTrips;
            // A max of 15 trips can fit nicely into tooltip based on current Tablet size
            if (trips.size() < 15) {
                selectedTrips = trips;
            } else {
                selectedTrips = selectRandomTrips(trips);
            }
            for (Trip trip : selectedTrips) {
                html.append("<tr> <td>")
                        .append(trip.getFormattedStartDate())
                        .append("</td>")
                        .append("<td> <b>")
                        .append(trip.getToCity())
                        .append("</b> </td>")
                        .append("<td>")
                        .append(trip.getDescription())
                        .append("</td> </tr>");
            }
            setValue("html", html.toString());
        }
    }

    private static List<Trip> selectRandomTrips(List<Trip> trips) {
        java.util.Set<Trip> randomTrips = new HashSet<>();
        while (randomTrips.size() < 15) {
            Random r = new Random();
            int low = 0;
            int high = trips.size() - 1;
            int result = r.nextInt(high - low) + low;
            randomTrips.add(trips.get(result));
        }
        List<Trip> selectedTrips = new ArrayList<>(randomTrips);
        Collections.sort(selectedTrips);
        return selectedTrips;
    }


    /*
     ********* AREA CHART **********************
     */

    public void initKmsAreaChart(AnyChartView anyChartView, boolean fullscreen, List<DataEntry> data) {
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        Cartesian areaChart = AnyChart.area();

        if (!fullscreen) {
            areaChart.title(context.getString(R.string.title_kms_area_chart));
        }

        areaChart.animation(true);

        Crosshair crosshair = areaChart.crosshair();
        crosshair.enabled(true);
        crosshair.yLabel(0).enabled(true);

        areaChart.yScale().stackMode(ScaleStackMode.VALUE);

        Set set = Set.instantiate();
        set.data(data);

        for (int i = 0; i < DatabaseHelper.CONTINENTS.size(); i++) {
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

            areaChart.interactivity().hoverMode(HoverMode.BY_X);
            areaChart.xAxis(0).labels().fontSize(AXIS_LABEL_FONT_SIZE);
            areaChart.yAxis(0).title().fontSize(AXIS_TITLE_FONT_SIZE);
            areaChart.yAxis(0).labels().fontSize(AXIS_LABEL_FONT_SIZE);
        }

        areaChart.tooltip()
                .valuePostfix(context.getString(R.string.kms_area_chart_y_axis))
                .fontSize(15)
                .displayMode(TooltipDisplayMode.UNION);

        areaChart.xAxis(0).title(false);
        areaChart.xAxis(0).ticks(false);

        areaChart.yAxis(0).title(context.getString(R.string.kms_area_chart_y_axis));

        areaChart.yScale().ticks().interval(10000);

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

    public void initKmsBubbleChart(AnyChartView anyChartView, boolean fullscreen, List<DataEntry> data) {
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        Scatter bubble = AnyChart.bubble();

        bubble.animation(true);

        List<Integer> allYears = myDB.getAllYearsOfTrips();
        bubble.xScale().minimum(allYears.get(0) - 1);
        bubble.xScale().maximum(allYears.get(allYears.size() - 1) + 1);

        bubble.yAxis(0)
                .title(context.getString(R.string.kms_bubble_chart_y_axis));
        bubble.yGrid(true);

        bubble.bubble(data).name("Details").selected().fill("#31eb97", 0.5);
        bubble.padding(20d, 20d, 10d, 20d);

        if (!fullscreen) {
            bubble.title(context.getString(R.string.title_kms_bubble_chart));
            bubble.minBubbleSize(2d)
                    .maxBubbleSize(20d);
        }

        if (fullscreen) {
            bubble.minBubbleSize(10d)
                    .maxBubbleSize(50d);
            bubble.yAxis(0).labels().fontSize(AXIS_LABEL_FONT_SIZE);
            bubble.yAxis(0).title().fontSize(AXIS_TITLE_FONT_SIZE);
            bubble.xAxis(0).labels().fontSize(AXIS_LABEL_FONT_SIZE);
        }

        bubble.tooltip()
                .useHtml(true)
                .fontColor("#fff")
                .format("function() {\n" +
                        "        return '<div style=\"width: 175px; font-size: 15px\"> " +
                        "           Year: <span style=\"color: #d2d2d2; font-size: 15px\">' +\n" +
                        "          this.getData('x') + '</span></strong><br/>' +\n" +
                        "          'Trips: <span style=\"color: #d2d2d2; font-size: 15px\">' +\n" +
                        "          this.getData('value') + '</span></strong><br/>' +\n" +
                        "          'Distance: <span style=\"color: #d2d2d2; font-size: 15px\">' +\n" +
                        "          this.getData('size') + ' kms.</span></strong><br/>' +\n" +
                        "          'Countries: <span style=\"color: #d2d2d2; font-size: 15px\">' +\n" +
                        "          this.getData('countries') + '</span></strong><br/> </div>';\n" +
                        "      }");


        anyChartView.setChart(bubble);
    }

    public static class CustomBubbleDataEntry extends BubbleDataEntry {

        public CustomBubbleDataEntry(Integer x, Integer value, Integer size, List<String> countries) {
            super(x, value, size);
            StringJoiner joiner = new StringJoiner(", ");
            for (String country : countries) {
                joiner.add(country);
            }
            setValue("countries", joiner.toString());
        }
    }
}
