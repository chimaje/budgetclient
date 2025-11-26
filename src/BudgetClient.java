import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class BudgetClient {

    private JFrame frame;
    private JTextField monthField;
    private JButton fetchButton;
    private JTextArea resultArea;
    public static String getBudgetSummary(String month) {
        String urlString = "http://localhost:8080/api/budget/" + month + "/summary";
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public BudgetClient() {
        frame = new JFrame("Budget Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Month (YYYY-MM):"));
        monthField = new JTextField(10);
        topPanel.add(monthField);

        fetchButton = new JButton("Fetch Summary");
        topPanel.add(fetchButton);

        frame.add(topPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        frame.setVisible(true);

        // Step 3: Add button action (we’ll implement fetch later)
        fetchButton.addActionListener(e -> {
            String month = monthField.getText();
            String json = getBudgetSummary(month);
            if (json != null) {
                try {
                    org.json.JSONObject obj = new org.json.JSONObject(json);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Month: ").append(month).append("\n");
                    sb.append("Total Expenses: ").append(obj.getDouble("total")).append("\n");
                    sb.append("Remaining Budget: ").append(obj.getDouble("remaining")).append("\n\n");

                    sb.append("Category Totals:\n");
                    org.json.JSONObject catTotals = obj.getJSONObject("categoryTotals");
                    for (String key : catTotals.keySet()) {
                        sb.append("  ").append(key).append(": ").append(catTotals.getDouble(key)).append("\n");
                    }

                    sb.append("\nCategory Averages:\n");
                    org.json.JSONObject catAvg = obj.getJSONObject("categoryAverages");
                    for (String key : catAvg.keySet()) {
                        sb.append("  ").append(key).append(": ").append(catAvg.getDouble(key)).append("\n");
                    }

                    sb.append("\nCategory Percentages:\n");
                    org.json.JSONObject catPerc = obj.getJSONObject("categoryPercentages");
                    for (String key : catPerc.keySet()) {
                        sb.append("  ").append(key).append(": ").append(String.format("%.2f", catPerc.getDouble(key))).append("%\n");
                    }

                    resultArea.setText(sb.toString());

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Failed to parse JSON", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to fetch budget", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BudgetClient::new);
    }
}
