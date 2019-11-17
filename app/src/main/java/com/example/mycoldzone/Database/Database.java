package com.example.mycoldzone.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import Model.Order;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "MCZDB.db";
    private static final int DB_ver = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_ver);
    }

    public boolean checkFoodExists(String foodId){
        boolean flag = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=null;
        String Query = String.format("SELECT * from OrderDetails WHERE Productid='%s'",foodId);
        cursor = db.rawQuery(Query,null);
        if(cursor.getCount()>0)
            return flag=true;
        else
            return flag=false;

    }

    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ID","ProductName", "ProductId", "Quantity", "Price"};
        String sqlTable = "OrderDetails";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(
                        c.getInt(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price"))
                ));
            } while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format("INSERT INTO OrderDetails(ProductId,ProductName,Quantity,Price)VALUES('%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice());

        db.execSQL(query);
    }


    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetails");
        db.execSQL(query);
    }

    public int getCountCart() {
        int count = 0;

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetails");

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return count;

    }


    public void updateCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetails SET Quantity= %s WHERE ID = %d",order.getQuantity(),order.getID());
        db.execSQL(query);
    }

    public void removeFromCart(String productId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetails WHERE ProductId='%s'",productId);
        db.execSQL(query);
    }
}
