package com.university.dao;

public class TestJDBCDriver {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ JDBC Driver successfully loaded!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found! " + e.getMessage());
        }
    }
}