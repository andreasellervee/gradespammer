import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestClass {

    public static void main(String[] args) throws IOException, InterruptedException {

        String url = "https://docs.google.com/spreadsheets/d/1j1WtINGJaxvQz1vSnMsIE0QeA3wqvTxXKWh2gU8SWS8/pubhtml?gid=1&single=true";
        Integer previousHash = null;
        String previousBody = null;

        while (true) {
            StringBuffer response = getResponse(url);
            String table = parseTable(response);
            if (previousBody == null) {
                previousBody = table;
            }

            Integer newHash = table.hashCode();
            if (previousHash == null) {
                previousHash = newHash;
            }

            if (!previousHash.equals(newHash)) {
                System.out.println("SOMETHING HAS CHANGED !!!!!!!!!!!! " + new Date());
                System.out.println("Sending email");
                EmailSender.sendEmail(table);
                System.out.println("Email sent");
                previousHash = newHash;
                previousBody = table;
            } else {
                System.out.println("No changes detected: " + new Date());
            }

            Thread.sleep(25000);
        }
    }

    private static StringBuffer getResponse(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response;
    }

    private static String parseTable(StringBuffer response) {
        final Pattern pattern = Pattern.compile("<table(.+?)</table>");
        final Matcher matcher = pattern.matcher(response.toString());
        matcher.find();
        String table = "<table" + matcher.group(1) + "</table>";
        return table;
    }
}