package notification;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.Modal.NotificationModel;
import com.kippinretail.callbacks.NotificationREveiver;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sandeep.singh on 8/3/2016.
 */
public class NotificationHandler {


    private static NotificationHandler handler = null;

    public static NotificationHandler getInstance() {
        if (handler != null) {
            return handler;
        } else {
            handler = new NotificationHandler();
            return handler;
        }
    }

    private NotificationHandler() {

    }

    public void getNotificationForVoucher(Activity context, final NotificationREveiver receiver) {
        try {
            String userId = String.valueOf(CommonData.getUserData(context).getId());
            RestClient.getApiServiceForPojo().AddedNewVoucherByMerchant(userId, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());

                    Gson gson = new Gson();
                    Boolean result = gson.fromJson(jsonElement.toString(), Boolean.class);
                    receiver.handleNotification(result.booleanValue(), false, false, false, false, false, false);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("RestClient", error.getUrl());

                }
            });
        }catch(Exception ec){
            Log.e("",ec.getMessage());
        }
    }

    public void getNotificationForCards(Activity context, final NotificationREveiver receiver) {
        try {
            String userId = String.valueOf(CommonData.getUserData(context).getId());
            RestClient.getApiServiceForPojo().GetNotification(userId, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());

                    Gson gson = new Gson();
                    NotificationModel model = gson.fromJson(jsonElement.toString(), NotificationModel.class);
                    if (model != null) {
                        receiver.handleNotification(model.isVoucher(), model.isTradePoint(), model.isFriendRequest(), model.istransferGiftCard(),
                                model.isNewMerchant(), model.isNonKippinPhysical(), model.isNonKippinLoyalty());
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("RestClient", error.getUrl());

                }
            });
        }catch(Exception ex){
                Log.e("",ex.getMessage());
        }

        }



}
