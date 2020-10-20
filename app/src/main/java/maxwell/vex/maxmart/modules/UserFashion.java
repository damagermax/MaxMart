package maxwell.vex.maxmart.modules;

public class UserFashion {
    private  String name;
    private  String price;
    private  String image;
    private  String productID;

    public UserFashion() {

    }

    public UserFashion(String name, String price, String image, String productID) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
