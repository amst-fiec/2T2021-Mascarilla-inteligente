package com.example.s_mask;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class TableDynamic {
    //Variables
    private final TableLayout tableLayout;
    private final Context context;
    private ArrayList<String[]>data;
    private TableRow tableRow;
    private TextView txtCell;
    private int indexC;
    private int indexR;
    int color;

    public TableDynamic(TableLayout tableLayout, Context context) {
        this.tableLayout = tableLayout;
        this.context = context;
    }


    public void addData(ArrayList<String[]>data){
        this.data = data;
        createDataTable();
    }

    private void newRow(){
        tableRow = new TableRow(context);
    }

    private void newCell(){
        txtCell = new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(16);
        txtCell.setTypeface(Typeface.DEFAULT_BOLD);
        txtCell.setPadding(1,30,1,30);
    }

    //Creacion de filas y columnas
    private void createDataTable(){
        String info;
        for(indexR = 1 ; indexR <= data.size() ; indexR++){
            newRow();
            for(indexC = 0 ; indexC < 3 ; indexC++){
                newCell();
                String[] row = data.get(indexR - 1);
                info = (indexC < row.length)?row[indexC]:"";
                txtCell.setText(info);
                tableRow.addView(txtCell, newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
    }

    private TableRow getRow(int index){
        return (TableRow) tableLayout.getChildAt(index);
    }

    private TextView getCell(int rowIndex, int columnIndex){
        tableRow = getRow(rowIndex);
        return (TextView) tableRow.getChildAt(columnIndex);
    }

    //Color de linea entre celdas
    public void lineColor(int color){
        indexR = 1;
        while(indexR < data.size()+1){
            getRow(indexR++).setBackgroundColor(color);
        }
    }

    //Coloreo de celdas
    public void backgroundData(int color){
        for(indexR = 1 ; indexR <= data.size() ; indexR++){
            for(indexC = 0 ; indexC < 3 ; indexC++){
                txtCell = getCell(indexR, indexC);
                txtCell.setBackgroundColor(color);
            }
        }
        this.color = color;
    }

    //Parametros de la tabla
    private TableRow.LayoutParams newTableRowParams(){
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(2, 2, 2, 2);
        params.weight = 1;
        return params;
    }
}
