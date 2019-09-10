package com.sinovatio.mapp.overwrite;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.title.ITitleDrawFormat;
import com.bin.david.form.utils.DrawUtils;

import java.util.List;
import java.util.Vector;

public class CustTitleDrawFormat implements ITitleDrawFormat {
    private boolean isDrawBg;
    private int nameMaxWidth;
    public CustTitleDrawFormat(int maxWidth){
        this.nameMaxWidth=maxWidth;
    }
    @Override
    public int measureWidth(Column column, TableConfig config) {
        Paint paint = config.getPaint();
        config.getColumnTitleStyle().fillPaint(paint);
        int width = DrawUtils.getMultiTextWidth(paint, StringFormat(column.getColumnName(),nameMaxWidth,(int)paint.getTextSize()));
        //if(column.getWidth()>0)
        //column.setWidth(column.getWidth());
        return width;
        //return (int) (paint.measureText(column.getColumnName()));
    }
    @Override
    public int measureHeight(TableConfig config) {
        Paint paint = config.getPaint();
        config.getColumnTitleStyle().fillPaint(paint);
        return DrawUtils.getTextHeight(config.getColumnTitleStyle(), config.getPaint());
    }
    @Override
    public void draw(Canvas c, Column column, Rect rect, TableConfig config) {
        Paint paint = config.getPaint();
        boolean isDrawBg = drawBackground(c, column, rect, config);
        config.getColumnTitleStyle().fillPaint(paint);
        ICellBackgroundFormat<Column> backgroundFormat = config.getColumnCellBackgroundFormat();
        paint.setTextSize(paint.getTextSize() * config.getZoom());
        if (isDrawBg && backgroundFormat.getTextColor(column) != TableConfig.INVALID_COLOR) {
            paint.setColor(backgroundFormat.getTextColor(column));
        }
        drawText(c, column, rect, paint);
    }
    private void drawText(Canvas c, Column column, Rect rect, Paint paint) {
        if (column.getTitleAlign() != null) { //如果列设置Align ，则使用列的Align
            paint.setTextAlign(column.getTitleAlign());
        }
        int maxWidth=180;
        List<Column> columns = column.getChildren();
        if(columns!=null&&columns.size()>1){//计算带有子列标题的表头最大宽度
            for (Column item:columns){
                String[] columnNames = StringFormat(item.getColumnName(),nameMaxWidth,(int)paint.getTextSize());
                int width = DrawUtils.getMultiTextWidth(paint, columnNames);
                maxWidth = maxWidth + width;
            }
            if(maxWidth<nameMaxWidth){
                maxWidth = nameMaxWidth;
            }
        }else{
            maxWidth = nameMaxWidth;
        }
        String[] columnNames = StringFormat(column.getColumnName(),maxWidth,(int)paint.getTextSize());
        //c.drawText(column.getColumnName(), DrawUtils.getTextCenterX(rect.left,rect.right,paint), DrawUtils.getTextCenterY((rect.bottom+rect.top)/2,paint) ,paint);
        if (columnNames.length > 1) {//换行是缩小字体避免超出
            paint.setTextSize(paint.getTextSize() - 4);
            //column.setMinHeight(20);
        } else if (columnNames.length > 2) {
            paint.setTextSize(paint.getTextSize() - 8);
        }
        DrawUtils.drawMultiText(c, paint, rect, columnNames);//表头换行
    }
    public boolean drawBackground(Canvas c, Column column, Rect rect, TableConfig config) {
        ICellBackgroundFormat<Column> backgroundFormat = config.getColumnCellBackgroundFormat();
        if (isDrawBg && backgroundFormat != null) {
            backgroundFormat.drawBackground(c, rect, column, config.getPaint());
            return true;
        }
        return false;
    }
    public boolean isDrawBg() {
        return isDrawBg;
    }
    public void setDrawBg(boolean drawBg) {
        isDrawBg = drawBg;
    }
    public String[] StringFormat(String text, int maxWidth, int fontSize) {
        String[] result = null;
        Vector<String> tempR = new Vector<String>();
        int lines = 0;
        int len = text.length();
        int index0 = 0;
        int index1 = 0;
        boolean wrap;
        while (true) {
            int widthes = 0;
            wrap = false;
            for (index0 = index1; index1 < len; index1++) {
                if (text.charAt(index1) == '\n') {
                    index1++;
                    wrap = true;
                    break;
                }
                widthes = fontSize + widthes;
                if (widthes > maxWidth) {
                    break;
                }
            }
            lines++;
            if (wrap) {
                tempR.addElement(text.substring(index0, index1 - 1));
            } else {
                tempR.addElement(text.substring(index0, index1));
            }
            if (index1 >= len) {
                break;
            }
        }
        result = new String[lines];
        tempR.copyInto(result);
        return result;
    }
}
