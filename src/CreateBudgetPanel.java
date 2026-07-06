
import javax.swing.*;
import java.awt.*;

public class CreateBudgetPanel extends JPanel {

    private JTextField monthField;
    private JTextField limitField;
    private JButton createBtn;
    private JTextArea output;

    public CreateBudgetPanel() {
        setLayout(new BorderLayout(6,6));
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        monthField = new JTextField(8);
        limitField = new JTextField(6);
        createBtn = new JButton("Create Budget");
        form.add(new JLabel("Month:"));
        form.add(monthField);
        form.add(new JLabel("Limit:"));
        form.add(limitField);
        form.add(createBtn);

        output = new JTextArea(6, 40);
        output.setEditable(false);

        createBtn.addActionListener(e -> {
            String month = monthField.getText().trim();
            String limit = limitField.getText().trim();
            if (month.isEmpty() || limit.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter month and limit");
                return;
            }
            String url = "https://budgetservice1.azurewebsites.net/api/budget/" + month + "?limit=" + limit;
            String resp = HttpHelper.sendPost(url, "");
            output.setText(resp);
        });

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
    }
}
