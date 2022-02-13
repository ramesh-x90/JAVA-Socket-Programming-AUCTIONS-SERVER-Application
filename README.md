# JAVA-Socket-Programming-AUCTIONS-SERVER-Application


## Stock auction
A stock auction is an auction through which different traders can purchase particular securities
(items) by bidding on them. Since the realistic stock auction process is quite complicated, you
are required to implement a simplified version of this auction similar to regular open auctions
(IPL, Ebay, etc.). The process is explained briefly below.
<br>
● Each item at the auction has a base price.
<br>
● Once the auction has started, the bidding process begins when a client makes a bid
higher than this base price.
<br>
● From that point onwards, each client can make a bid on an item by specifying a bid
higher than the previous bid. (similar to an open auction)
<br>
● Ultimately, the highest bidder wins the item.
<br>
● For simplicity, we assume that no two clients would bid on the same item within a time
frame of 500 ms.
<br>
● Auctions for different items occur in parallel (not in a one-by-one order of items)
<br>
● The bidding ends at the end of the day. However, if someone (trader) bids within the last
minute (60 sec), the bidding time for that particular item is increased by a minute.
○ Ie: If the highest bid of AAPL is $50 and someone bids $55 at 40 seconds before
the end of the day, bidding ends 60 seconds after on the next day if no more
bids.
<br>


## Description
The stock auction has 3 stakeholders, and each stakeholder has specific objectives which are
listed below.
<br>
● Auctioneer (Server) - The server functions as the auctioneer and must allow traders to
make bids on specific items.
<br>
● Traders (Client / Subscriber) - The traders should be able to make bids through the
auction server. They should also be able to get regular updates on changes to the
highest bid of the interested items, and other financial information such as profits.
<br>
● Companies (Publisher) - Companies must be able to inform the traders regarding
monthly profits.
<br>
# For this project you will implement both the client-server model and the publisher-subscriber
model. The server must perform 3 main tasks.
<br>
1. The client-model replicates the functionality of a stock auction. Clients must be able to
bid for different items at a stock auction through a server.
2. Companies must be able to publish profit information to the server and traders should be
able to get these updates by subscribing to particular topics.
3. Stakeholders should be able to get notification when bids are updated for an item
