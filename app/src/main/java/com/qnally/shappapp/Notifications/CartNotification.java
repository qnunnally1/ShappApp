package com.qnally.shappapp.Notifications;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.view.MenuItem;

public class CartNotification extends Activity{
    private static LayerDrawable icon;
    public CartNotification() {
        //constructor
    }

    public static void setAddToCart(Context context, MenuItem item, int numMessages) {
        icon = (LayerDrawable) item.getIcon();
        CartCount.setBadgeCount(context, icon, CartNotification.setNotifyCount(numMessages));

    }

    public static int setNotifyCount(int numMessages) {
        int count=numMessages;
        return count;

    }
}
