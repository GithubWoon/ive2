package payback.ive2;

import java.sql.Date;

import javax.persistence.*;
@Entity
@Table(name = "Receipt")
public class Receipt {

    private String id;
    private String name;
    private String productname;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "buyday")
    private Date buyDay;

    @Id
    @Column(name = "purchasenum")
    private int purchaseNum;
    
    // Getter, Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getBuyDay() {
        return buyDay;
    }

    public void setBuyDay(Date buyDay) {
        this.buyDay = buyDay;
    }

    public Integer getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(int purchaseNum) {
        this.purchaseNum = purchaseNum;
    }
}