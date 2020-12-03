package maxwell.vex.maxmart;

public class SimilarProducts {
    private String image;
    private  String name;
    private  String description;
    private  String price;
    private  String productID;
    private  String category;

    public SimilarProducts() {
    }

    public SimilarProducts(String image, String name, String description, String price, String productID, String category) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
        this.productID = productID;
        this.category = category;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
