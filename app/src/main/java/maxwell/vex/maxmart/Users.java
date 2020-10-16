package maxwell.vex.maxmart;

public class Users {
   private String name;
   private String nickname;
   private String phone;
   private String address;
   private String image;


    public Users() {
    }

    public Users(String name, String nickname, String phone, String address, String image, String password) {
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
        this.image = image;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
