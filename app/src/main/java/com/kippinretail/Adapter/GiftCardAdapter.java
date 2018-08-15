package com.kippinretail.Adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.Interface.OnSelectionChanged;
import com.kippinretail.Modal.VoucherModal;
import com.kippinretail.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sandeep.saini on 3/16/2016.
 */
public class GiftCardAdapter extends BaseAdapter
{
    private LayoutInflater inflater = null;
    public List<VoucherModal> _voucherModals;
    private Context mcontext;
    private int width,height;
    OnSelectionChanged onSelectionChanged;
    boolean  isMultichoice  = true;

    CheckBox old_cb= null;
    int old_position = -1;
    boolean old_check = false;

    public GiftCardAdapter(Context mcontext, List<VoucherModal> voucherModals)
    {
        this.mcontext = mcontext;
        this._voucherModals = voucherModals;
        inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        width =(mcontext.getResources().getDisplayMetrics().widthPixels)/2;
        height = dpToPx(100);
    }

    public GiftCardAdapter(Context mcontext, List<VoucherModal> voucherModals, boolean isMultiChoice , OnSelectionChanged onSelectionChanged) {
        this.mcontext = mcontext;
        this._voucherModals = voucherModals;
        inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        width =(mcontext.getResources().getDisplayMetrics().widthPixels)/2;
        height = dpToPx(100);
        this.isMultichoice = isMultiChoice;
        this.onSelectionChanged = onSelectionChanged;
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mcontext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    @Override
    public int getCount()
    {
      return _voucherModals.size();
    }

    @Override
    public Object getItem(int position) {
        return _voucherModals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent)
    {
        ViewHolder holder = null;

//        final int index = position;
        if(view == null)
        {
            view = inflater.inflate(R.layout.subitem_gift_card_grid , parent , false);
            holder = new ViewHolder();
            holder.layout = (RelativeLayout)view.findViewById(R.id.layout_giftcard);
            holder.voucherImager = (ImageView)view.findViewById(R.id.imageViewGift);;
            holder.txtprice =  (TextView)view.findViewById(R.id.txtprice);
            holder.checkbox = (CheckBox)view.findViewById(R.id.checkbox);
            view.setTag(holder);
        }
        else
        {
           holder = (ViewHolder) view.getTag();
        }

        if(_voucherModals.get(position).getUrl()!=null)
        {
            Picasso.with(mcontext).load(_voucherModals.get(position).getUrl()).resize(width,height).into(holder.voucherImager);
        }
        if(_voucherModals.get(position).getPrice()!=null)
        {
            double amount = Double.parseDouble(_voucherModals.get(position).getPrice());
            holder.txtprice.setText("Value :$"+_voucherModals.get(position).getPrice());
        }


        boolean isChecked  =_voucherModals.get(position).ischeched();

        holder.checkbox.setChecked(isChecked);

        if(isChecked){
            old_cb = holder.checkbox;
            old_position = position;
        }

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (old_cb != null && position != old_position && !isMultichoice) {
                    old_cb.setChecked(false);
                }
                CheckBox cb = (CheckBox) v;
                boolean isChecked = cb.isChecked();
                _voucherModals.get(position).setIscheched(isChecked);
                if (!isMultichoice) {
                    if (position != old_position && old_cb != null)
                        _voucherModals.get(old_position).setIscheched(false);
                    onSelectionChanged.onSelectionChanged(position, _voucherModals.get(position) , isChecked);
                    GiftCardAdapter.this.old_cb = cb;
                    GiftCardAdapter.this.old_position = position;
                }else onSelectionChanged.onSelectionChanged(position ,_voucherModals.get(position) ,  isChecked);
            }
        });
//
//        holder.cbSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });


        return view;
    }

     class ViewHolder
     {
         RelativeLayout layout;
         ImageView voucherImager;
         TextView txtprice;
         CheckBox checkbox;
     }
}
