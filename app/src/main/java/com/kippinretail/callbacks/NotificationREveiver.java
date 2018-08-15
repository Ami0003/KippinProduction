package com.kippinretail.callbacks;

/**
 * Created by sandeep.singh on 8/3/2016.
 */
public interface NotificationREveiver {
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard,
                                   boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty);

}
