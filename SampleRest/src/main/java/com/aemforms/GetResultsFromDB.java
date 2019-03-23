package com.aemforms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetResultsFromDB {
	private DataSource dataSource;

	public Connection getConnection() throws ClassNotFoundException, SQLException {
		// Class.forName("com.mysql.jdbc.Driver");
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/leads", "root", "password");
		return con;

	}

	public JSONArray getResultSet(String sqlQuery) {
		System.out.println("The query is " + sqlQuery);
		JSONArray jasonArray = new JSONArray();
		Connection con;
		try {
			con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("id", rs.getInt(1));
				obj.put("data", new JSONObject(rs.getString(2)));
				System.out.println("The obj is " + obj.toString());
				jasonArray.put(obj);

			}
			con.close();
			return jasonArray;
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;

	}

}
