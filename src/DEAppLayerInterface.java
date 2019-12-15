public interface DEAppLayerInterface 
{	
	public String getProduct(String productID);
	
	public String changePrice(String productID, String newPrice);
	
	public String applySale(String productID, String selectedSale);
	
}
