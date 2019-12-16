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
	
	// Method for establishing a database connection
	public Connection startConnection()
	{
		try
		{
			// Load driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// Establish connection to the database using JBDC and inputting the username and password
			Connection conn = DriverManager
							   .getConnection("jdbc:mysql://localhost:3308/de_store?user=user&password=user");
			
			// Return the connection to the methods that call this method
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
	
	// Get details for a Product
	public Product getProduct(String productID) 
	{
		// Declare variables
		String name = null;
		String stock = null;
		String price = null;
		ArrayList<String> sales = new ArrayList<String>();
		
		// Product object that will be passed back to Application Layer
		Product thisProduct = null;
		
		try
		{
			// Establish a connection to the database
			Connection conn = startConnection();
			
			// Create a SQL statement to run on the database
			Statement statement = conn.createStatement();
			
			// Query database for the product that matches the supplied Product ID
			String query = "SELECT * FROM Products WHERE PRODUCT_ID = " + productID;

			// Execute the SQL query
			ResultSet results = statement.executeQuery(query);

			// For every returned record (should only be one)
			while (results.next())
			{
				// Populate variables with the database values
				name = results.getString("PRODUCT_NAME");
				price = results.getString("PRICE");
				stock = results.getString("STOCK");
			}

			// Query database for the sales that matches the supplied Product ID
			query = "SELECT * FROM sales WHERE PRODUCT_ID = " + productID;
			
			// Execute the SQL query
			results = statement.executeQuery(query);
			
			// For every returned record
			while (results.next())
			{
				// Add to the ArrayList
				sales.add(results.getString("SALE_TYPE"));
			}
			
			// Close connection
			statement.close();
			conn.close();
			
			// Create the new Product object and pass it back to the Application Layer
			thisProduct = new Product(productID, name, stock, price, sales);
			return thisProduct;
		}
		catch (SQLException sqe)
		{
			System.out.println("Error performing SQL Query");
			System.out.println(sqe.getMessage());
			System.exit(-1);
		}
		return null;
	}
	
	// Change the price of a Product
	public boolean changePrice(String productID, String newPrice) throws ClassNotFoundException, IOException 
	{	
		// User input
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		// New price of the Product
		BigDecimal newPriceNum = new BigDecimal(newPrice);
		
		// Require the manager login (username and password
		System.out.println("\nOnly managers can control the price of a product");
		System.out.print("\nEnter username: ");
		String user = input.readLine();
		System.out.print("\nEnter password: ");
		String pass = input.readLine();
		
		// If the manager' credentials are correct, proceed to the main body of code
		if (user.equals("manager") && pass.equals("manager")) 
		{
			try
			{
				// Establish a connection to the database
				Connection conn = startConnection();
	
				// Create a SQL statement to run on the database
				Statement statement = conn
						.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
								ResultSet.CONCUR_UPDATABLE);
				
				// Query database for the product that matches the supplied Product ID
				String query = "SELECT * FROM products WHERE PRODUCT_ID = '"
						+ productID + "'";
	
				// Execute the SQL query
				ResultSet results = statement.executeQuery(query);
	
				// For every returned record (should only be one)
				if (results.next()) 
				{
					// Update the PRICE column of the record to reflect the new price
					results.first();
					results.updateBigDecimal("PRICE", newPriceNum);
					results.updateRow();
					
					// Return to the Application Layer
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
		// Else the manager credentials were incorrect, block access
		else
		{
			System.out.println("Incorrect credentials.");
		}
		return false;
	}

	// Apply a sale to a product
	public boolean applySale(String productID, String selectedSale) 
	{	
		try
		{
			// Establish a connection to the database
			Connection conn = startConnection();

			// Create a SQL statement to run on the database
			Statement statement = conn.createStatement();
			
			// Insert record into the SALES table with the product information and selected sale
			String update = "INSERT INTO sales (PRODUCT_ID, SALE_TYPE) " +
							"VALUES ('" + productID + "', '" + selectedSale + "')";
			
			// Execute the update
			statement.executeUpdate(update);
			
			// Close connection
			statement.close();
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
	
	// Monitor the stock of the products
	public ArrayList<String> monitorStock() 
	{	
		// ArrayList that will contain the low stock products
		ArrayList<String> lowStockProducts = new ArrayList<String>();
		
		try
		{
			// Establish a connection to the database
			Connection conn = startConnection();

			// Create a SQL statement to run on the database
			Statement statement = conn.createStatement();
			
			// Query database for products that have less than 5 stock
			String query = "SELECT product_id FROM products WHERE stock < 5";
			
			// Execute the SQL query
			ResultSet results = statement.executeQuery(query);
			
			// For every returned record
			while (results.next())
			{
				// Add to the LowStockProducts ArrayList
				lowStockProducts.add(getProductName(results.getInt("PRODUCT_ID")));
			}
					
			// Close connection
			statement.close();
			conn.close();
			
			return lowStockProducts;	
		} 
		catch (SQLException sqe) 
		{
			System.err.println("Error in SQL Update");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}

		return null;
	}
	
	// Monitor for out of stock products and order new items in
	public boolean outOfStock() 
	{	
		try
		{
			// Establish a connection to the database
			Connection conn = startConnection();

			// Create a SQL statement to run on the database
			Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
													   ResultSet.CONCUR_UPDATABLE);
			
			// Query database for products and their stock that have 0 stock
			String query = "SELECT product_id, stock FROM products WHERE stock = 0";
			
			// Execute the SQL query
			ResultSet results = statement.executeQuery(query);	
			
			// For every returned record
			if (results.next()) 
			{
				// Change stock from 0 to 20 (order 20 more items in)
				results.updateInt("STOCK", 20);
				// Update the row in the DB
				results.updateRow();
			} 
			else 
			{
				// No items are out of stock, so return to the Application Layer
				return false;
			}
			
			// Close connection
			statement.close();
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
	
	// Purchase a product
	public boolean purchaseProduct(int customerID, int productID) 
	{
		try
		{
			// Used to format prices into two decimal places
			DecimalFormat decimals = new DecimalFormat("#.##");
			
			// Declare the variables for the costs
			double cost = 0;
			double deliveryCharge;
			
			// Establish a connection to the database
			Connection conn = startConnection();

			// Create a SQL statement to run on the database
			Statement statement = conn.createStatement();
			
			// Update the product record and reduce the stock level by 1, where the Product ID matches the passed value
			String sql = "UPDATE products SET stock = stock - 1 WHERE stock > 0 AND product_id = " + productID;
			
			// Execute the update
			statement.executeUpdate(sql);	
			
			// Query database for the price of the Product
			sql = "SELECT price FROM products WHERE product_id = " + productID;
			
			// Execute the SQL query
			ResultSet results = statement.executeQuery(sql);
			
			// For every returned record (should only be one)
			while (results.next())
			{
				// Set cost variable to the the fetched PRICE value
				cost = results.getDouble("PRICE");
			}
			
			// Print out the normal price of the product
			System.out.println("\n\nProduct Price: £" + decimals.format(cost));
			
			// Query database for if the customer has a loyalty card
			sql = "SELECT loyalty_card FROM customers WHERE customer_id = " + customerID;
			
			// Execute the SQL query
			results = statement.executeQuery(sql);
			
			// For every returned record (should only be one)
			while (results.next())
			{
				// If the customer does have a loyalty card
				if (results.getInt("loyalty_card") == 1)
				{
					// The 10% discount is applied to the total purchase cost
					cost = cost*0.9;
					
					// Print the new discounted cost
					System.out.println("\nCustomer is applied on Loyalty Card scheme. Total price reduced by 10%.");
					System.out.println("\nPrice after Loyalty Discount: £" + decimals.format(cost));
				}
			}
			
			// Additional delivery charge is 5% of the product price, and is printed
			deliveryCharge = cost*0.05;
			System.out.println("\nDelivery Charge: £" + decimals.format(deliveryCharge));
			
			// Delivery charge is added to total cost
			cost = cost + deliveryCharge;
			
			// Print the total cost to the user
			System.out.println("\nTOTAL COST: £" + decimals.format(cost));
			
			// Insert record into TRANSACTIONS table that includes the product bought, the customer who bought it and the final cost
			sql = "INSERT INTO transactions (PRODUCT_ID, CUSTOMER_ID, COST) " +
					"VALUES ('" + productID + "', '" + customerID + "', '" + cost + "')";
			
			// Execute the update
			statement.executeUpdate(sql);
			
			// Close connection
			statement.close();
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
	
	// Check for customer's eligibility for a loyalty card
	public boolean loyaltyCard(int customerID) 
	{
		try
		{
			// Declare variables
			int noOfPurchases = 0;
			int customerHasCard = 0;
			
			// Establish a connection to the database
			Connection conn = startConnection();

			// Create a SQL statement to run on the database
			Statement statement = conn.createStatement();
			
			// Query database for the number of purchases that a customer has made
			String sql = "SELECT COUNT(transaction_id) FROM transactions WHERE customer_id = " + customerID;
			
			// Execute the SQL query
			ResultSet results = statement.executeQuery(sql);	
			
			// For every returned record (should only be one)
			while (results.next())
			{
				// Total number of purchases a customer has ever made
				noOfPurchases = results.getInt("COUNT(transaction_id)");
			}
				
			// Query database to check is customer is already enrolled for a loyalty card
			sql = "SELECT loyalty_card FROM customers WHERE customer_id = " + customerID;
			
			// Execute the SQL query
			results = statement.executeQuery(sql);	
			
			// For every returned record (should only be one)
			while (results.next())
			{
				// This variable will contain the answer to if the cusotmer already has a loyalty card
				customerHasCard = results.getInt("loyalty_card");
			}
			
			// Close connection
			statement.close();
			conn.close();
			
			// If the customer has made at least three purchases
			if (noOfPurchases > 2)
			{
				// And if the customer does not already have a loyalty card
				if (customerHasCard != 1)
				{
					// Customer is eligible for a loyalty card
					return true;
				}
				else
				{
					System.out.println("\nCustomer is already enrolled on the Loyalty Card scheme.");
					return false;
				}
			}
			else
			{
				// Customer does not have enough purchases to qualify
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
	
	// Place customer on a loyalty card
	public boolean placeOnLoyaltyCard(int customerID) 
	{
		try
		{
			// Establish a connection to the database
			Connection conn = startConnection();

			// Create a SQL statement to run on the database
			Statement statement = conn.createStatement();
			
			// Update the customer record to reflect their enrolment on the loyalty card
			String sql = "UPDATE customers SET LOYALTY_CARD = 1 WHERE customer_id  = " + customerID;
			
			// Execute the update
			statement.executeUpdate(sql);	
			
			// Close connection
			statement.close();
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
	
	// Produce a report of purchase information
	public HashMap<String, String> produceReport()
	{
		// HashMap containing report statistics
		HashMap<String, String> reportStatistics = new HashMap<String, String>();
		
		// Declare variables
		int noOfPurchases = 0;
		int totalRevenue = 0;
		int popularItem = 0;
		
		try
		{
			// Establish a connection to the database
			Connection conn = startConnection();
			
			// Create a SQL statement to run on the database
			Statement statement = conn.createStatement();
			
			// Query database for the number of transactions in the last month
			String sql = "SELECT COUNT(TRANSACTION_ID) FROM transactions WHERE DATE_PURCHASED > NOW() - INTERVAL 1 MONTH";
			
			// Execute the SQL query
			ResultSet results = statement.executeQuery(sql);	
			
			// For every returned record (should only be one)
			while (results.next())
			{
				// Set the number of transaction in the last month
				noOfPurchases = results.getInt("COUNT(transaction_id)");
			}
			
			// Query database for the total revenue in the last month
			sql = "SELECT SUM(cost) FROM transactions WHERE DATE_PURCHASED > NOW() - INTERVAL 1 MONTH";
			
			// Execute the SQL query
			results = statement.executeQuery(sql);	
			
			// For every returned record (should only be one)
			while (results.next())
			{
				// Set the total revenue in the last month
				totalRevenue = results.getInt("SUM(cost)");
			}
			
			// Query database for the most popular item in the database
			sql = "SELECT MAX(mycount) FROM (select PRODUCT_ID, count(product_id) mycount from transactions group by product_id) AS subqueryalias";
			
			// Execute the SQL query
			results = statement.executeQuery(sql);	
			
			// For every returned record (should only be one)
			while (results.next())
			{
				// Set the most popular item in the database
				popularItem = results.getInt("MAX(mycount)");
			}
			
			// Close connection
			statement.close();
			conn.close();
			
			// Insert the collected information into the statistics HashMap
			reportStatistics.put("purchases", String.valueOf(noOfPurchases));
			reportStatistics.put("revenue", String.valueOf(totalRevenue));
			reportStatistics.put("popularItem", getProductName(popularItem));
			
			// Return the HashMap
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
	
	// Get the name of the product from its ID
	public String getProductName(int productID)
	{
		try
		{
			// Establish a connection to the database
			Connection conn = startConnection();
			
			// Create a SQL statement to run on the database
			Statement statement = conn.createStatement();
			
			// Query database for the product name that matches the supplied Product ID
			String sql = "SELECT product_name FROM products WHERE product_id = " + productID;
			
			// Execute the SQL query
			ResultSet results = statement.executeQuery(sql);	
			
			// For every returned record (should only be one)
			while (results.next())
			{
				// The product name
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

	// Print the last 10 purchases made
	public void printPurchases() 
	{
		try
		{
			// Establish a connection to the database
			Connection conn = startConnection();
			
			// Create a SQL statement to run on the database
			Statement statement = conn.createStatement();
			
			// Query database for the details of the last 10 purchases, including the product, customer and date purchased
			String sql = "SELECT TRANSACTION_ID, "
					   + "(SELECT PRODUCT_NAME FROM products where PRODUCT_ID = transactions.PRODUCT_ID) as Product, "
					   + "(SELECT NAME FROM customers where CUSTOMER_ID = transactions.CUSTOMER_ID) as Customer, "
					   + "COST, DATE_FORMAT(DATE_PURCHASED, \"%d/%m/%Y, %T\") as Date FROM transactions ORDER BY Date DESC LIMIT 10";
			
			// Execute the SQL query
			ResultSet results = statement.executeQuery(sql);	
			
			// For every returned record
			while (results.next())
			{
				// Print the transaction details
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