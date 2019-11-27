package com.makcreation.winnerszone.play_secion;

public class play_stuff {
    private String winprice,perkill,enteyfee;
    private String type_txt,version_txt,map,matchno,date_time,roomid,roompass;
    private int remaing;
    private boolean isJoined;

    play_stuff(String winprice, String perkill, String enteyfee, String type, String version, String map, String matchno, String date_time,int remaing,boolean isJoined,String roomid,String roompass) {
        this.matchno = matchno;
        this.date_time = date_time;
        this.winprice = winprice;
        this.perkill = perkill;
        this.enteyfee = enteyfee;
        this.type_txt = type;
        this.version_txt = version;
        this.map = map;
        this.remaing = remaing;
        this.isJoined = isJoined;
        this.roomid = roomid;
        this.roompass = roompass;
    }

    public boolean isJoined() {
        return isJoined;
    }
    public void setMap(String map) {
        this.map = map;
    }
    public String getMatchno() {
        return matchno;
    }
    public String getDate_time() {
        return date_time;
    }
    public String getWinprice() {
        return winprice;
    }
    public String getPerkill() {
        return perkill;
    }
    public String getEnteyfee() {
        return enteyfee;
    }
    public String getType_txt() {
        return type_txt;
    }
    public String getVersion_txt() {
        return version_txt;
    }
    public String getMap() {
        return map;
    }
    public int getRemaing() {
        return remaing;
    }
    public void setJoined(boolean joined) {
        isJoined = joined;
    }
    public String getRoomid() {
        return roomid;
    }
    public String getRoompass() {
        return roompass;
    }
    public play_stuff(boolean isJoined) {
    this.isJoined = isJoined;
    }
}