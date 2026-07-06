
import javax.swing.*;
import java.awt.*;

public class SummaryPanel extends JPanel {

    private JTextField monthField;
    private JButton getBtn;
    private JTextArea output;

    public SummaryPanel() {
        setLayout(new BorderLayout(6,6));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        monthField = new JTextField(8);
        getBtn = new JButton("Get Summary");
        top.add(new JLabel("Month:"));
        top.add(monthField);
        top.add(getBtn);

        output = new JTextArea(10, 50);
        output.setEditable(false);

        getBtn.addActionListener(e -> {
            String month = monthField.getText().trim();
            if (month.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter month"); return; }
            String url = "https://budgetservice1.azurewebsites.net/api/budget/" + month + "/summary";
            String resp = HttpHelper.sendGet(url);
            String formatted = resp
                    .replace("{", "")
                    .replace("}", "")
                    .replace(",", "\n")
                    .replace("\"", "");

            output.setText(formatted);
        });

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
    }
}
