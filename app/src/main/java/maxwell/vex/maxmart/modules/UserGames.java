package maxwell.vex.maxmart.modules;

public class UserGames {
    private String image;
    private String price;
    private String name;
    private  String productID;

    public UserGames() {
    }

    public UserGames(String image, String price, String name, String productID) {
        this.image = image;
        this.price = price;
        this.name = name;
        this.productID = productID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
