
# Event-Driven-Program
Simple GUI that simulates a bookstore POS.

### Purpose
This application is a simple tool that will provide basic order information for a bookstore.

### How it works
This application uses a text file as it's database. The user will enter the required number of items for an order, and then each corresponding book ID. The application will check if the Book ID exists in the 'database' and pull in the information pertaining to the book. It will keep track of the total and any discounts that may apply. Once the order is complete, it will print out a receipt and also create/modify a transaction text file that keeps track of all orders with a unqie ID (using day-month-year-hour-min-sec).

### What's provided
Everything needed to run is provided in the source folder. The text file labled inventory.txt is a list of all the books the library contains. When running the application use the BookID's from the inventory.txt to create an order. 


**UI**
<div align = "center">
    <img src="/Images/initialStore.PNG" width="500px"</img> 
</div>


**Receipt upon completion**
<div align="center">
    <img src="/Images/orderComplete.PNG" width="500px"</img> 
</div>
