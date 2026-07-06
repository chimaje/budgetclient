

import javax.swing.*;
import java.awt.*;

public class DeleteExpensePanel extends JPanel {

    private JTextField monthField;
    private JTextField idField;
    private JButton delBtn;
    private JTextArea output;

    public DeleteExpensePanel() {
        setLayout(new BorderLayout(6,6));
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        monthField = new JTextField(8);
        idField = new JTextField(6);
        delBtn = new JButton("Delete Expense");
        p.add(new JLabel("Month:"));
        p.add(monthField);
        p.add(new JLabel("Expense ID:"));
        p.add(idField);
        p.add(delBtn);

        output = new JTextArea(6,40);
        output.setEditable(false);

        delBtn.addActionListener(e -> {
            String month = monthField.getText().trim();
            String id = idField.getText().trim();
            if (month.isEmpty() || id.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter month and id"); return; }
            String url = "https://budgetservice1.azurewebsites.net/api/budget/" + month + "/expense/" + id;
            String resp = HttpHelper.sendDelete(url);
            output.setText(resp);
        });

        add(p, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
    }
}
