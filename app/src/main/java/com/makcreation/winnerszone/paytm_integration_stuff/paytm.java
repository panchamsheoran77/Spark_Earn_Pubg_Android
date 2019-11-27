package com.makcreation.winnerszone.paytm_integration_stuff;
import com.google.gson.annotations.SerializedName;
import java.util.UUID;
public class paytm {
    @SerializedName("MID")
    String mId;
    @SerializedName("ORDER_ID")
    String orderId;
    @SerializedName("CUST_ID")
    String custId;
    @SerializedName("CHANNEL_ID")
    String channelId;
    @SerializedName("TXN_AMOUNT")
    String txnAmount;
    @SerializedName("WEBSITE")
    String website;
    @SerializedName("CALLBACK_URL")
    String callBackUrl;
    @SerializedName("INDUSTRY_TYPE_ID")
    String industryTypeId;
    public paytm(String mId, String custId,String channelId, String txnAmount, String website, String callBackUrl, String industryTypeId) {
        this.mId = mId;
        this.orderId = generateString();
        this.custId = custId;
        this.channelId = channelId;
        this.txnAmount = txnAmount;
        this.website = website;
        this.callBackUrl = callBackUrl+orderId;
        this.industryTypeId = industryTypeId;
    }
    public String getmId() {
        return mId;
    }
    public String getOrderId() {
        return orderId;
    }
    public String getCustId() {
        return custId;
    }
    public String getChannelId() {
        return channelId;
    }
    public String getTxnAmount() {
        return txnAmount;
    }
    public String getWebsite() {
        return website;
    }
    public String getCallBackUrl() {
        return callBackUrl;
    }
    public String getIndustryTypeId() {
        return industryTypeId;
    }

    private String generateString() {
        String uuid = UUID.randomUUID().toString().substring(0,10);
        return uuid.replaceAll("-", "");
    }
}
