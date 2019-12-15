import java.util.ArrayList;
import java.util.HashMap;

public interface DEDataLayerInterface 
{
	public Product getProduct(String productID);
	
	public boolean changePrice(String productID, String newPrice);
	
	public boolean applySale(String productID, String selectedSale);
	
	public ArrayList<String> monitorStock();

	public boolean outOfStock();

	public boolean purchaseProduct(int customerID, int productID);

	public boolean loyaltyCard(int customerID);

	public boolean placeOnLoyaltyCard(int customerID);

	public HashMap<String, String> produceReport();
}
