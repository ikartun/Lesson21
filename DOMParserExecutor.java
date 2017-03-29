package by.epam.tr.lesson21;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import by.epam.tr.lesson20.bean.Product;
import by.epam.tr.lesson20.bean.Rent;

public class DOMParserExecutor {

	public static void main(String[] args) throws SAXException, IOException {
		DOMParser domParser = new DOMParser();
		domParser.parse("src/by/epam/tr/lesson20/ProductRent.xml");
		
		Document document = domParser.getDocument();		
		Element element = document.getDocumentElement();
		
		List<Product> products = new ArrayList<>();
		
		NodeList productNodes = element.getElementsByTagName("product");
		Product product = null;
		
		for (int i = 0; i < productNodes.getLength(); i++) {
			product = new Product();
			Element productElement = (Element) productNodes.item(i);
			
			product.setProductId(Integer.parseInt(productElement.getAttribute("id")));
			product.setName(getSingleChild(productElement, "name").getTextContent().trim());
			product.setPrisePerDay(Integer.parseInt(getSingleChild(productElement, "price_per_day").getTextContent().trim()));
			product.setAmount(Integer.parseInt(getSingleChild(productElement, "amount").getTextContent().trim()));
			
			NodeList rentNodes = productElement.getElementsByTagName("rent");
			Rent rent = null;
			Set<Rent> rents = new HashSet<>();
			
			for (int j = 0; j < rentNodes.getLength(); j++) {
				rent = new Rent();
				Element rentElement = (Element) rentNodes.item(j);
				
				rent.setRentId(Integer.parseInt(rentElement.getAttribute("id")));
				rent.setDateFrom(Date.valueOf(getSingleChild(rentElement, "date_from").getTextContent().trim()));
				if (!getSingleChild(rentElement, "date_to").getTextContent().trim().isEmpty()) {
					rent.setDateTo(Date.valueOf(getSingleChild(rentElement, "date_to").getTextContent().trim()));					
				}
				rent.setProduct(product);
				rents.add(rent);
			}
			product.setRents(rents);
			products.add(product);
		}
		
		for (Product pr : products) {
			System.out.println(pr.getName());
			System.out.println("rents are: ");
			
			for (Rent rent : pr.getRents()) {
				System.out.println("from " + rent.getDateFrom().toString() + " to " + ((rent.getDateTo() == null) ? "now" : rent.getDateTo().toString()));
			}
		}		
	}
	
	private static Element getSingleChild(Element element, String childName){
		NodeList nlist = element.getElementsByTagName(childName);
		Element child = (Element) nlist.item(0);
		return child;
	}
}
