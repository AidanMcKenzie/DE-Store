import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class DEStoreDatabase 
{
	private Connection dbConnection = null;
	private String JDBC_DRIVER;
	private String DB_URL;
	private String USER;
	private String PASS;
	
	public DEStoreDatabase() 
	{
		
	
		connectToDB();

	}
	
	
	public boolean connectToDB() 
	{
		JDBC_DRIVER = "com.mysql.jdbc.Driver";
		DB_URL = "jdbc:mysql://localhost:3308/de_store";
		USER = "user";
		PASS = "user";
		
		try
		{  
			Class.forName("com.mysql.jdbc.Driver");  
			//System.out.println("Connecting to DEStore");
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3308/de_store?user=user&password=user");  
			System.out.println("Connected to DE Store database!");
			return true;
			
		} 
		catch(SQLException e)
		{ 
			JOptionPane.showMessageDialog(null, "Error connecting to Database - Please contact System Administrator", "Critical Error", 0);
			System.out.println(e);
			return false;
		}  
		catch(NullPointerException ex) 
		{
			ex.printStackTrace();
			return false;
		} 
		catch(ClassNotFoundException ex2) 
		{
			ex2.printStackTrace();
			return false;
		}
		
	}

	
	public void endConnection() 
	{
		try 
		{
			if(dbConnection != null) 
			{
				dbConnection.close();	
			}
		} 
		catch(SQLException e) 
		{
			e.printStackTrace();
		}
		
		System.out.println("Connection closed");
	}
	
	public  Connection getConnection() 
	{
		return dbConnection;
	}

	public static void main(String[] args) 
	{
		System.out.println("Printed line!!");
	}
	
}