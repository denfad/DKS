package ru.denfad.dks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;


import sun.bob.mcalendarview.CellConfig;
import sun.bob.mcalendarview.views.BaseCellView;
import sun.bob.mcalendarview.vo.DayData;

class DateCellView extends BaseCellView {
    public TextView textView;
    private AbsListView.LayoutParams matchParentParams;

    public DateCellView(Context context) {
        super(context);
        initLayout();
    }

    public DateCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    private void initLayout(){
        matchParentParams = new AbsListView.LayoutParams((int) CellConfig.cellWidth, (int) CellConfig.cellHeight);
        this.setLayoutParams(matchParentParams);
        this.setOrientation(VERTICAL);
        textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0));
        this.addView(textView);
    }

    @Override
    public void setDisplayText(DayData day) {
        textView.setText(day.getText());
    }

}