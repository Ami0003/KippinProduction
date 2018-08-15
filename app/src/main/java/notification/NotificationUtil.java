package notification;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.kippinretail.Adapter.GridViewAdpter;
import com.kippinretail.R;

/**
 * Created by sandeep.singh on 8/3/2016.
 */
public class NotificationUtil {
    private static NotificationUtil util;
    public static NotificationUtil getInstanse(){
        if(util!=null){
            return  util;
        }else{
            util = new NotificationUtil();
            return util;
        }
    }

    public static void setNotification(int index , GridView grid,boolean visibilty){
        View subItemView = (View) grid.getChildAt(index);
        if(subItemView!=null){
            ImageView v = (ImageView)subItemView.findViewById(R.id.iv_star);
            if(visibilty) {
                v.setVisibility(View.VISIBLE);
            }else{
                v.setVisibility(View.GONE);
            }
        }
    }

}
