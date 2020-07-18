package com.sucaldo.travelapp.views.charts;

import android.content.Context;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.R;

import java.util.List;

public class ChartHelper {

    private DatabaseHelper myDB;
    private Context context;

    public ChartHelper(DatabaseHelper myDB, Context context) {
        this.myDB = myDB;
        this.context = context;
    }

    public void initTop10CitiesChart(AnyChartView anyChartView, boolean fullscreen) {

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = myDB.getTop10Cities();

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
            cartesian.title(context.getString(R.string.top_10_cities));
        }

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title(context.getString(R.string.charts_top10_x_axis));
        cartesian.yAxis(0).title(context.getString(R.string.charts_top10_y_axis));

        if (fullscreen) {
            cartesian.xAxis(0).title().fontSize(20);
            cartesian.yAxis(0).title().fontSize(20);
            cartesian.xAxis(0).staggerMode(true);
            cartesian.xAxis(0).staggerLines(2);
            cartesian.xAxis(0).labels().fontSize(15);
            cartesian.yAxis(0).labels().fontSize(15);
        }

        anyChartView.setChart(cartesian);
    }
}
