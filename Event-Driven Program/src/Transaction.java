/* Name: Kristopher Valas
 Course: CNT 4714 – Summer 2018
 Assignment title: Program 1 – Event-driven Programming
 Date: Tuesday May 29, 2018
*/

//This class contains the book and all the information pertaining to the order it is apart of. A transaction is associated to an order. 


//Transaction class that keeps track of each book inside an order.
public class Transaction {
	private Book book;
	private int quantity;
	private int count;
	private double discount = 0;
	
	public Transaction(Book book, int count, int quantity) {
		this.book = book;
		this.count = count;
		this.quantity = quantity;
		checkDiscount();
	}
		
	//Returns the cost of the book with any discount applied
	public double getCost() {
		return (book.cost * quantity) * (1 - discount);
	}
	
	public double getDiscount() {
		return discount;
	}
	
	//Formats the book for printing to text file and order summary.
	public String getInfo() {
		String output = String.format("%d %s $%.2f %d %.0f%% $%.2f", book.id, book.name, book.cost, quantity, (discount * 100), getCost());
		return output;
	}
	
	//A discount is applied if a customers order has 5 or more books.
	private void checkDiscount() {
		if(count >= 5 && count <= 9) {
			discount = 0.1;
		}else if (count >= 10 && count <= 14) {
			discount = 0.15;
		}else if(count >= 15) {
			discount = 0.2;
		}
	}

}
