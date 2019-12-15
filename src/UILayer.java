import java.io.*;

public class UILayer
{
	// As other methods will be accessing these, make them globally accessible
	static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	static DEAppLayer appLayer;
	
	public static void main(String[] args)
	{
		// Create the data layer
		DEDataLayer dataLayer = new DEDataLayer();
		// Create the application layer, passing in the data layer
		appLayer = new DEAppLayer(dataLayer);
		
		try
		{
			// Loop until programme is exited (or an exception occurs)
			while (true)
			{
				// Monitor the stock
				appLayer.monitorStock();
				
				// Display the user menu
				displayMenu();
				
				// Get the user input
				int choice = Integer.parseInt(input.readLine());
				// Behave based on the user input
				switch (choice)
				{
					case 1:
						// Fetch details for a product
						getProductDetails();
						break;
					case 2:
						// Change the Price of a Product
						changePrice();
						break;
					case 3:
						applySale();
						break;
					case 4:
						purchaseProduct();
						break;
					case 5:
						loyaltyCard();
						break;
					case 6:
						produceReport();
						break;
					default:
						// Another value was entered.  Display error message
						System.out.println("\n");
						break;
				}
				System.out.println();
			}
		}
		catch (IOException ioe)
		{
			System.err.println("Error in I/O");
			System.err.println(ioe.getMessage());
			System.exit(-1);
		}
	}
	
	/**
	 * Displays the user menu
	 */
	private static void displayMenu()
	{
		System.out.println("----------------------------------------");
		System.out.println("DE Store - Distributed Management System");
		System.out.println("----------------------------------------");
		System.out.println("");
		System.out.println("1) Get Product Details");
		System.out.println("2) Price Control");
		System.out.println("3) Apply Sale to a Product");
		System.out.println("4) Purchase Product");
		System.out.println("5) Loyalty Card");
		System.out.println("6) Reports and Analysis");
		System.out.print("\nEnter choice: ");
	}
	
		
	// Get the details of a Product
	private static void getProductDetails() throws IOException
	{
		// Prompt for the Product ID and read input
		System.out.print("\nProduct ID: ");
		String productID = input.readLine();
		
		// Fetch the product's details based on the ID
		String result = appLayer.getProduct(productID);
		System.out.println("\nPRODUCT DETAILS");
		System.out.println("-----------------------------");
		System.out.println(result);
		System.out.println("\n");
	}
	
	private static void changePrice() throws IOException
	{
		
		// Prompt and get the Product ID
		System.out.print("\nEnter the ID of the Product you want to change the price of: ");
		String productID = input.readLine();
		
		System.out.print("\nEnter the new price of the product: ");
		String newPrice = input.readLine();
		
		String result = appLayer.changePrice(productID, newPrice);
		// Display result
		System.out.println("\n" + result);
	}
	
	private static void applySale() throws IOException
	{
		
		// Prompt and get the Product ID
		System.out.print("\nEnter the ID of the Product you want to apply a sale to: ");
		String productID = input.readLine();
		
		System.out.println("\nSelect the Sale to apply to this product: ");
		System.out.println("1) 3 for 2");
		System.out.println("2) Buy One Get One Free");
		System.out.println("3) Free Delivery Charge");
		System.out.print("\nEnter choice: ");
		String selectedSale = input.readLine();
		
		String result = appLayer.applySale(productID, selectedSale);
		// Display result
		System.out.println("\n" + result);
	}
	

	private static void purchaseProduct() throws IOException
	{
		
		// Prompt and get the Product ID
		System.out.print("\nEnter the ID of the Customer who is purchasing: ");
		int customerID = Integer.parseInt(input.readLine());
		
		System.out.print("\nEnter the ID of the product that is being purchased: ");
		int productID = Integer.parseInt(input.readLine());
		
		String result = appLayer.purchaseProduct(customerID, productID);
		// Display result
		System.out.println("\n" + result);
		
		System.out.println("\nDoes the customer wish to opt in to the 'Buy Now, Pay Later' scheme?");
		System.out.println("1) Yes");
		System.out.println("2) No");
		System.out.print("Enter choice: ");
		int choice = Integer.parseInt(input.readLine());
		
		appLayer.buyNowPayLater(choice);
	}
	
	
	private static void loyaltyCard() throws IOException
	{
		// Prompt and get the Product ID
		System.out.print("\nEnter a Customer ID: ");
		int customerID = Integer.parseInt(input.readLine());
		
		String result = appLayer.loyaltyCard(customerID);
		// Display result
		System.out.println("\n" + result);
	}
	
	private static void produceReport() throws IOException
	{
		String result = appLayer.produceReport();
		// Display result
		System.out.println("\n" + result);
	}

}
