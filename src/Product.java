import java.util.ArrayList;
import java.util.Arrays;

public class Product 
{
	private String productID;
	private String productName;
	private String stock;
	private String price;
	private ArrayList<String> sales;
	
	public Product(String productID, String productName, String stock, String price, ArrayList<String> sales)
	{
		this.productID = productID;
		this.productName = productName;
		this.stock = stock;
		this.price = price;
		this.sales = sales;
	}
	
	public String getProductID()
	{
		return productID;
	}
	
	public String getProductName()
	{
		return productName;
	}
	
	public String getStock()
	{
		return stock;
	}
	
	public String getPrice()
	{
		return price;
	}
	
	public String getSales()
	{
		return Arrays.toString(sales.toArray());
	}
}