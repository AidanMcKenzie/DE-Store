import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class DEAppLayer implements DEAppLayerInterface
{
	// Connection to Data Layer
	private DEDataLayerInterface dataLayer;
	
	public DEAppLayer(DEDataLayerInterface dataLayer)
	{
		this.dataLayer = dataLayer;
	}
		
	// Get the details of a Product
	public String getProduct(String productID) 
	{
		// Contains the name of the Sale
		String productSales;
		// Get the product's details and insert into a Product object
		Product product = dataLayer.getProduct(productID);
		
		// Depending on the integer found within the database, return the text of the appropriate sale
		if (product.getSales().contains("1"))
		{
			productSales = "3 for 2";
		}
		else if (product.getSales().contains("2"))
		{
			productSales = "Buy One Get One Free";
		}
		else if (product.getSales().contains("3"))
		{
			productSales = "Free Delivery Charge";
		}
		else
		{
			productSales = "None";
		}
		
		// Return the product details to the UI Layer
		return "Product ID: " + product.getProductID() + "\n" + 
			   "Product Name: " + product.getProductName() + "\n" + 
			   "Price: £" + product.getPrice() + "\n" +
			   "Number of Items in Stock: " + product.getStock() + "\n" +
			   "Sales this Product is Included in: " + productSales;
	}
	
	// Change the price of the Product
	public String changePrice(String productID, String newPrice) throws ClassNotFoundException, IOException 
	{
		// Attempt an update of the database based on the supplied parameters
		boolean updateDB = dataLayer.changePrice(productID, newPrice);
		
		// If update was successful, notify the user
		if (updateDB)
		{
			return "\nProduct Price Changed";
		}
		else
		{
			return "Product Price Not Changed";
		}
	}
	
	// Apply a Sale to a Product
	public String applySale(String productID, String selectedSale) 
	{
		System.out.println("Selected Sale" + selectedSale);
		
		// If a number other than the valid options is used, return an error message
		if (selectedSale.equals("1") || selectedSale.equals("2") || selectedSale.equals("3"))
		{
			// Attempt an update of the database based on the supplied parameters
			boolean updateDB = dataLayer.applySale(productID, selectedSale);
			
			// If update was successful, notify the user
			if (updateDB)
			{
				return "Sale Applied to Product";
			}
			else
			{
				return "Sale has not been Applied to Product";
			}
		}
		else
		{
			return "A valid sale option must be selected";
		}
	}
	
	// Monitor the Stock levels of the Products
	public String monitorStock() throws IOException 
	{
		// ArrayList of all the products that are low in stock
		// Run query on the database and insert the products into this ArrayList
		ArrayList<String> lowStockProducts = dataLayer.monitorStock();
		
		// Attempt a query on the database to see if there are items that are out of stock
		boolean outOfStock = dataLayer.outOfStock();
		
		// If there were out of stock items, notify the user that more items were ordered in
		if (outOfStock)
		{
			System.out.println("Out of Stock Items have been ordered in");
		}
		
		// If there were no low stock products, return nothing and continue as usual
		if (lowStockProducts == null)
		{
			return "";
		}
		else
		{
			// Alert user that product is low stock
			Iterator i = lowStockProducts.iterator();
			System.out.println("\nSTOCK WARNING");
	        System.out.println("The following products are low stock:");
	        
	        // Print all of the low stock products
		    while (i.hasNext()) 
		    {
		         System.out.println(i.next());
		    }
		}
		return null;		
	}
	
	// Purchase a product
	public String purchaseProduct(int customerID, int productID) 
	{
		// Attempt an update on the database and return a boolean
		boolean success = dataLayer.purchaseProduct(customerID, productID);
		
		// If the update was a success or failure, return the appropriate message to the UI Layer
		if (success)
		{
			return "Purchase confirmed!";
		}
		else
		{
			return "Purchase failed!";
		}
	}
	
	// Enrol customer on a Loyalty Card
	public String loyaltyCard(int customerID) throws IOException 
	{
		// Input from user
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		// Check if customer is eligible for Loyalty card
		boolean success = dataLayer.loyaltyCard(customerID);
		
		// If the customer is eligible for a loyalty card
		if (success)
		{
			// Alert this to the user
			System.out.println("Customer is eligible for Loyalty Card");
			
			// Prompt for user to make decision and read input
			System.out.println("\nPlace customer on Loyalty Card? ");
			System.out.println("1) Yes");
			System.out.println("2) No");
			System.out.print("Enter choice: ");
			int choice = Integer.parseInt(input.readLine());
			
			switch (choice)
			{
			case 1:
				// Update the database to reflect the change
				dataLayer.placeOnLoyaltyCard(customerID);
				return "Customer placed on Loyalty Card, and is now eligible for 10% off all purchases.";
			case 2:
				return "Customer not placed on Loyalty Card scheme.";
			default:
				// Invalid input detected
				System.out.println("\nInvalid choice");
				break;
			}
		}
		else
		{
			return "Customer is not eligible for Loyalty Card.";
		}
		return null;
	}

	// Allow user to 'Buy Now, Pay Later'
	public String buyNowPayLater(int choice) 
	{
		// Depending on the integer that was passed through, return a message to the user
		switch (choice)
		{
		case 1:
			return "Please visit the website Enabling, using the URL 'http://www.enabling.money/financing', and use the Transaction ID: " + "[INSERT]";
		case 2:
			return "Customer not opted in.";
		default:
			// Invalid input detected
			return "Invalid choice.";
		}
	}
	
	// Produce a report of purchases
	public String produceReport() 
	{
		// Query the database and store the results in a HashMap
		HashMap<String, String> reportStatistics = dataLayer.produceReport();
		
		// If there are statistics collected
		if (reportStatistics != null)
		{
			// Return the results in text form to the UI Layer
			return "MONTHLY REPORT\n" + "----------------------------\n" +
					"Total Number of Purchases in Last 30 Days: " + reportStatistics.get("purchases") + "\n" + 
					"Total Revenue in Last 30 Days: £" + reportStatistics.get("revenue") + "\n" + 
					"Most Popular Item in Last 30 Days: " + reportStatistics.get("popularItem");
		}
		else
		{
			return "Report could not be produced";
		}
	}

	// As part of producing the report, the user can optionally print the last 10 purchases
	public void printPurchases() 
	{
		// Run query on the database
		dataLayer.printPurchases();
	}
}