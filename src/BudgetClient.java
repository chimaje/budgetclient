

import javax.swing.*;

public class BudgetClient extends JFrame {

    public BudgetClient() {
        setTitle("Budget Client");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Create Budget", new CreateBudgetPanel());
        tabs.addTab("Summary", new SummaryPanel());
        tabs.addTab("Add Expense", new AddExpensePanel());
        tabs.addTab("Update Expense", new UpdateExpensePanel());
        tabs.addTab("Delete Expense", new DeleteExpensePanel());
        tabs.addTab("Charts", new BudgetChartPanel());

        add(tabs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BudgetClient().setVisible(true));
    }
}
