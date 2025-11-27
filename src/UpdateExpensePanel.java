
import javax.swing.*;
import java.awt.*;

public class UpdateExpensePanel extends JPanel {

    private JTextField monthField, idField, nameField, amountField, categoryField;
    private JButton updateBtn;
    private JTextArea output;

    public UpdateExpensePanel() {
        setLayout(new BorderLayout(6,6));
        JPanel grid = new JPanel(new GridLayout(6,2,5,5));

        monthField = new JTextField();
        idField = new JTextField();
        nameField = new JTextField();
        amountField = new JTextField();
        categoryField = new JTextField();
        updateBtn = new JButton("Update Expense");
        output = new JTextArea(6,40);
        output.setEditable(false);

        grid.add(new JLabel("Month:"));
        grid.add(monthField);
        grid.add(new JLabel("Expense ID:"));
        grid.add(idField);
        grid.add(new JLabel("New Name:"));
        grid.add(nameField);
        grid.add(new JLabel("New Amount:"));
        grid.add(amountField);
        grid.add(new JLabel("New Category:"));
        grid.add(categoryField);
        grid.add(new JLabel(""));
        grid.add(updateBtn);

        updateBtn.addActionListener(e -> updateExpense());

        add(grid, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
    }

    private void updateExpense() {
        try {
            String month = monthField.getText().trim();
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String amount = amountField.getText().trim();
            String category = categoryField.getText().trim();

            if (month.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter month and expense ID");
                return;
            }

            String json = "{\n" +
                    "\"name\": \"" + escape(name) + "\",\n" +
                    "\"amount\": " + amount + ",\n" +
                    "\"category\": \"" + escape(category) + "\"\n" +
                    "}";

            String url = "http://localhost:8080/api/budget/" + month + "/expense/" + id;
            String resp = HttpHelper.sendPut(url, json);
            output.setText(resp);

        } catch (Exception ex) {
            output.setText("Error: " + ex.getMessage());
        }
    }

    private String escape(String s) {
        return s.replace("\"", "\\\"");
    }
}
