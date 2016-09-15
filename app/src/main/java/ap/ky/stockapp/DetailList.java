package ap.ky.stockapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailList extends AppCompatActivity {
    ListView listView;
    TextView txtBenfit;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        StockDB stockDB = new StockDB(DetailList.this);
        ArrayList<StockDB.DBStruct> dbStruct = stockDB.queryData();

        listView = (ListView)findViewById(R.id.listView);
        ListaAdapter listaAdapter = new ListaAdapter(this);
        listaAdapter.setData(dbStruct);
        View l = LayoutInflater.from(this).inflate(R.layout.items,null);
        TextView textView2 = (TextView)l.findViewById(R.id.txt2);
        TextView textView3 = (TextView)l.findViewById(R.id.txt3);
        TextView textView5 = (TextView)l.findViewById(R.id.txt5);
        TextView textView6 = (TextView)l.findViewById(R.id.txt6);
        TextView textView7 = (TextView)l.findViewById(R.id.txt7);
        TextView textView8 = (TextView)l.findViewById(R.id.txt8);

        textView2.setText("date");
        textView3.setText("name");
        textView5.setText("stocks");
        textView6.setText("stock price");
        textView7.setText("total");
        textView8.setText("Type");



        listView.addHeaderView(l);
        listView.setAdapter(listaAdapter);

        int benfit = stockDB.queryBenfit();
        txtBenfit = (TextView)findViewById(R.id.txtBenfit);
        txtBenfit.setText(String.valueOf(benfit));

        ArrayList<String> nameList = stockDB.queryName();
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,nameList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
