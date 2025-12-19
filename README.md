# VideogameShopCapstone3

## Description of the Project

This project is a full-stack e-commerce application designed for a video game and equipment retailer. 
As a backend-focused developer, I inherited a Version 1 Spring Boot API and was tasked with fixing critical bugs and implementing new features including 
a Category management system, a User Profile dashboard, a persistent Shopping Cart, and a secure Checkout process. The application uses a MySQL database 
to store product, user, and order data, while utilizing JWT tokens for secure authentication.

## User Stories

- As a user or administrator, I want to retrieve a list of all product categories or a specific category by ID, So that I can navigate the store inventory effectively.
- As an Administrator (User with ADMIN role), I want to add, update, or delete product categories, so that I can keep the store's departmental structure organized.
- As a customer, I want to filter products by specific criteria (category, min price, max price, subcategory), so that I can find exactly what I can afford and need without errors.
- As an Administrator, I want to edit a product's price or description without creating a duplicate copy of that product, so that the inventory remains accurate and doesn't confuse customers with duplicate listings.
- As a logged-in user, I want to view all items currently in my cart, including the total cost, So that I can review my potential purchase before paying.
- As a logged-in user, I want to add a specific product to my cart, so that I can purchase it later.
- As a logged-in user, I want to manually set the quantity of a specific item in my cart, so that I can buy exactly the amount I need.
- As a logged-in user, I want to remove all items from my cart, so that I can start my shopping process over.
- As a user who logs out and logs back in, I want my shopping cart items to still be there, so that I don't lose the items I selected during a previous session.
- As a logged-in user, I want to view my profile details (name, address, email), so that I can verify my shipping and contact information is correct.
- As a logged-in user, I want to update my profile information, so that I can keep my address and contact details current.
- As a logged-in user with items in my cart, I want to "checkout" to finalize my purchase, so that my order is recorded and processed.

## Setup

Instructions on how to set up and run the project using IntelliJ IDEA.

### Prerequisites

- IntelliJ IDEA: Ensure you have IntelliJ IDEA installed, which you can download from [here](https://www.jetbrains.com/idea/download/).
- Java SDK: Make sure Java SDK is installed and configured in IntelliJ.

### Running the Application in IntelliJ

Follow these steps to get your application running within IntelliJ IDEA:

1. Open IntelliJ IDEA.
2. Select "Open" and navigate to the directory where you cloned or downloaded the project.
3. After the project opens, wait for IntelliJ to index the files and set up the project.
4. Find the main class with the `public static void main(String[] args)` method.
5. Right-click on the file and select 'Run 'YourMainClassName.main()'' to start the application.

## Technologies Used

- Java & Spring Boot: For the RESTful API and security.
- MySQL: For relational data storage.
- Spring Security (JWT): For stateless user authentication.
- Axios & Mustache.js: For the dynamic frontend interface.

## Demo

![GIF.gif](GIF.gif)

## Future Work

- Stock Management: Implement logic to automatically decrease product stock levels upon a successful checkout.
- Order History: Create a "Past Orders" page in the UI to allow users to track their previous purchases.

## Resources

List resources such as tutorials, articles, or documentation that helped you during the project.

- Raymond's lectures 
- Brightspace textbook
- Google Gemini: For CSS and HTML 

## Team Members

- Arsenii Kunilovskii

## Thanks

Express gratitude towards those who provided help, guidance, or resources:

- Thank you to Raymond for continuous support and guidance.
