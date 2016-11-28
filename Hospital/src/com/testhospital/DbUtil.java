package com.testhospital;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class DbUtil {
	private static Connection connection = null;
	private static Properties prop = new Properties();
	private static InputStream input = null;
	private static final String PROPERTY_FILE = "property/parameter";
	private static ResourceBundle mailResource = ResourceBundle.getBundle(PROPERTY_FILE,Locale.US);
	
	private static javax.sql.DataSource ds = null;
	private static String dataSource = "ICMSDS";
	private static Logger logger = Logger.getLogger("DbUtil");
	public static Connection getConnection() {
		
		
		
		Context ctx = null;
		try {
			
			Hashtable ht = new Hashtable();
			ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			ht.put(Context.PROVIDER_URL, mailResource.getString("CONNECTION_URL"));
		
			ctx = new InitialContext(ht);
			if (ds == null) {
				ds = (javax.sql.DataSource) ctx.lookup(dataSource);
			}
		
			connection = ds.getConnection();
			
		} catch (NamingException e) {
			logger.info("Error looking datasource:" + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			logger.info("SQL Error:" + e.getMessage());
		}  finally {
			if(ctx != null)
				try {
					ctx.close();
				} catch (NamingException e1) {
					logger.info("Error closing context:" + e1.getMessage());
				}
		}
		
		return connection;
	}

	public static void closeConnection(Connection connection,
			ResultSet resultSet) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeConnection(Connection connection,
			ResultSet resultSet, Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null && resultSet != null) {
			closeConnection(connection, resultSet);
		}
	}
	
	
	
	public static void main(String[] args) {
		//System.out.println(distFromInMeters(29.1101f, 79.51269f, 29.21876f, 79.52979f));
		
		//getConnection() ; 
		//closeConnection(connection, null);
	}
}
