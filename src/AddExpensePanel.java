
import javax.swing.*;
import java.awt.*;

public class AddExpensePanel extends JPanel {

    private JTextField monthField;
    private JTextField idField;
    private JTextField nameField;
    private JTextField amountField;
    private JTextField categoryField;
    private JButton addButton;
    private JTextArea output;

    public AddExpensePanel() {
        setLayout(new BorderLayout(6,6));
        JPanel grid = new JPanel(new GridLayout(6,2,5,5));

        monthField = new JTextField();
        idField = new JTextField(); // optional id
        nameField = new JTextField();
        amountField = new JTextField();
        categoryField = new JTextField();

        addButton = new JButton("Add Expense");
        output = new JTextArea(6,40);
        output.setEditable(false);

        grid.add(new JLabel("Month:"));
        grid.add(monthField);
        grid.add(new JLabel("ID (optional):"));
        grid.add(idField);
        grid.add(new JLabel("Name:"));
        grid.add(nameField);
        grid.add(new JLabel("Amount:"));
        grid.add(amountField);
        grid.add(new JLabel("Category:"));
        grid.add(categoryField);
        grid.add(new JLabel(""));
        grid.add(addButton);

        addButton.addActionListener(e -> submitExpense());

        add(grid, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
    }

    private void submitExpense() {
        try {
            String month = monthField.getText().trim();
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String amount = amountField.getText().trim();
            String category = categoryField.getText().trim();

            if (month.isEmpty() || name.isEmpty() || amount.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill month, name, amount, category");
                return;
            }

            String idPart = id.isEmpty() ? "" : "\"id\": " + id + ",\n";
            String json = "{\n" + idPart +
                    "\"name\": \"" + escape(name) + "\",\n" +
                    "\"amount\": " + amount + ",\n" +
                    "\"category\": \"" + escape(category) + "\"\n" +
                    "}";

            String url = "http://localhost:8080/api/budget/" + month + "/expense";
            String resp = HttpHelper.sendPost(url, json);
            output.setText(resp);
        } catch (Exception ex) {
            output.setText("Error: " + ex.getMessage());
        }
    }

    private String escape(String s) {
        return s.replace("\"", "\\\"");
    }
}
