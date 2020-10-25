package maxwell.vex.maxmart.modules;

public class UserCartProduct {
    private String price;
    private String image;
    private String name;
    private String productID;

    public UserCartProduct() {
    }

    public UserCartProduct(String price, String image, String name, String productID) {
        this.price = price;
        this.image = image;
        this.name = name;
        this.productID = productID;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
