import java.io.*;

public class UILayer
{
	// User input
	static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	static DEAppLayer appLayer;
	
	public static void main(String[] args) throws ClassNotFoundException
	{
		// Create the data layer
		DEDataLayer dataLayer = new DEDataLayer();
		// Create the application layer and pass in the data layer
		appLayer = new DEAppLayer(dataLayer);
		
		try
		{
			// Loop until the program is exited
			while (true)
			{
				// Monitor the stock levels
				appLayer.monitorStock();
				
				// Display the user menu
				displayMenu();
				
				// Get user input
				int choice = Integer.parseInt(input.readLine());
				
				// Invoke procedure depending on the option selected
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
						// Apply a sale to a product
						applySale();
						break;
					case 4:
						// Purchase a product
						purchaseProduct();
						break;
					case 5:
						// Enrol a customer on a Loyalty Card scheme
						loyaltyCard();
						break;
					case 6:
						// Produce a Monthly report of purchases
						produceReport();
						break;
					default:
						// An incorrect value was entered
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
	
	// Display the user menu
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
		// Print the product details
		System.out.println("\nPRODUCT DETAILS");
		System.out.println("-----------------------------");
		System.out.println(result);
		System.out.println("\n");
	}
	
	// Change the price of a product
	private static void changePrice() throws IOException, ClassNotFoundException
	{
		// Prompt for the Product ID and read input
		System.out.print("\nEnter the ID of the Product you want to change the price of: ");
		String productID = input.readLine();
		
		// Prompt for the new price and read input
		System.out.print("\nEnter the new price of the product: ");
		String newPrice = input.readLine();
		
		// Invoke the ChangePrice procedure in the Application Layer, pass in the Product ID and New Price
		String result = appLayer.changePrice(productID, newPrice);
		
		// Display result
		System.out.println("\n" + result);
	}
	
	// Apply a Sale to a Product
	private static void applySale() throws IOException
	{
		// Prompt for the Product ID and read input
		System.out.print("\nEnter the ID of the Product you want to apply a sale to: ");
		String productID = input.readLine();
		
		// Display the options for Sales to the user and read input
		System.out.println("\nSelect the Sale to apply to this product: ");
		System.out.println("1) 3 for 2");
		System.out.println("2) Buy One Get One Free");
		System.out.println("3) Free Delivery Charge");
		System.out.print("\nEnter choice: ");
		String selectedSale = input.readLine();
		
		// Invoke the ApplySale procedure in the Application Layer, pass in the Product ID and the selected Sale
		String result = appLayer.applySale(productID, selectedSale);
		
		// Display result
		System.out.println("\n" + result);
	}
	
	// Purchase a Product
	private static void purchaseProduct() throws IOException
	{
		// Prompt for the Customer ID and read input
		System.out.print("\nEnter the ID of the Customer who is purchasing: ");
		int customerID = Integer.parseInt(input.readLine());
		
		// Prompt for the Product ID and read input
		System.out.print("\nEnter the ID of the product that is being purchased: ");
		int productID = Integer.parseInt(input.readLine());
		
		// Invoke the PurchaseProduct procedure in the Application Layer, pass in the Customer and Product IDs
		String result = appLayer.purchaseProduct(customerID, productID);
		
		// Display result
		System.out.println("\n" + result);
		
		// Prompt for if the customer wants to 'Buy Now, Pay Later' and read input
		System.out.println("\nDoes the customer wish to opt in to the 'Buy Now, Pay Later' scheme?");
		System.out.println("1) Yes");
		System.out.println("2) No");
		System.out.print("Enter choice: ");
		int choice = Integer.parseInt(input.readLine());
		
		switch (choice)
		{
		case 1:
			System.out.println("\nPlease visit the website Enabling, using the URL 'http://www.enabling.money/financing'");
			break;
		case 2:
			System.out.println("Customer not opted in.");
			break;
		default:
			// Incorrect value entered
			System.out.println("Invalid choice.");
			break;
		}
	}
	
	// Enrol a customer on the Loyalty Card scheme
	private static void loyaltyCard() throws IOException
	{
		// Prompt for the Product ID and read input
		System.out.print("\nEnter a Customer ID: ");
		int customerID = Integer.parseInt(input.readLine());
		
		// Invoke the LoyaltyCard procedure in the Application Layer, pass in the Customer ID
		String result = appLayer.loyaltyCard(customerID);
		
		// Display result
		System.out.println("\n" + result);
	}
	
	// Produce a report of the last month's purchases
	private static void produceReport() throws IOException
	{
		// Invoke the ProduceReport procedure in the Application Layer
		String result = appLayer.produceReport();
		
		// Display result
		System.out.println("\n" + result);
		
		// Prompt for the user to print the details of last 10 purchases and read answer
		System.out.println("\nPrint the details of the last 10 purchases?");
		System.out.println("1) Yes");
		System.out.println("2) No");
		System.out.print("Enter choice: ");
		int choice = Integer.parseInt(input.readLine());
		
		switch(choice)
		{
		case 1:
			// Print the details of the last 10 purchases
			appLayer.printPurchases();
			break;
		case 2:
			System.out.println("\n");
			break;
		default:
			System.out.println("\n");
			break;
		}
	}
}