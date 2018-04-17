package com.qnally.shappapp.Helper;

import com.qnally.shappapp.Remote.ItemRequest;
import com.qnally.shappapp.Remote.RetrofitClient;

public class Common {

    public static ItemRequest getItemRequest(){

        return RetrofitClient.getClient("https://shappapp-c2aaf.firebaseio.com/").create(ItemRequest.class);
    }
}
