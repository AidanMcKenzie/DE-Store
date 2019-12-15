import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.*;

public class DEDataLayer implements DEDataLayerInterface
{
	public DEDataLayer()
	{
	}
	
	public Connection startConnection()
	{
		try
		{
		// Load the driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		// First we need to establish a connection to the database
		Connection conn = DriverManager
						   .getConnection("jdbc:mysql://localhost:3308/de_store?user=user&password=user");
		// Next we create a statement to access the database
		
		return conn;
		}
		catch (ClassNotFoundException cnf)
		{
			System.err.println("Could not load driver");
			System.err.println(cnf.getMessage());
			System.exit(-1);
		}
		catch (SQLException sqe)
		{
			System.out.println("Error performing SQL Query");
			System.out.println(sqe.getMessage());
			System.exit(-1);
		}
		return null;
		
	}
	
	
	public Product getProduct(String productID) 
	{
		String name = null;
		String stock = null;
		String price = null;
		ArrayList<String> sales = new ArrayList<String>();
		
		Product thisProduct = null;
		
		try
		{
			Connection conn = startConnection();
			
			Statement statement = conn.createStatement();
			
			String query = "SELECT * FROM Products WHERE PRODUCT_ID = " + productID;

			ResultSet results = statement.executeQuery(query);

			while (results.next())
			{
				name = results.getString("PRODUCT_NAME");
				price = results.getString("PRICE");
				stock = results.getString("STOCK");
			}

			query = "SELECT * FROM sales WHERE PRODUCT_ID = " + productID;
			
			results = statement.executeQuery(query);
			
			while (results.next())
			{
				sales.add(results.getString("SALE_TYPE"));
			}
			
			statement.close();
			conn.close();
			
			thisProduct = new Product(productID, name, stock, price, sales);
			return thisProduct;
		}
		catch (SQLException sqe)
		{
			System.out.println("Error performing SQL Query");
			System.out.println(sqe.getMessage());
			System.exit(-1);
		}
		
		// If null, then return anyway
		return null;
	}
	
	public boolean changePrice(String productID, String newPrice) throws ClassNotFoundException, IOException 
	{	
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		BigDecimal newPriceNum = new BigDecimal(newPrice);
		
		System.out.println("\nOnly managers can control the price of a product");
		System.out.print("\nEnter username: ");
		String user = input.readLine();
		
		System.out.print("\nEnter password: ");
		String pass = input.readLine();
		
		System.out.print("\nThese are: " + user + " " + pass);
		
		if (user.equals("manager") && pass.equals("manager")) 
		{
			try
			{

				Class.forName("com.mysql.cj.jdbc.Driver");
				// First we need to establish a connection to the database
				Connection conn = DriverManager
								   .getConnection("jdbc:mysql://localhost:3308/de_store?user=user&password=user");
		
	
				Statement statement = conn
						.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
								ResultSet.CONCUR_UPDATABLE);
	
				String query = "SELECT * FROM products WHERE PRODUCT_ID = '"
						+ productID + "'";
	
				ResultSet results = statement.executeQuery(query);
	
				if (results.next()) {
					results.first();
					results.updateBigDecimal("PRICE", newPriceNum);
					results.updateRow();
					return true;
				}
				
			} 
			catch (SQLException sqe) 
			{
				System.err.println("Error in SQL Update");
				System.err.println(sqe.getMessage());
				System.exit(-1);
			}
		}
		else
		{
			System.out.println("Incorrect credentials.");
		}
		return false;
	}

	
	public boolean applySale(String productID, String selectedSale) 
	{	
		try
		{
			Connection conn = startConnection();

			// Create a new SQL statement
			Statement statement = conn.createStatement();
			// Build the INSERT statement
			String update = "INSERT INTO sales (PRODUCT_ID, SALE_TYPE) " +
							"VALUES ('" + productID + "', '" + selectedSale + "')";
			// Execute the statement
			statement.executeUpdate(update);
			// Release resources held by the statement
			statement.close();
			// Release resources held by the connection.  This also ensures that the INSERT completes
			conn.close();
			
			return true;
			
		} catch (SQLException sqe) {
			System.err.println("Error in SQL Update");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}

		return false;
	}
	
	
	public ArrayList<String> monitorStock() 
	{	
		ArrayList<String> lowStockProducts = new ArrayList<String>();
		try
		{
			Connection conn = startConnection();

			// Create a new SQL statement
			Statement statement = conn.createStatement();
			// Build the INSERT statement
			String query = "SELECT product_id FROM products WHERE stock < 5";
			// Execute the statement
			ResultSet results = statement.executeQuery(query);
			
			while (results.next())
			{
				lowStockProducts.add(getProductName(results.getInt("PRODUCT_ID")));
			}
					
			// Release resources held by the statement
			statement.close();
			// Release resources held by the connection.  This also ensures that the INSERT completes
			conn.close();
			
			return lowStockProducts;
			
		} catch (SQLException sqe) {
			System.err.println("Error in SQL Update");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}

		return null;
	}
	
	
	public boolean outOfStock() 
	{	
		try
		{
			Connection conn = startConnection();

			// Create a new SQL statement
			Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
													   ResultSet.CONCUR_UPDATABLE);
			// Build the INSERT statement
			String query = "SELECT product_id, stock FROM products WHERE stock = 0";
			// Execute the statement
			ResultSet results = statement.executeQuery(query);	
			
			if (results.next()) {
				// We will update the first hit (there should be only one)
				results.updateInt("STOCK", 20);
				// Update the row in the DB
				results.updateRow();
			} else {
				// No items are out of stock
				return false;
			}
			// Release resources held by the statement
			statement.close();
			// Release resources held by the connection.  This also ensures that the INSERT completes
			conn.close();
			
			return true;
			
		} 
		catch (SQLException sqe) 
		{
			System.err.println("Error in SQL Update");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}

		return false;
	}
	
	
	public boolean purchaseProduct(int customerID, int productID) 
	{
		try
		{
			Connection conn = startConnection();

			// Create a new SQL statement
			Statement statement = conn.createStatement();
			// Build the INSERT statement
			String sql = "UPDATE products SET stock = stock - 1 WHERE stock > 0 AND product_id = " + productID;
			// Execute the statement
			statement.executeUpdate(sql);	
			
			sql = "SELECT price FROM products WHERE product_id = " + productID;
			
			ResultSet results = statement.executeQuery(sql);
			
			int cost = 0;
			
			while (results.next())
			{
				cost = results.getInt("PRICE");
			}
			
			
			sql = "INSERT INTO transactions (PRODUCT_ID, CUSTOMER_ID, COST) " +
					"VALUES ('" + productID + "', '" + customerID + "', '" + cost + "')";
			// Execute the statement
			statement.executeUpdate(sql);
			
			// Release resources held by the statement
			statement.close();
			// Release resources held by the connection.  This also ensures that the INSERT completes
			conn.close();
			
			return true;
			
		} 
		catch (SQLException sqe) 
		{
			System.err.println("Error in SQL Update");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}

		return false;
	}
	
	
	
	public boolean loyaltyCard(int customerID) 
	{
		try
		{
			int noOfPurchases = 0;
			Connection conn = startConnection();

			// Create a new SQL statement
			Statement statement = conn.createStatement();
			// Build the INSERT statement
			String sql = "SELECT COUNT(transaction_id) FROM transactions WHERE customer_id = " + customerID;
			// Execute the statement
			ResultSet results = statement.executeQuery(sql);	
			
			while (results.next())
			{
				noOfPurchases = results.getInt("COUNT(transaction_id)");
			}
			
			System.out.println("No of Purchases: " + noOfPurchases);
			
			// Release resources held by the statement
			statement.close();
			// Release resources held by the connection.  This also ensures that the INSERT completes
			conn.close();
			
			if (noOfPurchases > 2)
			{
				return true;
			}
			else
			{
				return false;
			}
				
			
		} 
		catch (SQLException sqe) 
		{
			System.err.println("Error in SQL Query");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}

		return false;
	}
	
	public boolean placeOnLoyaltyCard(int customerID) 
	{
		try
		{
			Connection conn = startConnection();

			// Create a new SQL statement
			Statement statement = conn.createStatement();
			// Build the INSERT statement
			String sql = "UPDATE customers SET LOYALTY_CARD = 1 WHERE customer_id  = " + customerID;
			// Execute the statement
			statement.executeUpdate(sql);	
			
			statement.close();
			// Release resources held by the connection.  This also ensures that the INSERT completes
			conn.close();
			
			return true;
		}
		catch (SQLException sqe) 
		{
			System.err.println("Error in SQL Update");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}

		return false;
	}
	
	public HashMap<String, String> produceReport()
	{
		HashMap<String, String> reportStatistics = new HashMap<String, String>();
		
		int noOfPurchases = 0;
		int totalRevenue = 0;
		int  popularItem = 0;
		
		try
		{
			Connection conn = startConnection();
			// Create a new SQL statement
			Statement statement = conn.createStatement();
			// Build the INSERT statement
			String sql = "SELECT COUNT(TRANSACTION_ID) FROM transactions WHERE DATE_PURCHASED > NOW() - INTERVAL 1 MONTH";
			
			ResultSet results = statement.executeQuery(sql);	
			
			while (results.next())
			{
				noOfPurchases = results.getInt("COUNT(transaction_id)");
			}
			
			
			sql = "SELECT SUM(cost) FROM transactions WHERE DATE_PURCHASED > NOW() - INTERVAL 1 MONTH";
			
			results = statement.executeQuery(sql);	
			
			while (results.next())
			{
				totalRevenue = results.getInt("SUM(cost)");
			}
			

			sql = "SELECT MAX(mycount) FROM (select PRODUCT_ID, count(product_id) mycount from transactions group by product_id) AS subqueryalias";
			
			results = statement.executeQuery(sql);	
			
			while (results.next())
			{
				popularItem = results.getInt("MAX(mycount)");
			}
			
			statement.close();
			// Release resources held by the connection. This also ensures that the INSERT completes
			conn.close();
			
			reportStatistics.put("purchases", String.valueOf(noOfPurchases));
			reportStatistics.put("revenue", String.valueOf(totalRevenue));
			reportStatistics.put("popularItem", getProductName(popularItem));
			
			return reportStatistics;	
		}
		catch (SQLException sqe) 
		{
			System.err.println("Error in SQL Update");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}
		
		return null;
	}
	
	public String getProductName(int productID)
	{
		try
		{
			Connection conn = startConnection();
			// Create a new SQL statement
			Statement statement = conn.createStatement();
			// Build the INSERT statement
			String sql = "SELECT product_name FROM products WHERE product_id = " + productID;
			
			ResultSet results = statement.executeQuery(sql);	
			
			while (results.next())
			{
				return results.getString("product_name");
			}
		}
		catch (SQLException sqe) 
		{
			System.err.println("Error in SQL Update");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}
		return null;
	}

	public void printPurchases() 
	{
		try
		{
			Connection conn = startConnection();
			// Create a new SQL statement
			Statement statement = conn.createStatement();
			// Build the INSERT statement
			String sql = "SELECT TRANSACTION_ID, "
					   + "(SELECT PRODUCT_NAME FROM products where PRODUCT_ID = transactions.PRODUCT_ID) as Product, "
					   + "(SELECT NAME FROM customers where CUSTOMER_ID = transactions.CUSTOMER_ID) as Customer, "
					   + "COST, DATE_FORMAT(DATE_PURCHASED, \"%d/%m/%Y, %T\") as Date FROM transactions LIMIT 10";
			
			ResultSet results = statement.executeQuery(sql);	
			
			while (results.next())
			{
				System.out.println("-------------------------");
				System.out.println("TRANSACTION ID: " + results.getString("TRANSACTION_ID"));
				System.out.println("Product: " + results.getString("Product"));
				System.out.println("Customer: " + results.getString("Customer"));
				System.out.println("Cost: £" + results.getString("Cost"));
				System.out.println("Date Purchased: " + results.getString("Date"));
				System.out.println("-------------------------");
				System.out.println("\n");
			}
		}
		catch (SQLException sqe) 
		{
			System.err.println("Error in SQL Update");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}
		
	}
}
