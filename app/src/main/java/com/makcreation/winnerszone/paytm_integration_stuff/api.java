package com.makcreation.winnerszone.paytm_integration_stuff;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface api {
    String BASE_URL = "https://winnerzone.000webhostapp.com/ypsssheoranpancham7779875648318109716921/";
    @FormUrlEncoded
    @POST("generateChecksum.php")
    Call<checkSum> getChecksum(
            @Field("MID") String mId,
            @Field("ORDER_ID") String orderId,
            @Field("CUST_ID") String custId,
            @Field("INDUSTRY_TYPE_ID") String industryTypeId,
            @Field("CHANNEL_ID") String channelId,
            @Field("TXN_AMOUNT") String txnAmount,
            @Field("WEBSITE") String website,
            @Field("CALLBACK_URL") String callbackUrl

    );
}
