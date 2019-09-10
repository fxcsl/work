package com.sinovatio.mapp.overwrite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.draw.IDrawFormat;
import com.bin.david.form.utils.DensityUtils;
import com.bin.david.form.utils.DrawUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class CustTextDrawFormat<T> implements IDrawFormat<T> {
    private Map<String,SoftReference<String[]>> valueMap; //避免产生大量对象
    private int nameMaxWidth;
    public CustTextDrawFormat() {
        valueMap = new HashMap<>();
    }
    public CustTextDrawFormat(int maxWidth) {
        nameMaxWidth = maxWidth;
        valueMap = new HashMap<>();
    }

    public CustTextDrawFormat(Context context, int maxWidth) {
        nameMaxWidth = DensityUtils.dp2px(context,maxWidth);
        valueMap = new HashMap<>();
    }

    @Override
    public int measureWidth(Column<T> column, int position, TableConfig config) {
        Paint paint = config.getPaint();
        config.getContentStyle().fillPaint(paint);
        int width =DrawUtils.getMultiTextWidth(paint,getSplitString(column.format(position)));
        if(width>nameMaxWidth){//此处进行内容最大值限制
            width = nameMaxWidth;
            column.setWidth(nameMaxWidth);
        }
        return width;
    }
    @Override
    public int measureHeight(Column<T> column, int position, TableConfig config) {
        Paint paint = config.getPaint();
        config.getContentStyle().fillPaint(paint);
        return DrawUtils.getMultiTextHeight(paint,getSplitString(column.format(position)));
    }
    @Override
    public void draw(Canvas c, Rect rect, CellInfo<T> cellInfo, TableConfig config) {
        Paint paint = config.getPaint();
        setTextPaint(config,cellInfo, paint);
        if(cellInfo.column.getTextAlign() !=null) {
            paint.setTextAlign(cellInfo.column.getTextAlign());
        }
        drawText(c, cellInfo.value, rect, paint);
    }
    protected void drawText(Canvas c, String value, Rect rect, Paint paint) {
        DrawUtils.drawMultiText(c,paint,rect,getSplitString(value));
    }
    public void setTextPaint(TableConfig config, CellInfo<T> cellInfo, Paint paint) {
        config.getContentStyle().fillPaint(paint);
        ICellBackgroundFormat<CellInfo> backgroundFormat = config.getContentCellBackgroundFormat();
        if(backgroundFormat!=null && backgroundFormat.getTextColor(cellInfo) != TableConfig.INVALID_COLOR){
            paint.setColor(backgroundFormat.getTextColor(cellInfo));
        }
        paint.setTextSize(paint.getTextSize()*config.getZoom());
    }
    protected String[] getSplitString(String val){
        String[] values = null;
        if(valueMap.get(val)!=null){
            values= valueMap.get(val).get();
        }
        if(values == null){
            values = val.split("\n");
            valueMap.put(val, new SoftReference<>(values));
        }
        return values;
    }

    public static void main(String[] args){
        String a = "\n";
        String b = "\\n";
        System.out.println(a.length());
        System.out.println(b.length());

    }
}
