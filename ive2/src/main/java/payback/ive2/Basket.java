package payback.ive2;

import javax.persistence.*;

@Entity
@Table(name = "BASKET")
public class Basket {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "PRODUCTNAME")
    private String productName;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "NAME")
    private String name;

    // getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
