package ap.ky.stockapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ap.ky.util.DateUtil;

public class MainActivity extends AppCompatActivity {
    TextView txtDate;
    Button btnOk;
    Button btnDetail;
    StockDB stockDB;
    //EditText edtNumber;
    EditText edtStock;
    //EditText edtCompany;
    EditText edtUnitPrice;
    EditText edtTotal;
    RadioButton radioSell;
    RadioButton radioBuy;
    RadioButton radioDiviend;
    private String TAG="StockMain";
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    AutoCompleteTextView actxtName;
    int year,month,day,hour,minute;
    private AutoCompleteTextView actxtNumber;

    void resetUI(){
        edtTotal.setText("");
        edtUnitPrice.setText("");
        edtTotal.setEnabled(true);
        edtUnitPrice.setEnabled(true);
    }
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

        //edtNumber = (EditText)findViewById(R.id.edtNumber);
        edtStock = (EditText)findViewById(R.id.edtStock);
        //edtCompany = (EditText)findViewById(R.id.editName);

        edtUnitPrice = (EditText)findViewById(R.id.edtunitprice);
        edtTotal = (EditText)findViewById(R.id.edttotal);

        stockDB = new StockDB(this);
        radioSell = (RadioButton)findViewById(R.id.sell);
        radioSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetUI();
            }
        });
        radioBuy = (RadioButton)findViewById(R.id.buy);
        radioBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetUI();
            }
        });
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
                calcTotal();
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
                calcTotal();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GregorianCalendar calendar = new GregorianCalendar();

                datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        year = i;
                        month = i1;
                        day = i2;
                        Log.e(TAG,"i " +i + " i1 " + i1 + " i2 " + i2);
                        timePickerDialog.show();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

                timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Log.e(TAG,"i " +i + " i1 " + i1 );
                        String date = DateUtil.getFullDate(year,month,day,i,i1);
                        txtDate.setText(date);
                    }
                },calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),false);

            }
        });
        actxtName = (AutoCompleteTextView)findViewById(R.id.actxtName);
        setacNameAdapter();

        actxtNumber = (AutoCompleteTextView)findViewById(R.id.actxtNumber);
        setacNumber();

    }
    void setacNameAdapter(){
        ArrayList<String>  namelist= stockDB.queryName();
        String[] name = new String[namelist.size()];
        namelist.toArray(name);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,name);

        actxtName.setAdapter(adapter);
    }
    void setacNumber(){
        ArrayList<String>  number= stockDB.queryCompNum();
        String[] numberarr = new String[number.size()];
        number.toArray(numberarr);

        ArrayAdapter<String> adapterNumber = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,numberarr);
        actxtNumber.setAdapter(adapterNumber);
    }
    void calcTotal(){
        String edtstock = edtStock.getText().toString();
        String edtunit = edtUnitPrice.getText().toString();
        if(!(edtstock.equals("") && edtunit.equals(""))) {
            int stock = Integer.parseInt(edtstock);
            double unit = Double.parseDouble(edtunit);
            edtTotal.setText(String.valueOf(stock * unit));
            Log.e(TAG,"total " + stock * unit);
        }
    }
    int type = 0;
    int total;
    int stocks;
    View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(actxtName.getText().toString().equals("")==false &&
                    actxtNumber.getText().toString().equals("")==false &&
                    edtStock.getText().toString().equals("")==false) {

                final String date = txtDate.getText().toString();
                final String compname = actxtName.getText().toString();
                final String compnumber = actxtNumber.getText().toString();
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
                    msg+= " Diviend_";
                }

                new AlertDialog.Builder(MainActivity.this).setTitle("Confirm")
                        .setMessage("input confirm :" +msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                stockDB.addData(date, compname, compnumber,stocks,unitprice,total, type);
                                //update
                                setacNameAdapter();
                                setacNumber();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e(TAG,"requestCode " + requestCode + " resultCode " + resultCode);
    }
}
