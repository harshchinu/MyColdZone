package Model;

import java.util.List;

public class SubmittedOrders {
    private String total;
    private String data;
    private String phone;
    private List<Order> food;


    public SubmittedOrders() {
    }

    public SubmittedOrders(String total,String data,String phone, List<Order> food) {
        this.total = total;
        this.data=data;
        this.phone=phone;
        this.food = food;

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFood() {
        return food;
    }

    public void setFood(List<Order> food) {
        this.food = food;
    }


}
