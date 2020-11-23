package maxwell.vex.maxmart.Interface;

public interface UserCartItemClick {
    void deleteCartItem(int position);
    void increaseQuantity(int position);
    void decreaseQuantity(int position);
}
