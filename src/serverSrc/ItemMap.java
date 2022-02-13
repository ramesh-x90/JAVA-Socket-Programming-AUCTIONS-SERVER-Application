package serverSrc;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ItemMap extends ConcurrentHashMap<String, Item>{
	
	private static final long serialVersionUID = 1L;

	public ItemMap(int initialCapacity, float loadFactor, int concurrencyLevel )
	{
		
		super(initialCapacity , loadFactor , concurrencyLevel);
	}
	
	public ItemMap()
	{
		
		super();
	}
	
	
	//put items
	public Item put(String key , Item item)
	{
		return super.putIfAbsent(key, item);
	}
	
	//get price of specific item
	public float getPrice(String key)
	{
		
		Item item = super.get(key);
		return item.getPrice();
		
		
	}
	
	//update item
	public void update(String key , float value, String iD)
	{
		Item item = super.get(key);
		item.setPrice(value);
		super.putIfAbsent(key , item);
		item.setMaxpricebider(iD);
		item = null;
	}
	
	//get set of symbols
	public Set<String> getItems() {
		
		return super.keySet();
		
		
	}
	

}