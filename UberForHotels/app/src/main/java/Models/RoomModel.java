package Models;

public class RoomModel {
    String roomno;
    String noofbeds;
    String internet;
    String rent;
    String phone;
    String imgul;
    String pushid;
    String roomstatus;

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getNoofbeds() {
        return noofbeds;
    }

    public void setNoofbeds(String noofbeds) {
        this.noofbeds = noofbeds;
    }

    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgul() {
        return imgul;
    }

    public void setImgul(String imgul) {
        this.imgul = imgul;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public String getRoomstatus() {
        return roomstatus;
    }

    public RoomModel() {
    }

    public void setRoomstatus(String roomstatus) {
        this.roomstatus = roomstatus;
    }

    public RoomModel(String roomno, String noofbeds, String internet, String rent, String phone, String imgul, String pushid, String roomstatus) {
        this.roomno = roomno;
        this.noofbeds = noofbeds;
        this.internet = internet;
        this.rent = rent;
        this.phone = phone;
        this.imgul = imgul;
        this.pushid = pushid;
        this.roomstatus = roomstatus;
    }
}
