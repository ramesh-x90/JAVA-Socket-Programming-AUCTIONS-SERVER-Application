package serverSrc;


// class of item


import java.util.HashSet;

public class Item {
	
	//properties
    private String symbol;
    private float price;
    private int security;
    private float profit;
    
    
    //current max price bider
    private String maxpricebider = "";
   
    //object of subscribers who subscribed to this item (bids)
    HashSet<Client> bid_subs = new HashSet<Client>();
    
  //object of subscribers who subscribed to this item (profits)
    HashSet<Client> prft_subs = new HashSet<Client>();

    public Item(String symbol , float price, int security, float profit){
    	this.symbol = symbol;
        this.price = price;
        this.security = security;
        this.profit = profit;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public float getPrice() {
    	return this.price;
		
    }
   
    
    public String getMaxpricebider() {
		return maxpricebider;
	}

	public void setMaxpricebider(String maxpricebider) {
		this.maxpricebider = maxpricebider;
	}

	public int getSecurity()
    {
    	return security;
    	
    }
    public void setPrice(float price){
    	this.price = price;
    }

    
	public HashSet<Client> getBid_subs() {
		return bid_subs;
	}

	public void addBid_subs(Client client) {
		this.bid_subs.add(client);
	}
	
	public HashSet<Client> getPrft_subs() {
		return prft_subs;
	}

	public void addPrft_subs(Client client) {
		this.prft_subs.add(client);
	}



	public float getProfit() {
		return profit;
	}

	public void setProfit(float profit) {
		this.profit = profit;
	}
	
	
	

	

}
