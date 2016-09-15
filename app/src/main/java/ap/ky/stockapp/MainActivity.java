package ap.ky.stockapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import ap.ky.util.DateUtil;

public class MainActivity extends AppCompatActivity {
    TextView txtDate;
    Button btnOk;
    Button btnDetail;
    StockDB stockDB;
    EditText edtNumber;
    EditText edtStock;
    EditText edtCompany;
    EditText edtUnitPrice;
    EditText edtTotal;
    RadioButton radioSell;
    RadioButton radioBuy;
    RadioButton radioDiviend;
    private String TAG="StockMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDate = (TextView)findViewById(R.id.txtDate);
        txtDate.setText(DateUtil.getFullDateTime());

        btnOk = (Button)findViewById(R.id.btnOk);
        btnOk.setOnClickListener(btnClick);

        btnDetail = (Button)findViewById(R.id.btnDetail);
        btnDetail.setOnClickListener(btnDetial);

        edtNumber = (EditText)findViewById(R.id.edtNumber);
        edtStock = (EditText)findViewById(R.id.edtStock);
        edtCompany = (EditText)findViewById(R.id.editName);

        edtUnitPrice = (EditText)findViewById(R.id.edtunitprice);
        edtTotal = (EditText)findViewById(R.id.edttotal);

        stockDB = new StockDB(this);
        radioSell = (RadioButton)findViewById(R.id.sell);
        radioBuy = (RadioButton)findViewById(R.id.buy);
        radioDiviend = (RadioButton)findViewById(R.id.rdDiviend);
        radioDiviend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtTotal.setText("0");
                edtUnitPrice.setText("0");
                edtTotal.setEnabled(false);
                edtUnitPrice.setEnabled(false);
            }
        });

        edtStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String txt = edtStock.getText().toString();
                Log.e(TAG, " " + txt);
                if (txt.equals("") == true) {
                    edtStock.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtUnitPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String txt = edtUnitPrice.getText().toString();
                Log.e(TAG, " " + txt);
                if (txt.equals("") == true) {
                    edtStock.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    int type = 0;
    int total;
    int stocks;
    View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(edtCompany.getText().toString().equals("")==false &&
                    edtNumber.getText().toString().equals("")==false &&
                    edtStock.getText().toString().equals("")==false) {

                final String date = txtDate.getText().toString();
                final String compname = edtCompany.getText().toString();
                final String compnumber = edtNumber.getText().toString();
                stocks = Integer.parseInt(edtStock.getText().toString());
                final float unitprice = Float.parseFloat(edtUnitPrice.getText().toString());
                total = Integer.parseInt(edtTotal.getText().toString());
                String msg = String.format("%s %s %s %d %f %d",date,compname,compnumber,stocks,unitprice,total);
                if(radioBuy.isChecked() == true){
                    total = -total;
                    type = stockDB.TYPE_BUY;
                    msg+= " BUY";
                }else if(radioSell.isChecked() == true){
                    type = stockDB.TYPE_SELL;
                    stocks = -stocks;
                    msg+= " Sell";
                }else{
                    type = StockDB.TYPE_DIVIEND;
                    msg+= " Diviend";
                }

                new AlertDialog.Builder(MainActivity.this).setTitle("Confirm")
                        .setMessage("input confirm :" +msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                stockDB.addData(date, compname, compnumber,stocks,unitprice,total, type);
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();

            }else{
                new AlertDialog.Builder(MainActivity.this).setTitle("Error")
                        .setMessage("input blank")
                        .setPositiveButton("Ok",null)
                        .show();
            }

        }
    };
    View.OnClickListener btnDetial = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this,DetailList.class));
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
