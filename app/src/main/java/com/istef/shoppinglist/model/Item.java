package com.istef.shoppinglist.model;

public class Item {
    private long id;
    private String item;
    private int size;
    private int quantity;
    private String dateAdded;


    public Item() {
    }

    public Item(String item, int size, int quantity, String dateAdded) {
        this.item = item;
        this.size = size;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
    }

    public Item(long id, String item, int size, int quantity, String dateAdded) {
        this(item, size, quantity, dateAdded);
        this.id = id;
    }



    public long getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", item='" + item + '\'' +
                ", size=" + size +
                ", quantity=" + quantity +
                ", dateAdded='" + dateAdded + '\'' +
                '}';
    }
}
