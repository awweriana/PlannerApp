package service;

import model.WishItem;
import java.util.List;

public interface WishListService {
    void saveWishListService(WishItem wishItem);
    List<WishItem> loadWishListService();
    void deleteWishList(WishItem wishItem);
}
