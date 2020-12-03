package maxwell.vex.maxmart.models;

public class AllProducts {
    private  String name;
    private  String price;
    private  String image;
    private  String category;
    private  String productID;

    public AllProducts() {
    }

    public AllProducts(String name, String price, String image, String category, String productID) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
