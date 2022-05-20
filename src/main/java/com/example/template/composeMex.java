package com.example.template;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class composeMex {
    private static  Connection conn;
    final static private  String host="jdbc:mysql://localhost:3306/db_trenitalia";
    final static private String username ="root";
    static Result r;
    public static void creaConnessione() {
        try {
            conn=DriverManager.getConnection(host,username, "");
            System.out.println(conn.toString()); 
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.toString());;
        }
        r= new Result();
    }


    public Result update(String sql, String message, String error) throws SQLException{
        LinkedHashMap<String,Object> map= new LinkedHashMap<>();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        if(ps.executeUpdate()>0){
            r.setResult(true);
            map.put("result", message);
            r.setMessage(map);
        }else{
            r.setResult(false);
            map.put("result", error);
            r.setMessage(map);
        }
            
        return r;
    }

    
    public Result query(String sql, String field, String error) throws SQLException{
        LinkedHashMap<String,Object> map= new LinkedHashMap<>();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();


        if(rs.next()){
            r.setResult(true);
            map.put("message", rs.getObject(field));
            r.setMessage(map);
        }else{
            r=error(error);
        }
        return r;
    }




    private Result writeMapTreni(ResultSet rs, int i) throws SQLException{
        ArrayList<LinkedHashMap<String,Object>> listMap= new ArrayList<>();
        LinkedHashMap<String,Object> temp=new LinkedHashMap<>();
        if(i==1){//se è il getToken(Token);
            temp.put("token", rs.getString("token"));
            listMap.add(temp);
        }
        do{
            temp=new LinkedHashMap<>();
            temp.put("da", rs.getString("da"));
            temp.put("a",rs.getString("a"));
            temp.put("data", rs.getDate("dataOra"));
            listMap.add(temp);
        }while (rs.next());

        r.setMessage(listMap);
        return r;
    }

    public Result getViaggi(String sql, String error, int i) throws SQLException{
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();


        if(rs.next()){
            r=writeMapTreni(rs, i);   //map=writeMapTreniNoToken(rs); noToken -->0 ; Token--> 1
            r.setResult(true);
        }else{
            r=error(error);
        }
        return r;
    }

    public Result error(String message){
        LinkedHashMap<String,Object> map= new LinkedHashMap<>();
        r.setResult(false);
        map.put("message", message);
        r.setMessage(map);
        return r;
    }

}
