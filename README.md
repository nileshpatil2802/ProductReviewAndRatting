Project title is Product Review and Rating System.

Functionalities:
1.Product Management:
	Add new products to the platform
	View product details
	Update product information
	Delete products from the platform
2.Review Submission:
	Submit new reviews for products
	View review details
	Update review information
	Delete reviews
3.Rating Analysis:
	Calculate average ratings for products
	Analyze ratings distribution (e.g., number of 5-star, 4-star, etc. ratings)
	View top-rated products
Database Schema:
Product Table:
	product_id (Primary Key)
	product_name
	price
	description
	category_id (Foreign Key references Category Table)
	Review Table:
	review_id (Primary Key)
	product_id (Foreign Key references Product Table)
	customer_id (Foreign Key references Customer Table)
	review_text
	rating
Rating Table:
	rating_id (Primary Key)
	product_id (Foreign Key references Product Table)
	rating_value
	rating_date
Requirements:
	Develop a menu-based console application using Core Java.
	Use JDBC for interactions with the MySQL database.
	Implement menu options for managing products, submitting reviews, and analyzing product ratings.
	Ensure that the application accurately calculates average ratings, analyzes ratings distribution, and displays top-rated products.
	Handle exceptions effectively and provide user-friendly error messages.Ensure the application code is clean, well-documented, and 	follows standard coding conventions.