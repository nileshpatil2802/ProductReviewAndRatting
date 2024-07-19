package com.myapp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

	private static final String DB_URL = "jdbc:mysql://localhost:3307/productreviewandratingsystem";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "root";
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


	public static void main(String[] args) {
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
			Main app = new Main();
			app.runApp(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void runApp(Connection connection) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("____________________________________________________________");
			System.out.println("Menu:");
			System.out.println("1. Add Category");
			System.out.println("2. View All Categories");
			System.out.println("3. Update Category");
			System.out.println("4. Delete Category");
			System.out.println("5. Add new product");
			System.out.println("6. View All Products");
			System.out.println("7. View product details");
			System.out.println("8. Update product information");
			System.out.println("9. Delete product");
			System.out.println("10. Add Customer");
			System.out.println("11. View All Customers");
			System.out.println("12. Update Customer");
			System.out.println("13. Delete Customer");
			System.out.println("14. Add Ratings");		// pending
			System.out.println("15. Submit new review");
			System.out.println("16. View review details");
			System.out.println("17. Update review information");
			System.out.println("18. Delete review");
			System.out.println("19. Calculate average ratings");
			System.out.println("20. Analyze ratings distribution");
			System.out.println("21. View top-rated products");
			System.out.println("0. Exit");
			System.out.println("____________________________________________________________");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				addNewCategory(connection, scanner);
				break;
			case 2:
				viewAllCategories(connection);
				break;
			case 3:
				updateCategories(connection, scanner);
				break;
			case 4:
				deleteCategories(connection, scanner);
				break;
			case 5:
				addNewProduct(connection, scanner);
				break;
			case 6:
				viewAllProducts(connection, scanner);
				break;
			case 7:
				viewProductDetails(connection, scanner);
				break;
			case 8:
				updateProductInformation(connection, scanner);
				break;
			case 9:
				deleteProduct(connection, scanner);
				break;
			case 10:
				addCustomer(connection, scanner);
				break;
			case 11:
				viewAllCustomers(connection, scanner);
				break;
			case 12:
				updateCustomer(connection, scanner);
				break;
			case 13:
				deleteCustomer(connection, scanner);
				break;
			case 14:
				addRatings(connection, scanner);
				break;
			case 15:
				submitNewReview(connection, scanner);
				break;
			case 16:
				viewReviewDetails(connection, scanner);
				break;
			case 17:
				updateReviewInformation(connection, scanner);
				break;
			case 18:
				deleteReview(connection, scanner);
				break;
			case 19:
				calculateAverageRatings(connection);
				break;
			case 20:
				analyzeRatingsDistribution(connection);
				break;
			case 21:
				viewTopRatedProducts(connection);
				break;
			case 0:
				System.out.println("Exiting...");
				return;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	private void addNewCategory(Connection connection, Scanner scanner) {
		System.out.print("Enter category name: ");
		String categoryName = scanner.nextLine();

		String sql = "INSERT INTO Category (category_name) VALUES (?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, categoryName);
			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("A new category was inserted successfully!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void viewAllCategories(Connection connection) {
		String sql = "SELECT * FROM Category";

		try (PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				System.out.println("Category ID: " + rs.getInt("category_id"));
				System.out.println("Category Name: " + rs.getString("category_name"));
				System.out.println("-------------------------------");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void updateCategories(Connection connection, Scanner scanner) {
		System.out.print("Enter category ID: ");
		int categoryId = scanner.nextInt();
		scanner.nextLine(); // Consume newline
		System.out.print("Enter new category name: ");
		String categoryName = scanner.nextLine();

		String sql = "UPDATE Category SET category_name = ? WHERE category_id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, categoryName);
			pstmt.setInt(2, categoryId);
			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Category updated successfully!");
			} else {
				System.out.println("No category found with the given ID.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void deleteCategories(Connection connection, Scanner scanner) {
		System.out.print("Enter category ID to delete: ");
		int categoryId = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		String sql = "DELETE FROM Category WHERE category_id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, categoryId);
			int rowsDeleted = pstmt.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Category deleted successfully!");
			} else {
				System.out.println("No category found with the given ID.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void addNewProduct(Connection connection, Scanner scanner) {
		System.out.print("Enter product name: ");
		String productName = scanner.nextLine();
		System.out.print("Enter product price: ");
		double price = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter product description: ");
		String description = scanner.nextLine();
		System.out.print("Enter category ID: ");
		int categoryId = scanner.nextInt();
		scanner.nextLine();

		String sql = "INSERT INTO Product (product_name, price, description, category_id) VALUES (?, ?, ?, ?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, productName);
			pstmt.setDouble(2, price);
			pstmt.setString(3, description);
			pstmt.setInt(4, categoryId);
			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("A new product was inserted successfully!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void viewAllProducts(Connection connection, Scanner scanner) {
		String sql = "SELECT * FROM Product";

		try (PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				System.out.println("Product ID: " + rs.getInt("product_id"));
				System.out.println("Product Name: " + rs.getString("product_name"));
				System.out.println("Price: " + rs.getDouble("price"));
				System.out.println("Description: " + rs.getString("description"));
				System.out.println("Category ID: " + rs.getInt("category_id"));
				System.out.println("-------------------------------");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void viewProductDetails(Connection connection, Scanner scanner) {
		System.out.print("Enter product ID: ");
		int productId = scanner.nextInt();
		scanner.nextLine();

		String sql = "SELECT * FROM Product WHERE product_id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, productId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				System.out.println("Product ID: " + rs.getInt("product_id"));
				System.out.println("Product Name: " + rs.getString("product_name"));
				System.out.println("Price: " + rs.getDouble("price"));
				System.out.println("Description: " + rs.getString("description"));
				System.out.println("Category ID: " + rs.getInt("category_id"));

			} else {
				System.out.println("No product found with the given ID.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateProductInformation(Connection connection, Scanner scanner) {
		System.out.print("Enter product ID to update: ");
		int productId = scanner.nextInt();
		scanner.nextLine();

		System.out.print("Enter new product name: ");
		String productName = scanner.nextLine();
		System.out.print("Enter new product price: ");
		double price = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter new product description: ");
		String description = scanner.nextLine();
		System.out.print("Enter new category ID: ");
		int categoryId = scanner.nextInt();
		scanner.nextLine();

		String sql = "UPDATE Product SET product_name = ?, price = ?, description = ?, category_id = ? WHERE product_id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, productName);
			pstmt.setDouble(2, price);
			pstmt.setString(3, description);
			pstmt.setInt(4, categoryId);
			pstmt.setInt(5, productId);
			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Product updated successfully!");
			} else {
				System.out.println("No product found with the given ID.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void deleteProduct(Connection connection, Scanner scanner) {
		System.out.print("Enter product ID to delete: ");
		int productId = scanner.nextInt();
		scanner.nextLine();
		String sql = "DELETE FROM Product WHERE product_id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, productId);
			int rowsDeleted = pstmt.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Product deleted successfully!");
			} else {
				System.out.println("No product found with the given ID.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void addCustomer(Connection connection, Scanner scanner) {
		System.out.print("Enter customer name: ");
		String customerName = scanner.nextLine();
		System.out.print("Enter customer email: ");
		String email = scanner.nextLine();
		System.out.print("Enter customer mobile: ");
		String mobile = scanner.nextLine();

		String sql = "INSERT INTO Customer (customer_name, email, mobile) VALUES (?, ?, ?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, customerName);
			pstmt.setString(2, email);
			pstmt.setString(3, mobile);
			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("A new customer was inserted successfully!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void viewAllCustomers(Connection connection, Scanner scanner) {
		String sql = "SELECT * FROM Customer";

		try (PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				System.out.println("Customer ID: " + rs.getInt("customer_id"));
				System.out.println("Customer Name: " + rs.getString("customer_name"));
				System.out.println("Email: " + rs.getString("email"));
				System.out.println("Mobile: " + rs.getString("mobile"));
				System.out.println("-------------------------------");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void updateCustomer(Connection connection, Scanner scanner) {
		System.out.print("Enter customer ID to update: ");
		int customerId = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		System.out.print("Enter new customer name: ");
		String customerName = scanner.nextLine();
		System.out.print("Enter new customer email: ");
		String email = scanner.nextLine();
		System.out.print("Enter new customer mobile: ");
		String mobile = scanner.nextLine();

		String sql = "UPDATE Customer SET customer_name = ?, email = ?, mobile = ? WHERE customer_id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, customerName);
			pstmt.setString(2, email);
			pstmt.setString(3, mobile);
			pstmt.setInt(4, customerId);

			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Customer updated successfully!");
			} else {
				System.out.println("No customer found with the given ID.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void deleteCustomer(Connection connection, Scanner scanner) {
		try {
			connection.setAutoCommit(false); // Start transaction

			System.out.print("Enter customer ID to delete: ");
			int customerId = scanner.nextInt();
			scanner.nextLine(); // Consume newline

			// First delete dependent records from the review table
			String deleteReviewsSql = "DELETE FROM review WHERE customer_id = ?";
			try (PreparedStatement pstmtReviews = connection.prepareStatement(deleteReviewsSql)) {
				pstmtReviews.setInt(1, customerId);
				int rowsDeletedReviews = pstmtReviews.executeUpdate();
				System.out.println(rowsDeletedReviews + " review(s) deleted.");
			}

			// Now delete the customer
			String deleteCustomerSql = "DELETE FROM customer WHERE customer_id = ?";
			try (PreparedStatement pstmtCustomer = connection.prepareStatement(deleteCustomerSql)) {
				pstmtCustomer.setInt(1, customerId);
				int rowsDeletedCustomer = pstmtCustomer.executeUpdate();
				if (rowsDeletedCustomer > 0) {
					System.out.println("Customer deleted successfully!");
				} else {
					System.out.println("No customer found with the given ID.");
				}
			}

			connection.commit(); // Commit transaction
		} catch (SQLException e) {
			try {
				connection.rollback(); // Rollback if there's an exception
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true); // Restore auto-commit mode
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void addRatings(Connection connection, Scanner scanner) {
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over

        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over

        System.out.print("Enter rating value (1-5): ");
        int ratingValue = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over

        System.out.print("Enter rating date (YYYY-MM-DD, press Enter for today's date): ");
        String dateString = scanner.nextLine().trim(); // Trim any leading/trailing spaces

        LocalDate date;
        if (dateString.isEmpty()) {
            date = LocalDate.now();
        } else {
            try {
                date = LocalDate.parse(dateString, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format. Please enter date in YYYY-MM-DD format.");
                return; // Exit method if date format is incorrect
            }
        }

        // Convert LocalDate to java.sql.Date
        Date ratingDate = Date.valueOf(date);

        String sql = "INSERT INTO Rating (product_id, customer_id, rating_value, rating_date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, productId);
            pstmt.setInt(2, customerId);
            pstmt.setInt(3, ratingValue);
            pstmt.setDate(4, ratingDate);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new rating was inserted successfully!");

                // Retrieve the generated rating_id if needed
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int ratingId = generatedKeys.getInt(1);
                    System.out.println("Generated Rating ID: " + ratingId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	private void submitNewReview(Connection connection, Scanner scanner) {
		System.out.print("Enter product ID: ");
		int productId = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Enter customer ID: ");
		int customerId = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Enter review text: ");
		String reviewText = scanner.nextLine();
		System.out.print("Enter rating (1-5): ");
		int rating = scanner.nextInt();
		scanner.nextLine();
		String sql = "INSERT INTO Review (product_id, customer_id, review_text, rating) VALUES (?, ?, ?, ?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, productId);
			pstmt.setInt(2, customerId);
			pstmt.setString(3, reviewText);
			pstmt.setInt(4, rating);
			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("A new review was submitted successfully!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void viewReviewDetails(Connection connection, Scanner scanner) {
		System.out.print("Enter review ID: ");
		int reviewId = scanner.nextInt();
		scanner.nextLine();

		String sql = "SELECT * FROM Review WHERE review_id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, reviewId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				System.out.println("Review ID: " + rs.getInt("review_id"));
				System.out.println("Product ID: " + rs.getInt("product_id"));
				System.out.println("Customer ID: " + rs.getInt("customer_id"));
				System.out.println("Review Text: " + rs.getString("review_text"));
				System.out.println("Rating: " + rs.getInt("rating"));
			} else {
				System.out.println("No review found with the given ID.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateReviewInformation(Connection connection, Scanner scanner) {
		System.out.print("Enter review ID to update: ");
		int reviewId = scanner.nextInt();
		scanner.nextLine();

		System.out.print("Enter new review text: ");
		String reviewText = scanner.nextLine();
		System.out.print("Enter new rating (1-5): ");
		int rating = scanner.nextInt();
		scanner.nextLine();

		String sql = "UPDATE Review SET review_text = ?, rating = ? WHERE review_id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, reviewText);
			pstmt.setInt(2, rating);
			pstmt.setInt(3, reviewId);
			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Review information updated successfully!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void deleteReview(Connection connection, Scanner scanner) {
		System.out.print("Enter review ID: ");
		int reviewId = scanner.nextInt();
		scanner.nextLine();

		String sql = "DELETE FROM Review WHERE review_id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, reviewId);
			int rowsDeleted = pstmt.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Review deleted successfully!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void calculateAverageRatings(Connection connection) {
		String sql = "SELECT product_id, AVG(rating) as average_rating FROM Review GROUP BY product_id";

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			boolean hasResults = false;

			while (rs.next()) {
				hasResults = true;
				int productId = rs.getInt("product_id");
				double averageRating = rs.getDouble("average_rating");
				System.out.println("Product ID: " + productId + " - Average Rating: " + averageRating);
			}

			if (!hasResults) {
				System.out.println("No results found.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void analyzeRatingsDistribution(Connection connection) {
		String sql = "SELECT product_id, rating, COUNT(*) as count FROM Review GROUP BY product_id, rating";

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				int productId = rs.getInt("product_id");
				int rating = rs.getInt("rating");
				int count = rs.getInt("count");
				System.out.println("Product ID: " + productId + " - Rating: " + rating + " - Count: " + count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void viewTopRatedProducts(Connection connection) {
		String sql = "SELECT product_id, AVG(rating) as average_rating FROM Review GROUP BY product_id ORDER BY average_rating DESC LIMIT 10";

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				int productId = rs.getInt("product_id");
				double averageRating = rs.getDouble("average_rating");
				System.out.println("Product ID: " + productId + " - Average Rating: " + averageRating);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
