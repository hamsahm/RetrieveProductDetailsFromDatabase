package com.simplilearn.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RetrieveServlet
 */
public class RetrieveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RetrieveServlet() {
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			// Get database details from properties file that is present in
			// config.properties
			InputStream in = getServletContext().getResourceAsStream("\\WEB-INF\\config.properties");
			Properties prop = new Properties();
			prop.load(in);

			String url = prop.getProperty("url"); // Fetch database url
			String username = prop.getProperty("username"); // Fetch username
			String password = prop.getProperty("password"); // Fetch password
			String driver = prop.getProperty("driver"); // Fetch database driver

			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);

			PrintWriter out = response.getWriter();

			// Get the product Id from the
			String productIdFromHTML = request.getParameter("productId");

			PreparedStatement ps = conn.prepareStatement("select * from products where product_id=?");
			ps.setString(1, productIdFromHTML);

			ResultSet rs = ps.executeQuery();

			boolean hasProduct = false;
			while (rs.next()) {
				hasProduct = true;
				int productId = rs.getInt(1);
				String productName = rs.getString(2);
				int productQuantityInStock = rs.getInt(3);
				float productUnitPrice = rs.getFloat(4);

				out.println("Product Id: " + productId + "<br/>");
				out.println("Product Name: " + productName + "<br/>");
				out.println("Product Quantity In Stock: " + productQuantityInStock + "<br/>");
				out.println("Product Unit Price: " + productUnitPrice + "<br/>");
				out.println("Product Unit Price1: " + productUnitPrice + "<br/>");


			}
			if (!hasProduct) {
				out.println("There is no such product with id : " + productIdFromHTML);
			}
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
