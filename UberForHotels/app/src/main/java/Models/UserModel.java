package Models;
public class UserModel {

    private String name;
    private String email;
    private String password;
    private String id;
    private String userid;
    private String phone;
    private String ImageUrl;

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    private String usertype;

    public UserModel(String name, String email, String phone, String imageUrl) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        ImageUrl = imageUrl;
    }

    public UserModel(String usertype, String name, String email, String password, String phone, String imageUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.ImageUrl = imageUrl;
        this.usertype = usertype;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }





public UserModel() {
        }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getpassword() {
        return password;
        }

public void setpassword(String password) {
        this.password = password;
        }



public String getname() {
        return name;
        }

public void setname(String name) {
        this.name = name;
        }

public String getemail() {
        return email;
        }

public void setemail(String email) {
        this.email = email;
        }



public String getid() {
        return id;
        }

public void setid(String id) {
        this.id = id;
        }


        }

