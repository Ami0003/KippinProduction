package com.kippinretail;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

abstract public class AbstractListActivity extends SuperActivity {
    EditText txtSearch;
    ListView list_data;
    LinearLayout layout_nonKippin;
    RelativeLayout layout_txtMercahntName;
    RelativeLayout layout_container_search, layout_dialog;
    Button btn_upload;
    TextView txtMerchantName;
    ImageView iv_nonKippin;
    TextView txtNonKippin, txt_share, txt_unshare;
    View undline_nonKippin;


    ImageView iv_delete, iv_barcode;
    TextView txt_merchantName, txt_barcode;
    View layout_cardDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract_list);
        initUI();
    }

    abstract public void updateToolBar();

    abstract public void updateUI();

    abstract public void addListener();

    public void initUI() {
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        txt_share = (TextView) findViewById(R.id.txt_share);
        txt_unshare = (TextView) findViewById(R.id.txt_unshare);
        list_data = (ListView) findViewById(R.id.list_data);
        layout_nonKippin = (LinearLayout) findViewById(R.id.layout_nonKippin);
        layout_txtMercahntName = (RelativeLayout) findViewById(R.id.layout_txtMercahntName);
        layout_container_search = (RelativeLayout) findViewById(R.id.layout_container_search);
        layout_dialog = (RelativeLayout) findViewById(R.id.layout_dialog);
        txtNonKippin = (TextView) findViewById(R.id.txtNonKippin);
        txtMerchantName = (TextView) findViewById(R.id.txtMerchantName);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        iv_nonKippin = (ImageView) findViewById(R.id.iv_nonKippin);
        undline_nonKippin = findViewById(R.id.undline_nonKippin);
        btn_upload.setBackground(null);
        btn_upload.setBackgroundResource(R.drawable.x_trade_points_red);
//     variable for carddetail
        txt_merchantName = (TextView) findViewById(R.id.txt_merchantName);
        txt_barcode = (TextView) findViewById(R.id.txt_barcode);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_barcode = (ImageView) findViewById(R.id.iv_barcode);
        layout_cardDetail = findViewById(R.id.layout_cardDetail);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
