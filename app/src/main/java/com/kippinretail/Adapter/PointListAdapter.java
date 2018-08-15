package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.PointList.PointDetail;
import com.kippinretail.R;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sandeep.saini on 3/26/2016.
 */
public class PointListAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private Activity mcontext;
    private List<PointDetail> pointDetails;
    boolean isAcceptButtonDisable;
    private boolean isShowSideButton = true;
    View terms;
    boolean value = false;
    boolean outgoing = false;

    public PointListAdapter(Activity mcontext, List<PointDetail> pointDetails, boolean isAcceptButtonDisable) {
        value = false;
        this.mcontext = mcontext;
        this.pointDetails = pointDetails;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isAcceptButtonDisable = isAcceptButtonDisable;
    }

    public PointListAdapter(Activity mcontext, List<PointDetail> pointDetails, boolean isAcceptButtonDisable, boolean isShowSideButton) {
        value = false;
        this.isShowSideButton = isShowSideButton;
        this.mcontext = mcontext;
        this.pointDetails = pointDetails;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isAcceptButtonDisable = isAcceptButtonDisable;
    }

    public PointListAdapter(Activity mcontext, List<PointDetail> pointDetails, boolean isAcceptButtonDisable, boolean isShowSideButton, boolean outgoing) {
        value = false;
        this.outgoing = outgoing;
        this.isShowSideButton = isShowSideButton;
        this.mcontext = mcontext;
        this.pointDetails = pointDetails;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isAcceptButtonDisable = isAcceptButtonDisable;
    }

    public PointListAdapter(Activity mcontext, List<PointDetail> pointDetails, boolean isAcceptButtonDisable, View terms) {
        value = true;
        this.mcontext = mcontext;
        this.pointDetails = pointDetails;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isAcceptButtonDisable = isAcceptButtonDisable;
        this.terms = terms;
    }

    @Override
    public int getCount() {
        return pointDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Log.e("getView", "getView");
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.subitem_point_list, parent, false);
            holder.txtFriendName = (TextView) view.findViewById(R.id.txtFriendName);
            holder.txtMerchantName = (TextView) view.findViewById(R.id.txtMerchantName);
            holder.txtPoints = (TextView) view.findViewById(R.id.txtPoints);
            holder.btn_accept = (TextView) view.findViewById(R.id.btn_accept);
            // if(isAcceptButtonDisable){
            //  holder.btn_accept.setVisibility(View.GONE);
            //  }
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
            //  if(isAcceptButtonDisable){
            //  holder.btn_accept.setVisibility(View.INVISIBLE);
            //  }
        }
        //if(isShowSideButton) {

        if (pointDetails.get(position).getIsLoyalitySignUp().booleanValue()) {
            if (outgoing) {
                holder.btn_accept.setVisibility(View.GONE);
                holder.btn_accept.setText("Accept");
            } else {
                holder.btn_accept.setVisibility(View.VISIBLE);
                holder.btn_accept.setText("Accept");
            }

        } else {
            if (outgoing) {
                holder.btn_accept.setVisibility(View.GONE);
                holder.btn_accept.setText("Accept");
            } else {
                holder.btn_accept.setVisibility(View.VISIBLE);
                holder.btn_accept.setText("Accept");
            }
            //  holder.btn_accept.setVisibility(View.VISIBLE);
            //  holder.btn_accept.setText("Accept");
            //holder.btn_accept.setText("Not\napplicable");
        }
        //}
        if (pointDetails.get(position) != null) {
            if (pointDetails.get(position).getFreindName() != null) {
                holder.txtFriendName.setText(pointDetails.get(position).getFreindName());
            }
            if (pointDetails.get(position).getMerchantName() != null) {
                holder.txtMerchantName.setText(pointDetails.get(position).getMerchantName());
            }
            if (pointDetails.get(position).getPoints() != null) {
                holder.txtPoints.setText(pointDetails.get(position).getPoints());
            }
            holder.btn_accept.setTag(new Integer(position));
            holder.btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer index = (Integer) v.getTag();
                    final PointDetail _poinPointDetail = pointDetails.get(index.intValue());
                    if (value) {
                        terms.setVisibility(View.VISIBLE);
                        Animation animation = AnimationUtils.loadAnimation(mcontext, R.anim.translate_up);
                        terms.startAnimation(animation);
                        WebView w = (WebView) terms.findViewById(R.id.wv_terms);
                        w.loadData(_poinPointDetail.getTermsConditions(), "text/html", "UTF-8");
                        terms.findViewById(R.id.txt_accept).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("LoyalityBarCode:", "" + _poinPointDetail.getLoyalityBarCode());
                                Log.e("getPoints:", "" + _poinPointDetail.getPoints());
                                Log.e("getUserLoyalityBarCode:", "" + _poinPointDetail.getUserLoyalityBarCode());
                                if (_poinPointDetail.getUserLoyalityBarCode() != null) {
                                    acceptRequest(_poinPointDetail.getLoyalityBarCode(), _poinPointDetail.getPoints(), _poinPointDetail.getUserLoyalityBarCode());
                                } else {
                                    acceptRequest(_poinPointDetail.getLoyalityBarCode(), _poinPointDetail.getPoints(), "0");
                                }

                            }
                        });
                        terms.findViewById(R.id.lalayout_ivBack).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mcontext.finish();
                            }
                        });
                    } else {

                        if (!_poinPointDetail.getIsLoyalitySignUp().booleanValue()) {
                            MessageDialog.showDialog(mcontext, "Please navigate to the Loyalty Program SignUp and Merchant.Once merchant is enabled you can return and accept the points.Merchant signup must be done within 7 days else points will be lost", false);
                        } else {
                            if (_poinPointDetail.getUserLoyalityBarCode() != null) {
                                acceptRequest(_poinPointDetail.getLoyalityBarCode(), _poinPointDetail.getPoints(), _poinPointDetail.getUserLoyalityBarCode());
                            } else {
                                acceptRequest(_poinPointDetail.getLoyalityBarCode(), _poinPointDetail.getPoints(), "0");
                            }
                            //  acceptRequest(_poinPointDetail.getLoyalityBarCode(), _poinPointDetail.getPoints(), _poinPointDetail.getUserLoyalityBarCode());
                        }
                    }

                }
            });

        }

        return view;
    }

    private void acceptRequest(final String barcode, String points, String userLoyaltyBarcode) {
        final String userId = String.valueOf(CommonData.getUserData(mcontext).getId());
        LoadingBox.showLoadingDialog(mcontext, "Loading...");
        RestClient.getApiServiceForPojo().AcceptIncomingTradePoints(userId, barcode, points, userLoyaltyBarcode, "",
                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        Log.e("Output -->", jsonElement.toString());
                        Type listType = new TypeToken<List<PointDetail>>() {
                        }.getType();
                        Gson gson = new Gson();
                        Boolean flag = gson.fromJson(jsonElement.toString(), Boolean.class);
                        if (flag.booleanValue()) {
                            MessageDialog.showDialog(mcontext, "Points successfully added to your loyalty card", true);
                        } else {

                        }
                        removeNotificationForTradePoint(userId, barcode);

                        LoadingBox.dismissLoadingDialog();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                    }
                });
    }

    private void removeNotificationForTradePoint(String userId, String barcode) {


        CommonUtility.isMoveForward = true;


        RestClient.getApiServiceForPojo().RemoveIsReadTradePoints(userId, barcode, "", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Gson gson = new Gson();
                Boolean serverresult = gson.fromJson(jsonElement.toString(), Boolean.class);
                if (serverresult.booleanValue()) {
                    Log.e("Notification ", "Remove ");
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e(error.getMessage(), error.getUrl());
            }
        });
    }

    class ViewHolder {
        TextView txtFriendName, txtMerchantName, txtPoints;
        TextView btn_accept;
    }
}
