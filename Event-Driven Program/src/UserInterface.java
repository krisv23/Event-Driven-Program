/* Name: Kristopher Valas
 Course: CNT 4714 – Summer 2018
 Assignment title: Program 1 – Event-driven Programming
 Date: Tuesday May 29, 2018
*/
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UserInterface extends JFrame implements ActionListener  {

	//Variables used throughout different functions inside the class.
	private static ArrayList<Book> bookLibrary;
	private int userBookID;
	private int userQuantity;
	private int userNumItems;
	private int count = 1;
	private int bookCount = 0;
	private Transaction newT;
	private Order newO;
	private Book currentBook;
	
	public static void main(String[] args) {
		
		int bookID;
		String bookName;
		double bookCost;
		
		File file = new File("inventory.txt");
		Scanner scan;
		
		//Scan's the inventory text file in and store's all book objects into an array for future use.
		try {
			scan = new Scanner(file);
			bookLibrary = new ArrayList<Book>();
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] lineArray = line.split(",");
				bookID = Integer.parseInt(lineArray[0]);
				bookName = lineArray[1];
				bookCost = Double.parseDouble(lineArray[2]);				
				Book newBook = new Book(bookID, bookName, bookCost);
				bookLibrary.add(newBook);
				
			}
			scan.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		UserInterface gui = new UserInterface();
		
	}
	
	//Iterates through the library and determines if the ID exists. 
	public Book checkBookID(int id) {
		for(int i = 0; i < bookLibrary.size(); i++) {
			if (bookLibrary.get(i).id == id) {
				return bookLibrary.get(i);
			}
		}
		return null;
	}
		
	public UserInterface() {
		initComponents();
	}
	
	
//Event listener that handles all button events.
	public void actionPerformed(ActionEvent e) {
		
		//Updates the UI and creates a new transaction based on the book ID provided in the ID field. Also verifies that the BookID entered is correct.
		if(e.getSource() == process) {
			
			if(bookIDTxt.getText() == null) {
				JOptionPane.showMessageDialog(frame, "Please enter a valid Book ID ");
			} else {
				userBookID = Integer.parseInt(bookIDTxt.getText());
				currentBook = checkBookID(userBookID);
			}
			
			if(currentBook != null) {
				process.setEnabled(false);
				confirm.setEnabled(true);
				numItemsTxt.setEnabled(false);
				
				userNumItems = Integer.parseInt(numItemsTxt.getText());
				userQuantity = Integer.parseInt(quantityTxt.getText());
				
				bookCount += userQuantity;
				newT = new Transaction(currentBook, bookCount, userQuantity);
				
				infoTxt.setText(newT.getInfo());
				info.setText("Item# " + count + " info:");
			
			}else {
				JOptionPane.showMessageDialog(frame, "Book ID is not found in file, Please try again. ");
				quantityTxt.setText("");
				bookIDTxt.setText("");
			}
		}
		
		//Updates the UI and creates a new order (if one does not exist) and adds the transaction created from the process step. 
		if(e.getSource() == confirm) {
			
			JOptionPane.showMessageDialog(frame, "Item #" + (count) + " accepted.");
			if(newO == null) {
				newO = new Order();
			}
			newO.addTransaction(newT);
			subtotalTxt.setText("$" + String.format("%.2f",newO.calculateSubTotal()));
			count++;
			//Check if the order is filled
			if(count > userNumItems) {
				closeOrder();
			}else {
				processNextItem();
			}	
		}
		//Displays the current order via viewOrder function
		if(e.getSource() == view) {
			viewOrder();
		}
		//Completes the order via finishOrder function
		if(e.getSource() == finish) {
			finishOrder();
		}
		//Updates the UI and prepares application for a newOrder. 
		if(e.getSource() == newOrder) {
			count = 1;
			newO = null;
			processNextItem();
			numItemsTxt.setEnabled(true);
			bookIDTxt.setEnabled(true);
			quantityTxt.setEnabled(true);
			numItemsTxt.setText("");
			view.setEnabled(false);
			finish.setEnabled(false);
			subtotalTxt.setText("");
			infoTxt.setText("");
			info.setText("Item #1 info:");
		}
		if(e.getSource() == exit) {
			System.exit(0);
		}
	}
	
	//Prepares the UI for another user entered item.
	private void processNextItem() {
		subtotal.setText("Order subtotal for " + (count) + " item(s):");
		bookID.setText("Enter Book ID for Item #" + count + ":");
		quantity.setText("Enter quantity for Item #" + count + ":");
		
		subtotal.setText("Order subtotal for " + (count) + " item(s):");
		bookIDTxt.setText("");
		quantityTxt.setText("");
		process.setText("Process Item #" + count);
		confirm.setText("Confirm Item #" + count);
		process.setEnabled(true);
		confirm.setEnabled(false);
		view.setEnabled(true);
		finish.setEnabled(true);
	}
	
	//Updates the UI to display an order that has been closed.
	private void closeOrder() {
		bookID.setText("");
		bookIDTxt.setText("");
		quantity.setText("");
		quantityTxt.setText("");
		bookIDTxt.setEnabled(false);
		quantityTxt.setEnabled(false);
		confirm.setEnabled(false);
		view.setEnabled(true);
		finish.setEnabled(true);
		
	}
	
	//Display's all items associated with the current transaction to a dialog box.
	private void viewOrder( ) {
		String output = new String();
		for(int i = 1; i <= newO.transactions.size(); i++) {
			 output += (i + ". " + newO.transactions.get(i-1).getInfo() + "\n");
		}
		JOptionPane.showMessageDialog(frame, output);
	}
	
	//Print's out all items associated with the current transaction to a file.
	private void printTransaction() {
		String output;
		try {
			BufferedWriter textOut = new BufferedWriter(new FileWriter("transactions.txt", true));
			for(int i = 1; i <= newO.transactions.size(); i++) {
				 output = (getDateTime("yyMMddyyhhmm") + ", " + newO.transactions.get(i-1).getInfo() + ", " + getDateTime("M/dd/yyyy hh:mm:ss z") + "\n");
				 textOut.write(output);
				 
			}
			textOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	//Prepares a dialog box with complete order details and writes all transactions that occurred during application run to a file. 
	private void finishOrder( ) {
		String output = new String();
		String space  = new String();
		double subtotal = newO.calculateSubTotal();
		double taxAmount = newO.calculateTax(subtotal);
		space = "\n\n";
		output = "Date: " + getDateTime("M/dd/yyyy hh:mm:ss z");
		output += space;
		output += ("Number of line Items: " + (count - 1));
		output += space;
		output += ("Item# / ID / Title / Price / Qty / Disc% / Subtotal");
		output += space;
		for(int i = 1; i <= newO.transactions.size(); i++) {
			 output += (i + ". " + newO.transactions.get(i-1).getInfo() + "\n");
		}
		output += space;
		output += ("Order subtotal: " + String.format("%.2f", subtotal));
		output += space;
		output += ("Tax Rate: " + String.format("%.2f", newO.taxRate * 100) + "%");
		output += space;
		output += ("Tax amount: $" + String.format("%.2f", taxAmount));
		output += space;
		output += ("Order Total: $" + String.format("%.2f", (subtotal + taxAmount)));
		output += space;
		output += "Thanks for shopping at the Valas Bookstore!";
		JOptionPane.showMessageDialog(frame, output);
		printTransaction();
	}
	
	//Given a string format as a parameter, this function returns a dateTime as a string.
	private String getDateTime(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = new Date();
		return formatter.format(date);
	}
	
	//UI variables and initialization methods.
	
	
	private JFrame frame;
	private JPanel panel;
	private JPanel topPanel;

	
	private JButton process;
	private JButton confirm;
	private JButton	view;
	private JButton finish;
	private JButton newOrder;
	private JButton exit;
	
	private JLabel numItems;
	private JLabel bookID;
	private JLabel quantity;
	private JLabel info;
	private JLabel subtotal;
	
	private JTextField numItemsTxt;
	private JTextField bookIDTxt;
	private JTextField quantityTxt;
	private JTextField infoTxt;
	private JTextField subtotalTxt;
	
	//Prepares and presents the UI
	private void initComponents() {
		
		//frame set up
		frame = new JFrame("Valas Bookstore");
		frame.setSize(700, 300);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		Container pane = frame.getContentPane();
		//pane.setBackground(Color.GREEN);
		
		//Panel set up
		
		panel = new JPanel();
		panel.setBackground(Color.blue);
		
		
		topPanel = new JPanel();
		topPanel.setBackground(Color.green);
		
		
		//Button initializers
		process = new JButton("Process Item #1");
		process.setPreferredSize(new Dimension(130,25));
		process.addActionListener(this);
		panel.add(process);
		
		confirm = new JButton("Confirm Item #1");
		confirm.setPreferredSize(new Dimension(130,25));
		confirm.setEnabled(false);
		confirm.addActionListener(this);
		panel.add(confirm);
		
		
		view = new JButton("View Order");
		view.setPreferredSize(new Dimension(100,25));
		view.setEnabled(false);
		view.addActionListener(this);
		panel.add(view);
		
		finish = new JButton("Finish Order");
		finish.setPreferredSize(new Dimension(110,25));
		finish.setEnabled(false);
		finish.addActionListener(this);
		panel.add(finish);
		
		newOrder = new JButton("New Order");
		newOrder.setPreferredSize(new Dimension(100,25));
		newOrder.addActionListener(this);
		panel.add(newOrder);
		
		exit = new JButton("Exit");
		exit.setPreferredSize(new Dimension(60,25));
		exit.addActionListener(this);
		panel.add(exit);
		
		numItems = new JLabel("Enter number of items in this order:");
		numItems.setPreferredSize(new Dimension(200,25));
		numItemsTxt = new JTextField(30);
		topPanel.add(numItems, BorderLayout.CENTER);
		topPanel.add(numItemsTxt, BorderLayout.LINE_END);
		
		bookID = new JLabel("Enter Book ID for Item #1:");
		bookID.setPreferredSize(new Dimension(200,25));
		bookIDTxt = new JTextField(30);
		topPanel.add(bookID, BorderLayout.CENTER);
		topPanel.add(bookIDTxt, BorderLayout.LINE_END);
		
		quantity = new JLabel("Enter Quantity for Item#1:");
		quantity.setPreferredSize(new Dimension(200,25));
		quantityTxt = new JTextField(30);
		topPanel.add(quantity);
		topPanel.add(quantityTxt);
		
		info = new JLabel("Item #1 info:");
		info.setPreferredSize(new Dimension(200,25));
		infoTxt = new JTextField(30);
		infoTxt.setEnabled(false);
		topPanel.add(info);
		topPanel.add(infoTxt);
		
		subtotal = new JLabel("Order subtotal for 0 item(s):");
		subtotal.setPreferredSize(new Dimension(200,25));
		subtotalTxt = new JTextField(30);
		subtotalTxt.setEnabled(false);
		topPanel.add(subtotal);
		topPanel.add(subtotalTxt);
		
		frame.add(topPanel);
		frame.add(panel, BorderLayout.PAGE_END);
		frame.setVisible(true);
	}


}
