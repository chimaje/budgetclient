

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.data.general.DefaultPieDataset;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.StringReader;
import java.util.Iterator;

public class BudgetChartPanel extends JPanel {

    private JTextField monthField;
    private JButton loadBtn;
    private JPanel chartHolder;

    public BudgetChartPanel() {
        setLayout(new BorderLayout(6,6));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        monthField = new JTextField(8);
        loadBtn = new JButton("Load Chart");
        top.add(new JLabel("Month:"));
        top.add(monthField);
        top.add(loadBtn);

        chartHolder = new JPanel(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(chartHolder, BorderLayout.CENTER);

        loadBtn.addActionListener(e -> loadChart());
    }

    private void loadChart() {
        String month = monthField.getText().trim();
        if (month.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter month"); return; }

        String url = "http://localhost:8080/api/budget/" + month + "/summary";
        String resp = HttpHelper.sendGet(url);

        if (resp.startsWith("Error") || resp.startsWith("HTTP")) {
            JOptionPane.showMessageDialog(this, "Failed to get summary: " + resp);
            return;
        }

        try {
            JSONObject obj = new JSONObject(resp);
            if (obj.has("error")) {
                JOptionPane.showMessageDialog(this, obj.getString("error"));
                return;
            }

            JSONObject percentages = obj.optJSONObject("categoryPercentages");
            DefaultPieDataset dataset = new DefaultPieDataset();

            if (percentages != null) {
                Iterator<String> keys = percentages.keys();
                while (keys.hasNext()) {
                    String k = keys.next();
                    double val = percentages.getDouble(k);
                    dataset.setValue(k, val);
                }
            }

            JFreeChart chart = ChartFactory.createPieChart("Budget - " + month, dataset, true, true, false);
            chartHolder.removeAll();
            chartHolder.add(new ChartPanel(chart), BorderLayout.CENTER);
            chartHolder.revalidate();
            chartHolder.repaint();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "JSON/Chart error: " + ex.getMessage());
        }
    }
}
