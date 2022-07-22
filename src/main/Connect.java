package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
	public int getPlayerValue(String objName) {
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/thegame","root","");
			Statement st = c.createStatement();
			String sqlQ = "Select Value from infoplayer where ObjectName = '" + objName + "'";
			ResultSet rs = st.executeQuery(sqlQ);
			if(rs.next()) return rs.getInt("Value");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	public void modifyPlayerValue(String objName, int newValue) {
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/thegame","root","");
			Statement st = c.createStatement();
			String sqlQ = "Update infoplayer set Value = " + Integer.toString(newValue) + " where ObjectName = '" + objName + "'";
			st.executeQuery(sqlQ);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
