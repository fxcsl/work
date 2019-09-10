package com.sinovatio.mapp.overwrite;

import android.content.Context;

import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.ArrayTableData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyArrayTableData<T> extends ArrayTableData<T> {

    public MyArrayTableData(String tableName, List<T> t, List<Column<T>> columns) {
        super(tableName, t, columns);
    }


    public static<T> ArrayTableData<T> create(String tableName, String[] titleNames, T[][] data,
                                              int [] width, Context context){
        List<Column<T>> columns = new ArrayList<>();
        for(int i = 0;i <data.length;i++){
            T[] dataArray = data[i];
            Column<T> column = new Column<>(titleNames == null?"":titleNames[i], null,
                    new CustTextDrawFormat(context,width[i]));
            column.setDatas(Arrays.asList(dataArray));
            columns.add(column);
        }
        ArrayList<T> arrayList = new ArrayList<>(Arrays.asList(data[0]));
        ArrayTableData<T> tableData =  new MyArrayTableData<>(tableName,arrayList,columns);
        tableData.setData(data);
        return tableData;
    }



}
