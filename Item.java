package JAVA_fin_exam;

public class Item {
	String menu;
	int price;
	String barcode;
	int stock;
	
	public Item() {
		this.menu = null;
		this.price = 0;
		this.barcode = null;
		this.stock = 0;
	}
	
	public Item(String menu, int price, String barcorde, int stock) {
		this.menu = menu;
		this.price = price;
		this.barcode = barcorde;
		this.stock = stock;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	    }
}
