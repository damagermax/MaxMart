package maxwell.vex.maxmart.models;

public class AdminProductManagement {
    private  String name;
    private  String productID;
    private  String image;
    private  String price;
    private  String description;
    private  String category;

    public AdminProductManagement() {
    }

    public AdminProductManagement(String name, String productID, String image, String price, String description, String category) {
        this.name = name;
        this.productID = productID;
        this.image = image;
        this.price = price;
        this.description = description;
        this.category = category;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
