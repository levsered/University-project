package com.team_of_freedom.rnd_wheather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class db_work {
    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/wheather_db";
    private static final String USER = "verdgil";
    private static final String PASS = "0000";
    private Connection connection;
    private String GetString() {
        URLConnection connection = null;
        try {
            connection = new URL("https://api.openweathermap.org/data/2.5/forecast?id=501175&appid=e234bd4f6cf90feb586d37ac9c705a79").openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = null;
        try {
            assert connection != null;
            is = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert is != null;
        InputStreamReader reader = new InputStreamReader(is);
        char[] buffer = new char[256];
        int rc;

        StringBuilder sb = new StringBuilder();

        while (true) {
            try {
                if ((rc = reader.read(buffer)) == -1) break;
                sb.append(buffer, 0, rc);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public db_work() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
//        parser();
    }
    public void db_add_wheather(String date, float temp, String url) {
//        connection

        String insertTableSQL = "INSERT INTO wheather"
                + "(date, temp, img_url) " + "VALUES"
                + "("  + "'"
                + date + "', " +
                temp + ", '" + url + "')";
        System.out.println(insertTableSQL);
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(insertTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // выполнить SQL запрос

    }

    public void parser() {
        String str_json = this.GetString();
        JSONObject obj = new JSONObject(str_json);
        JSONArray arr = obj.getJSONArray("list");

        for(int i = 0; i < arr.length(); i++) {
            JSONObject inn_obj = arr.getJSONObject(i);
            String dt_second = inn_obj.getString("dt_txt");
            float temp = inn_obj.getJSONObject("main").getFloat("temp");
            JSONArray tmp_arr = inn_obj.getJSONArray("weather");
            String wheather_icon = "http://openweathermap.org/img/w/" + tmp_arr.getJSONObject(0).getString("icon") + ".png";;
            db_add_wheather(dt_second, temp, wheather_icon);
        }
    }

    public ArrayList<ArrayList<String>> read(){
        String selectTableSQL = "SELECT date, temp, img_url from wheather";
        try {

            Statement statement = connection.createStatement();
            ArrayList<ArrayList<String>> ret = new  ArrayList<ArrayList<String>>();
            // выбираем данные с БД
            ResultSet rs = statement.executeQuery(selectTableSQL);

            // И если что то было получено то цикл while сработает
            while (rs.next()) {
                String date = rs.getString("date");
                String temp = rs.getString("temp");
                String url = rs.getString("img_url");
                ArrayList<String> singleList = new ArrayList<String>();
                singleList.add(date);
                singleList.add(temp);
                singleList.add(url);
                ret.add(singleList);
            }
            return ret;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return null;
    }
//    private static String getCurrentTimeStamp() { Date today = new Date(); return dateFormat.format(today.getTime()); }

}
