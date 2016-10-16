package ap.ky.stockapp;

import java.io.Serializable;

/**
 * Created by kylin25 on 2016/10/2.
 */

public class DBStruct implements Serializable {
    private static final long serialVersionUID = 1L;
    public int recid;
    public String date;
    public String company;
    public String compnum;
    public int stocks;
    public float stockprice;
    public int totalprice ;
    public int type;
}
