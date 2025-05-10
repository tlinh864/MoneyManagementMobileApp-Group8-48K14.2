package org.o7planning.nhom8_quanlychitieu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.o7planning.nhom8_quanlychitieu.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expense_manager.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng giao dịch
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_IS_EXPENSE = "is_expense";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng giao dịch
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_AMOUNT + " REAL,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_NOTE + " TEXT,"
                + COLUMN_IS_EXPENSE + " INTEGER"
                + ")";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu tồn tại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);

        // Tạo lại bảng
        onCreate(db);
    }

    // Thêm giao dịch mới
    public long addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, transaction.getDate());
        values.put(COLUMN_CATEGORY, transaction.getCategory());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_TITLE, transaction.getTitle());
        values.put(COLUMN_NOTE, transaction.getNote());
        values.put(COLUMN_IS_EXPENSE, transaction.isExpense() ? 1 : 0);

        // Chèn dòng mới
        long id = db.insert(TABLE_TRANSACTIONS, null, values);

        // Đóng kết nối
        db.close();

        return id;
    }

    // Lấy tất cả giao dịch
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactionList = new ArrayList<>();

        // Câu truy vấn SELECT
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Duyệt qua tất cả các dòng và thêm vào danh sách
        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getLong(0));
                transaction.setDate(cursor.getString(1));
                transaction.setCategory(cursor.getString(2));
                transaction.setAmount(cursor.getDouble(3));
                transaction.setTitle(cursor.getString(4));
                transaction.setNote(cursor.getString(5));
                transaction.setExpense(cursor.getInt(6) == 1);

                // Thêm vào danh sách
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        // Đóng cursor và kết nối
        cursor.close();
        db.close();

        return transactionList;
    }

    // Lấy giao dịch theo ID
    public Transaction getTransaction(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRANSACTIONS, new String[] {
                COLUMN_ID, COLUMN_DATE, COLUMN_CATEGORY, COLUMN_AMOUNT, COLUMN_TITLE, COLUMN_NOTE, COLUMN_IS_EXPENSE
        }, COLUMN_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Transaction transaction = new Transaction(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getDouble(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getInt(6) == 1
        );

        cursor.close();
        db.close();

        return transaction;
    }

    // Lấy tổng số dư
    public double getTotalBalance() {
        double totalBalance = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS, null);

        if (cursor.moveToFirst()) {
            totalBalance = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return totalBalance;
    }

    // Lấy tổng chi phí
    public double getTotalExpense() {
        double totalExpense = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS +
                " WHERE " + COLUMN_IS_EXPENSE + " = 1", null);

        if (cursor.moveToFirst()) {
            totalExpense = Math.abs(cursor.getDouble(0));
        }

        cursor.close();
        db.close();

        return totalExpense;
    }

    // Lấy tổng thu nhập
    public double getTotalIncome() {
        double totalIncome = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS +
                " WHERE " + COLUMN_IS_EXPENSE + " = 0", null);

        if (cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return totalIncome;
    }

    // Xóa giao dịch
    public void deleteTransaction(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Cập nhật giao dịch
    public int updateTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, transaction.getDate());
        values.put(COLUMN_CATEGORY, transaction.getCategory());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_TITLE, transaction.getTitle());
        values.put(COLUMN_NOTE, transaction.getNote());
        values.put(COLUMN_IS_EXPENSE, transaction.isExpense() ? 1 : 0);

        // Cập nhật dòng
        return db.update(TABLE_TRANSACTIONS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(transaction.getId())});
    }

    // Lấy giao dịch theo loại (chi phí hoặc thu nhập)
    public List<Transaction> getTransactionsByType(boolean isExpense) {
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRANSACTIONS, new String[] {
                COLUMN_ID, COLUMN_DATE, COLUMN_CATEGORY, COLUMN_AMOUNT, COLUMN_TITLE, COLUMN_NOTE, COLUMN_IS_EXPENSE
        }, COLUMN_IS_EXPENSE + "=?", new String[] { isExpense ? "1" : "0" }, null, null, COLUMN_DATE + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getLong(0));
                transaction.setDate(cursor.getString(1));
                transaction.setCategory(cursor.getString(2));
                transaction.setAmount(cursor.getDouble(3));
                transaction.setTitle(cursor.getString(4));
                transaction.setNote(cursor.getString(5));
                transaction.setExpense(cursor.getInt(6) == 1);

                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return transactionList;
    }

    // Lấy giao dịch theo danh mục
    public List<Transaction> getTransactionsByCategory(String category) {
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRANSACTIONS, new String[] {
                COLUMN_ID, COLUMN_DATE, COLUMN_CATEGORY, COLUMN_AMOUNT, COLUMN_TITLE, COLUMN_NOTE, COLUMN_IS_EXPENSE
        }, COLUMN_CATEGORY + "=?", new String[] { category }, null, null, COLUMN_DATE + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getLong(0));
                transaction.setDate(cursor.getString(1));
                transaction.setCategory(cursor.getString(2));
                transaction.setAmount(cursor.getDouble(3));
                transaction.setTitle(cursor.getString(4));
                transaction.setNote(cursor.getString(5));
                transaction.setExpense(cursor.getInt(6) == 1);

                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return transactionList;
    }

    // Tìm kiếm giao dịch theo từ khóa
    public List<Transaction> searchTransactions(String keyword) {
        List<Transaction> transactionList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS +
                " WHERE " + COLUMN_TITLE + " LIKE '%" + keyword + "%'" +
                " OR " + COLUMN_CATEGORY + " LIKE '%" + keyword + "%'" +
                " OR " + COLUMN_NOTE + " LIKE '%" + keyword + "%'" +
                " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getLong(0));
                transaction.setDate(cursor.getString(1));
                transaction.setCategory(cursor.getString(2));
                transaction.setAmount(cursor.getDouble(3));
                transaction.setTitle(cursor.getString(4));
                transaction.setNote(cursor.getString(5));
                transaction.setExpense(cursor.getInt(6) == 1);

                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return transactionList;
    }
}