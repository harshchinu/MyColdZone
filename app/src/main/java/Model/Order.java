package Model;

public class Order {
    private int ID;
    private String ProductId;
    private String ProductName;private String Quantity;private String Price;


    public Order() {
    }

    public Order(String productId, String productName, String quantity, String price) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;

    }

    @Override
    public String toString() {
        return "Product id: " + this.getProductId() +
                "Product Name " + this.getProductName()+
                "Quantity " + this.getQuantity() +
                "Price" + this.getPrice();

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Order(int ID, String productId, String productName, String quantity, String price) {
        this.ID = ID;
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }



}

