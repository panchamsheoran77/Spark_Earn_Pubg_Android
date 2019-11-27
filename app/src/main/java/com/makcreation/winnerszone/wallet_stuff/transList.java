package com.makcreation.winnerszone.wallet_stuff;

public class transList {
    private String sno,amt,id;
    public transList(String sno, String amt, String id) {
        this.sno = sno;
        this.amt = amt;
        this.id = id;
    }
    public String getSno() {
        return sno;
    }
    public String getAmt() {
        return amt;
    }
    public String getId() {
        return id;
    }
}
