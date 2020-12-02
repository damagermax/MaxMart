package maxwell.vex.maxmart.models;

public class UserSearchProduct {
    private String name,productID;

    public UserSearchProduct() {
    }

    public UserSearchProduct(String name, String productID) {
        this.name = name;
        this.productID = productID;
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
