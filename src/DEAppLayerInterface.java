import java.io.IOException;

public interface DEAppLayerInterface 
{	
	public String getProduct(String productID);
	
	public String changePrice(String productID, String newPrice) throws ClassNotFoundException, IOException;
	
	public String applySale(String productID, String selectedSale);	
}