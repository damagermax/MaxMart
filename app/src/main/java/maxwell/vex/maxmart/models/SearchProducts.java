package maxwell.vex.maxmart.models;

public class SearchProducts {
    private  String name;
    private  String price;
    private  String Image;

    public SearchProducts() {
    }

    public SearchProducts(String name, String price, String image) {
        this.name = name;
        this.price = price;
        this.Image = image;
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
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
