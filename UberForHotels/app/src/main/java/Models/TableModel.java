package Models;

public class TableModel {
    String tableno;
    String noofchairs;
    String internetstatus;
    String rent;
    String pushid;
    String tablestatus;
    String reservationtime;

    public String getTableno() {
        return tableno;
    }

    public void setTableno(String tableno) {
        this.tableno = tableno;
    }

    public String getNoofchairs() {
        return noofchairs;
    }

    public void setNoofchairs(String noofchairs) {
        this.noofchairs = noofchairs;
    }

    public String getInternetstatus() {
        return internetstatus;
    }

    public void setInternetstatus(String internetstatus) {
        this.internetstatus = internetstatus;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public String getTablestatus() {
        return tablestatus;
    }

    public void setTablestatus(String tablestatus) {
        this.tablestatus = tablestatus;
    }

    public String getReservationtime() {
        return reservationtime;
    }

    public void setReservationtime(String reservationtime) {
        this.reservationtime = reservationtime;
    }

    public TableModel() {
    }

    public TableModel(String tableno, String noofchairs, String internetstatus, String rent, String pushid, String tablestatus, String reservationtime) {
        this.tableno = tableno;
        this.noofchairs = noofchairs;
        this.internetstatus = internetstatus;
        this.rent = rent;
        this.pushid = pushid;
        this.tablestatus = tablestatus;
        this.reservationtime = reservationtime;
    }
}
