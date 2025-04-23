package Objects;

import android.graphics.Bitmap;

public class Product {
    private String nameProduct;
    private String category,image;
    private String description,nativeKeyProduct;
    private int price;
    private Boolean shoppingCardExist = false;
    private Bitmap bitmap;
    private int quantity, personQuantityInt;
    private Boolean personQuantity=false;
    private int place;
    private boolean check;

    public Product() {
    }

    public Product(String nameProduct, String category, String description, String nativeKeyProduct, int price, String image, int quantity1, boolean personQuantity, int personQuantityInt) {
        this.nameProduct = nameProduct;
        this.category = category;
        this.description = description;
        this.nativeKeyProduct = nativeKeyProduct;
        this.price = price;
        this.image = image;
        this.shoppingCardExist = false;
        this.quantity=quantity1;
        this.personQuantity=personQuantity;
        this.personQuantityInt=personQuantityInt;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getPersonQuantityInt() {
        return personQuantityInt;
    }

    public void setPersonQuantityInt(int personQuantityInt) {
        this.personQuantityInt = personQuantityInt;
    }

    public Boolean getPersonQuantity() {
        return personQuantity;
    }

    public void setPersonQuantity(Boolean personQuantity) {
        this.personQuantity = personQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getNativeKeyProduct() {
        return nativeKeyProduct;
    }

    public void setNativeKeyProduct(String nativeKeyProduct) {
        this.nativeKeyProduct = nativeKeyProduct;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String  image) {
        this.image = image;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String  getPriceString() {
        return String.valueOf(price) + "тг ";
    }

    public int getPrice(){
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Boolean getShoppingCardExist() {
        return shoppingCardExist;
    }

    public void setShoppingCardExist(Boolean shoppingCardExist) {
        this.shoppingCardExist = shoppingCardExist;
    }
}
