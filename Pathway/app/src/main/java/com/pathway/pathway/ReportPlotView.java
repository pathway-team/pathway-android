package com.pathway.pathway;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import org.json.JSONException;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportPlotView extends AppCompatActivity {
    XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_plot_view);
        plot = (XYPlot) findViewById(R.id.plot);
        plot.setTitle("Speed plot");

        List<Integer> timex = getIntent().getExtras().getIntegerArrayList("timex");
        List<Double> speedy = new ArrayList<>();
        double[] temp = getIntent().getExtras().getDoubleArray("speedy");

        for (double d : temp) {
            speedy.add(d);
        }


        XYSeries series1 = new SimpleXYSeries((timex), (speedy), "Recent");

        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.GRAY, Color.GREEN, null, null);

        series1Format.setInterpolationParams(new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        Paint p = series1Format.getLinePaint();

        p.setStrokeWidth(15);
        series1Format.setLinePaint(p);

        //set min/max x-axis values and step interval
        plot.setDomainBoundaries(0, 30, BoundaryMode.FIXED);
        plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 3);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                // obj contains the raw Number value representing the position of the label being drawn.
                // customize the labeling however you want here:
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(i + " sec");
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                // unused
                return null;
            }
        });

        //set min/max y-axis values and step interval
        plot.setRangeBoundaries(0, 3, BoundaryMode.FIXED);
        plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 1);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                // obj contains the raw Number value representing the position of the label being drawn.
                // customize the labeling however you want here:
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(i + "");
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                // unused
                return null;
            }
        });

        //setup plot pan functionality for side-scrolling
        plot.getOuterLimits().set(0, 1000, 0, 25);
        PanZoom.attach(plot, PanZoom.Pan.HORIZONTAL, null);

        //Set Labels for axis
        plot.setDomainLabel("Time(sec)");
        plot.setRangeLabel("Speed(mph)");

        //set Title for graph
        plot.setTitle("Speed Graph");

        plot.addSeries(series1, series1Format);

    }
}
