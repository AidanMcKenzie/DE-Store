import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class DEAppLayer implements DEAppLayerInterface
{
	// The underlying data layer this application layer sits upon
	private DEDataLayerInterface dataLayer;
	
	/**
	 * Default constructor
	 * 
	 * @param dataLayer The data layer that this layer sits upon
	 */
	public DEAppLayer(DEDataLayerInterface dataLayer)
	{
		this.dataLayer = dataLayer;
	}
		
	// Get the details of a Product
	public String getProduct(String productID) 
	{
		String productSales;
		// Get the product's details and insert into a Product object
		Product product = dataLayer.getProduct(productID);
		
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
		
		// If the product exists, return the details
		if (product != null)
		{
			return "Product ID: " + product.getProductID() + "\n" + 
				   "Product Name: " + product.getProductName() + "\n" + 
				   "Price: £" + product.getPrice() + "\n" +
				   "Number of Items in Stock: " + product.getStock() + "\n" +
				   "Sales this Product is Included in: " + productSales;
		}
		else
		{
			// Else inform the user the product does not exist
			return "Product with the ID  " + productID + " does not exist";
		}
	}
	
	// Change the price of the Product
	public String changePrice(String productID, String newPrice) 
	{
		// Attempt an update of the database based on the supplied parameters
		boolean updateDB = dataLayer.changePrice(productID, newPrice);
		
		// If update was successful, notify the user
		if (updateDB)
		{
			return "Product Price Changed";
		}
		else
		{
			return "Product Price Not Changed";
		}
	}
	
	public String applySale(String productID, String selectedSale) 
	{
		if (selectedSale != "1" || selectedSale != "2" || selectedSale != "3")
		{
			return "A valid sale option must be selected";
		}
		else
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
	}
	
	public String monitorStock() throws IOException 
	{
		ArrayList<String> lowStockProducts = dataLayer.monitorStock();
		boolean outOfStock = dataLayer.outOfStock();
		
		if (outOfStock)
		{
			System.out.println("Out of Stock Items have been ordered in");
		}
		
		// Attempt an update of the database based on the supplied parameters
		if (lowStockProducts == null)
		{
			// Everything is fine
			return "";
		}
		else
		{
			// Alert user that product is low stock
			Iterator i = lowStockProducts.iterator();
	        System.out.println("The following products are low stock:");
		    while (i.hasNext()) 
		    {
		         System.out.println(i.next());
		    }
		}
		return null;		
	}
	
	
	public String purchaseProduct(int customerID, int productID) 
	{
		// Try and remove the Student from the data layer
		boolean success = dataLayer.purchaseProduct(customerID, productID);
		// Either we were successful or not.  Return appropriate message
		if (success)
		{
			return "Purchase confirmed!";
		}
		else
		{
			return "Purchase failed!";
		}
	}
	
	public String loyaltyCard(int customerID) throws IOException 
	{
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		// Check if customer is eligible for Loyalty card
		boolean success = dataLayer.loyaltyCard(customerID);
		// Either we were successful or not.  Return appropriate message
		if (success)
		{
			System.out.println("Customer is eligible for Loyalty Card");
			
			System.out.println("\nPlace customer on Loyalty Card? ");
			System.out.println("1) Yes");
			System.out.println("2) No");
			System.out.print("Enter choice: ");
			
			int choice = Integer.parseInt(input.readLine());
			
			switch (choice)
			{
			case 1:
				// Make call to database
				dataLayer.placeOnLoyaltyCard(customerID);
				return "Customer placed on Loyalty Card, and is now eligible for 10% off all purchases.";
			case 2:
				return "Customer not placed on Loyalty Card scheme.";
			default:
				// Another value was entered.  Display error message
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

	public String buyNowPayLater(int choice) 
	{
		switch (choice)
		{
		case 1:
			return "Now linking to Enabling website...";
		case 2:
			return "Customer not opted in.";
		default:
			// Another value was entered.  Display error message
			return "Invalid choice.";
		}
	}
	
	public String produceReport() 
	{
		HashMap<String, String> reportStatistics = dataLayer.produceReport();
		
		if (reportStatistics != null)
		{
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

}
