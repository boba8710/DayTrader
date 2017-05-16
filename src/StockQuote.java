public class StockQuote {

	// Given symbol, get HTML
	private static String readHTML(String symbol) {
		In page = new In("http://finance.yahoo.com/quote/" + symbol);
		String html = page.readAll();
		if (html.contains("<title></title>"))
			return null;
		else
			return html;
	}

	// Given symbol, get current stock price.
	public static double priceOf(String symbol) {
		String html = readHTML(symbol);
		String target = "<span class=\"Trsdu(0.3s) Fw(b) Fz(36px) Mb(-4px) D(ib)\" data-reactid=\"36\">";
		int p = html.indexOf(target, 0);
		int initialPos = p + target.length() - 1;
		int finalPos = initialPos + 6;
		String price = html.substring(initialPos + 1, finalPos + 1);

		// remove any comma separators
		return Double.parseDouble(price.replaceAll(",", ""));
	}

	// Given symbol, get current stock price.
	public static double priceOf(String symbol, String html) {
		String target = "<span class=\"Trsdu(0.3s) Fw(b) Fz(36px) Mb(-4px) D(ib)\" data-reactid=\"36\">";
		int p = html.indexOf(target, 0);
		int initialPos = p + target.length() - 1;
		int finalPos = initialPos + 6;
		String price = html.substring(initialPos + 1, finalPos + 1);
		// remove any comma separators
		return Double.parseDouble(price.replaceAll(",", ""));
	}

	public static void main(String[] args) {
		String symbol = "NVDA";

		String html = readHTML(symbol);
		if (html == null)
			StdOut.println("Invalid symbol: " + symbol);
		else {
			StdOut.println(priceOf(symbol, html));

		}
	}

}