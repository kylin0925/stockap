package ap.ky.stockapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import java.util.StringTokenizer;

import ap.ky.util.DateUtil;

public class ModifyData extends AppCompatActivity {

    private static final String TAG = "Modify";
    TextView txtDate;
    Button btnOk;
    Button btnCancel;
    EditText edtStock;

    EditText edtUnitPrice;
    EditText edtTotal;
    RadioButton radioSell;
    RadioButton radioBuy;
    RadioButton radioDiviend;
    AutoCompleteTextView actxtName;
    AutoCompleteTextView actxtNumber;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    int year,month,day,hour,minute;
    StockDB stockDB;
    DBStruct data;
    void resetUI(){
        edtTotal.setText("");
        edtUnitPrice.setText("");
        edtTotal.setEnabled(true);
        edtUnitPrice.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_data);

        btnOk = (Button)findViewById(R.id.btnOk);
        btnOk.setOnClickListener(btnClick);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyData.this.setResult(0);
                finish();
            }
        });


        txtDate = (TextView)findViewById(R.id.txtDate);
        txtDate.setText(DateUtil.getFullDateTime());

        edtStock = (EditText)findViewById(R.id.edtStock);
        edtUnitPrice = (EditText)findViewById(R.id.edtunitprice);
        edtTotal = (EditText)findViewById(R.id.edttotal);

        radioSell = (RadioButton)findViewById(R.id.sell);
        radioBuy = (RadioButton)findViewById(R.id.buy);
        radioDiviend = (RadioButton)findViewById(R.id.rdDiviend);


        radioSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetUI();
            }
        });
        radioBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetUI();
            }
        });
        radioDiviend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtTotal.setText("0");
                edtUnitPrice.setText("0");
                edtTotal.setEnabled(false);
                edtUnitPrice.setEnabled(false);
            }
        });

        actxtName = (AutoCompleteTextView)findViewById(R.id.actxtName);
        actxtNumber = (AutoCompleteTextView)findViewById(R.id.actxtNumber);

        stockDB = new StockDB(this);
        setacNameAdapter();
        setacNumber();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        data = (DBStruct)bundle.getSerializable("data");

        Log.e(TAG,"" + data.company );

        txtDate.setText(data.date);
        edtStock.setText(String.valueOf(data.stocks));
        edtUnitPrice.setText(String.valueOf(data.stockprice));
        actxtName.setText(data.company);
        actxtNumber.setText(data.compnum);
        edtTotal.setText(String.valueOf(data.totalprice));

        if(data.type==StockDB.TYPE_SELL){
            radioSell.toggle();
        }else if(data.type == StockDB.TYPE_BUY){
            radioBuy.toggle();
        }else if(data.type == StockDB.TYPE_DIVIEND){
            radioDiviend.toggle();
        }

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GregorianCalendar calendar = new GregorianCalendar();

                datePickerDialog = new DatePickerDialog(ModifyData.this, new DatePickerDialog.OnDateSetListener() {
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

                timePickerDialog = new TimePickerDialog(ModifyData.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Log.e(TAG,"i " +i + " i1 " + i1 );
                        String date = DateUtil.getFullDate(year,month,day,i,i1);
                        txtDate.setText(date);
                    }
                },calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),false);

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
    }
    void setacNameAdapter(){
        ArrayList<String> namelist= stockDB.queryName();
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
            edtTotal.setText(String.valueOf((int)(stock * unit)));
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

                new AlertDialog.Builder(ModifyData.this).setTitle("Confirm")
                        .setMessage("Update confirm :" +msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //update
                                stockDB.updateData(date, compname, compnumber,stocks,unitprice,total,
                                        type,data.recid);
                                setacNameAdapter();
                                setacNumber();
                                ModifyData.this.setResult(0);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();

            }else{
                new AlertDialog.Builder(ModifyData.this).setTitle("Error")
                        .setMessage("input blank")
                        .setPositiveButton("Ok",null)
                        .show();
            }

        }
    };
}
