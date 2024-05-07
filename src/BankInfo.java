import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BankInfo {

    public static void main(String[] args) {
        try {
            // Get user input
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the first three digits of your bank account: ");
            String userInput = reader.readLine();

            // Validate user input
            if (!userInput.matches("\\d{3}")) {
                System.out.println("Invalid input. Please enter three digits.");
                return;
            }

            String bankCode = userInput;

            // Ensure the bank code is three digits
            while (bankCode.length() < 3) {
                bankCode += "0";
            }

            // Fetch bank data
            String bankInfo = getBankInfo(bankCode);

            // Display bank information
            if (bankInfo != null) {
                System.out.println("You have an account in " + bankInfo);
            } else {
                System.out.println("Bank information not found for the provided bank code.");
            }

        } catch (IOException e) {
            System.out.println("Error reading user input or fetching bank data: " + e.getMessage());
        }
    }

    private static String getBankInfo(String bankCode) throws IOException {
        String url = "https://ewib.nbp.pl/plewibnra?dokNazwa=plewibnra.txt";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith(bankCode)) {
                    String[] parts = inputLine.split("\\s+", 2);
                    if (parts.length == 2) {
                        return parts[1].trim();
                    }
                }
            }
        }

        return null;
    }
}
