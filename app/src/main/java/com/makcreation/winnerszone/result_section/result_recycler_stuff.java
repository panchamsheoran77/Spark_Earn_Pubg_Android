package com.makcreation.winnerszone.result_section;
public class result_recycler_stuff {
private String winprice,perkill,enteyfee,winner1,winner2,winner3;
private String type_txt,version_txt,map,matchno,date_time;

    public result_recycler_stuff(String winprice, String perkill, String enteyfee, String type, String version, String map, String matchno, String date_time,String winner1,String winner2,String winner3) {
        this.matchno = matchno;
        this.date_time = date_time;
        this.winprice = winprice;
        this.perkill = perkill;
        this.enteyfee = enteyfee;
        this.type_txt = type;
        this.version_txt = version;
        this.map = map;
        this.winner1 = winner1;
        this.winner2 = winner2;
        this.winner3 = winner2;
    }

    public String getWinner1() {
        return winner1;
    }
    public String getWinner2() {
        return winner2;
    }
    public String getWinner3() {
        return winner3;
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
}
