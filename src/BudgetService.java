import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BudgetService {

    private IRepo repo;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public BudgetService(IRepo repo) {
        this.repo = repo;
    }

    public double queryBudget(LocalDate startDate, LocalDate endDate) {

        List<Budget> budgets = repo.getAll();
        double budgetValue = 0;
        if (startDate.getYear() == endDate.getYear() && startDate.getMonth() == endDate.getMonth()) {
            int days = endDate.getDayOfMonth() - startDate.getDayOfMonth() + 1;
            for (int i = 0; i < budgets.size(); i++) {
                Budget budget = budgets.get(i);
                LocalDate budgetLocalDate = LocalDate.parse(budget.getYearMonth() + "01", dateTimeFormatter);
                if (budgetLocalDate.getYear() == startDate.getYear()
                        && budgetLocalDate.getMonth() == startDate.getMonth()) {
                    return getBudgetPerDay(budget, budgetLocalDate) * days;
                }
            }
        }
        return budgetValue;
    }

    private double getBudgetPerDay(Budget budget, LocalDate budgetLocalDate) {
        return Math.round(budget.getAmount() * 1.0 / budgetLocalDate.lengthOfMonth() * 100) / 100.0;
    }
}
