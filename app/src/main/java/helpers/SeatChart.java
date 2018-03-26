package helpers;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;


public class SeatChart implements OnChartValueSelectedListener {

    private LineChart mChart;

    public SeatChart(LineChart chart) {
        mChart = chart;
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.setNoDataText("You need to provide data for the chart.");

//        // enable touch gestures
//        mChart.setTouchEnabled(true);
//
//        // enable scaling and dragging
//        mChart.setDragEnabled(true);
//        mChart.setScaleEnabled(true);
//        mChart.setDrawGridBackground(false);
//
//        // if disabled, scaling can be done on x- and y-axis separately
//        mChart.setPinchZoom(true);

        Description description = new Description();
        description.setText("Seat movement of all athletes");
        mChart.setDescription(description);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setBorderColor(Color.rgb(67,164,34));


        LineData data = new LineData();
//        List<LineDataSet> dataSets = createSets();
//        for (ILineDataSet s : dataSets) {
//            data.addDataSet(s);
//        }

        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);
//        mChart.invalidate();  // ?

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(Typeface.MONOSPACE);
        l.setTextColor(Color.rgb(67, 164, 34));

        XAxis xl = mChart.getXAxis();
//        xl.setTypeface(Typeface.MONOSPACE);
//        xl.setTextColor(Color.rgb(67, 164, 34));
//        xl.setDrawGridLines(false);
//        xl.setAvoidFirstLastClipping(true);
//        xl.setEnabled(true);
        xl.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(Typeface.MONOSPACE);
        leftAxis.setTextColor(Color.rgb(67, 164, 34));

        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void setChart(LineChart chart){ this.mChart = chart; }

    public void addEntry(float value, String identifier) {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByLabel(identifier, false);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                List<LineDataSet> sets = createSets();
                for (LineDataSet s : sets) {
                    data.addDataSet(s);
                }

                set = data.getDataSetByLabel(identifier, false);
            }

            if (set != null) {
                set.addEntry(new Entry(set.getEntryCount(), value));
//            data.addEntry(new Entry(set.getEntryCount(),value),0);
                Log.w("SeatGraphNewEntry", set.getEntryForIndex(set.getEntryCount() - 1).toString());

                data.notifyDataChanged();

                // let the chart know it's data has changed
                mChart.notifyDataSetChanged();

                // limit the number of visible entries
                mChart.setVisibleXRangeMaximum(10);
                // mChart.setVisibleYRange(30, AxisDependency.LEFT);

                // move to the latest entry
                mChart.moveViewTo(set.getEntryCount() - 1, data.getYMax(), YAxis.AxisDependency.LEFT);
                // moveViewTo automatically refreshes the chart (calls invalidate())
            }
        }
    }

    private List<LineDataSet> createSets() {
        LineDataSet seatOne = new LineDataSet(null, "Seat1");
        seatOne.setAxisDependency(YAxis.AxisDependency.LEFT);
        seatOne.setColor(Color.rgb(255, 0, 0));
        //set.setCircleColor(Color.WHITE);
        seatOne.setLineWidth(2f);
        //set.setCircleRadius(4f);
//        seatOne.setFillAlpha(65);
//        seatOne.setFillColor(Color.rgb(255, 0, 0));
        seatOne.setHighLightColor(Color.rgb(255, 0, 0));
        seatOne.setValueTextColor(Color.rgb(255, 0, 0));
        seatOne.setValueTextSize(9f);
        seatOne.setDrawValues(false);
        seatOne.setDrawCircles(false);

        LineDataSet seatTwo = new LineDataSet(null, "Seat2");
        seatTwo.setAxisDependency(YAxis.AxisDependency.LEFT);
        seatTwo.setColor(Color.rgb(90, 164, 34));
        //set.setCircleColor(Color.WHITE);
        seatTwo.setLineWidth(2f);
        //set.setCircleRadius(4f);
//        seatTwo.setFillAlpha(65);
//        seatTwo.setFillColor(Color.rgb(90, 164, 34));
        seatTwo.setHighLightColor(Color.rgb(90, 164, 34));
        seatTwo.setValueTextColor(Color.rgb(90, 164, 34));
        seatTwo.setValueTextSize(9f);
        seatTwo.setDrawValues(false);
        seatTwo.setDrawCircles(false);

        // Apparantly the charting lib assumes that there is a value in index 0 of each data set?
        seatOne.addEntry(new Entry(0, 100));
        seatTwo.addEntry(new Entry(0, 100));

        List<LineDataSet> sets = new ArrayList();
        sets.add(seatOne);
        sets.add(seatTwo);

        return sets;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected(){
        Log.i("Nothing selected", "Nothing selected.");
    }

}
