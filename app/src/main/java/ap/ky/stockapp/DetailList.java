package ap.ky.stockapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailList extends AppCompatActivity {
    ListView listView;
    TextView txtBenfit;
    Spinner spinner;
    Button btnModify;
    Button btnDelete;
    ArrayList<DBStruct> dbStruct;
    String TAG="DetailList";
    View itemrow;
    int select = 0;
    StockDB stockDB;
    void loadData(){
        Log.e(TAG,"loadData");
        dbStruct = stockDB.queryData();

        ListaAdapter listaAdapter = new ListaAdapter(this);
        listaAdapter.setData(dbStruct);

        listView.setAdapter(listaAdapter);
        int benfit = stockDB.queryBenfit();

        txtBenfit.setText(String.valueOf(benfit));
        ArrayList<String> nameList = stockDB.queryName();
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,nameList));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        stockDB = new StockDB(DetailList.this);
        listView = (ListView)findViewById(R.id.listView);
        txtBenfit = (TextView)findViewById(R.id.txtBenfit);
        spinner = (Spinner)findViewById(R.id.spinner);

        View l = LayoutInflater.from(this).inflate(R.layout.items,null);
        TextView textView2 = (TextView)l.findViewById(R.id.txt2);
        TextView textView3 = (TextView)l.findViewById(R.id.txt3);
        TextView textView5 = (TextView)l.findViewById(R.id.txt5);
        TextView textView6 = (TextView)l.findViewById(R.id.txt6);
        TextView textView7 = (TextView)l.findViewById(R.id.txt7);
        TextView textView8 = (TextView)l.findViewById(R.id.txt8);
        listView.addHeaderView(l);

        textView2.setText("date");
        textView3.setText("name");
        textView5.setText("stocks");
        textView6.setText("stock price");
        textView7.setText("total");
        textView8.setText("Type");



        listView.setOnItemClickListener(onItemClickListener);

        btnModify = (Button)findViewById(R.id.btnModify);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbStruct.size() > 0) {

                    DBStruct data = dbStruct.get(select);

                    //Log.e(TAG,"" + row.company);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", data);

                    Intent intent = new Intent(DetailList.this, ModifyData.class);
                    intent.putExtras(bundle);

                    startActivityForResult(intent,0);
                    

                }
            }
        });
        btnDelete = (Button)findViewById(R.id.btnDel);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbStruct.size() > 0) {

                    DBStruct data = dbStruct.get(select);
                    new AlertDialog.Builder(DetailList.this).setTitle("Confirm")
                            .setMessage("Delete ?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DBStruct data = dbStruct.get(select);
                                    stockDB.delData(data.recid);
                                    loadData();
                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .show();
                }
            }
        });
        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadData();
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(i>0) {
                DBStruct row = dbStruct.get(i - 1);
                Log.e(TAG, "" + row.recid + " " + row.company);
                select = i - 1;
                if (itemrow != null) {
                    itemrow.setBackgroundColor(Color.WHITE);
                }
                itemrow = view;
                view.setBackgroundColor(Color.RED);
            }
        }
    };
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
