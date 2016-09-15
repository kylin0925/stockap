package ap.ky.stockapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kylin25 on 2016/8/28.
 */
public class ListaAdapter extends BaseAdapter {
    ArrayList<StockDB.DBStruct> arrayList = new ArrayList<StockDB.DBStruct>();
    Context context;
    public ListaAdapter(Context context){
        this.context = context;
    }
    public  void setData( ArrayList<StockDB.DBStruct> arrayList){
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        v = LayoutInflater.from(this.context).inflate(R.layout.items,null);
        TextView t1 = (TextView)v.findViewById(R.id.txt1);
        TextView t2 = (TextView)v.findViewById(R.id.txt2);
        TextView t3 = (TextView)v.findViewById(R.id.txt3);
        TextView t4 = (TextView)v.findViewById(R.id.txt4);
        TextView t5 = (TextView)v.findViewById(R.id.txt5);
        TextView t6 = (TextView)v.findViewById(R.id.txt6);
        TextView t7 = (TextView)v.findViewById(R.id.txt7);
        TextView t8 = (TextView)v.findViewById(R.id.txt8);

        t1.setText(Integer.toString(arrayList.get(i).recid));
        t1.setVisibility(View.GONE);
        t2.setText(arrayList.get(i).date);
        t3.setText(arrayList.get(i).company);
        t4.setText(arrayList.get(i).compnum);
        t4.setVisibility(View.GONE);
        t5.setText(Integer.toString(arrayList.get(i).stocks));
        t6.setText(Float.toString(arrayList.get(i).stockprice));
        t7.setText(Integer.toString(arrayList.get(i).totalprice));

        if(arrayList.get(i).type == StockDB.TYPE_BUY)
            t8.setText("Buy");
        else if(arrayList.get(i).type == StockDB.TYPE_SELL)
            t8.setText("Sell");
        else
            t8.setText(Integer.toString(arrayList.get(i).type));
        //t8.setText("hello");
        return v;
    }
}
