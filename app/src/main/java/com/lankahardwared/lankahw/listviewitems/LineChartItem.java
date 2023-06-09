package com.lankahardwared.lankahw.listviewitems;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.github.mikephil.charting.utils.YLabels;
import com.lankahardwared.lankahw.R;


public class LineChartItem extends ChartItem {

    private Typeface mTf;

    public LineChartItem(ChartData cd, Context c) {
        super(cd);
        mTf = Typeface.createFromAsset(c.getAssets(), "fonts/Exo2_Regular.otf");
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_linechart, null);
            holder.chart = (LineChart) convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        // holder.chart.setValueTypeface(mTf);
        holder.chart.setDrawYValues(false);
        holder.chart.setDescription("");
        holder.chart.setDrawVerticalGrid(false);
        holder.chart.setDrawGridBackground(false);

        XLabels xl = holder.chart.getXLabels();
        xl.setCenterXLabelText(true);
        xl.setTextSize(14f);
        xl.setPosition(XLabelPosition.BOTTOM);
        xl.setTypeface(mTf);

        YLabels yl = holder.chart.getYLabels();
        yl.setTypeface(mTf);
        yl.setLabelCount(5);
        yl.setTextSize(14f);

        // set data
        holder.chart.setData((LineData) mChartData);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart.animateX(1000);

        return convertView;
    }

    private static class ViewHolder {
        LineChart chart;
    }
}
