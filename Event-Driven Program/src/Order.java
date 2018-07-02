/* Name: Kristopher Valas
 Course: CNT 4714 – Summer 2018
 Assignment title: Program 1 – Event-driven Programming
 Date: Tuesday May 29, 2018
*/

import java.util.ArrayList;

//This class maintains an order. An order can have multiple transactions and represents the order a customer places.
public class Order {

	public double taxRate = 0.06;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	
	//Iterates through the transaction array to get the cost of each book and totals it. 
	public double calculateSubTotal() {
		double subTotal = 0;
	
		for(int i = 0; i < transactions.size(); i++) {
			subTotal += transactions.get(i).getCost();
		}
		
		return subTotal;
	}
	
	public void addTransaction(Transaction t) {
		transactions.add(t);
	}
	
	public double calculateTax(double subTotal) {
		return subTotal * (taxRate);
	}
}
