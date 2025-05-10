package org.o7planning.nhom8_quanlychitieu.model;

public class Transaction {
    private long id;
    private String date;
    private String category;
    private double amount;
    private String title;
    private String note;
    private boolean isExpense; // true: chi phí, false: thu nhập

    public Transaction() {
    }

    public Transaction(String date, String category, double amount, String title, String note) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.title = title;
        this.note = note;
        this.isExpense = amount < 0;
    }

    public Transaction(long id, String date, String category, double amount, String title, String note, boolean isExpense) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.title = title;
        this.note = note;
        this.isExpense = isExpense;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        this.isExpense = amount < 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public void setExpense(boolean expense) {
        isExpense = expense;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", note='" + note + '\'' +
                ", isExpense=" + isExpense +
                '}';
    }
}