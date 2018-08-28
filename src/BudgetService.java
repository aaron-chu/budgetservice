import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetService {

    private IRepo repo;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public BudgetService(IRepo repo) {
        this.repo = repo;
    }

    public double queryBudget(LocalDate startDate, LocalDate endDate) {
        List<Budget> budgets = repo.getAll();
        double budgetValue = 0;
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDate finalDate = date;
            List<Budget> collect = budgets.stream()
                    .filter(budget -> isSameYearMonth(budget, finalDate))
                    .collect(Collectors.toList());
            if (collect.isEmpty()) {
                budgetValue += 0.0;
            } else {
                budgetValue += getBudgetPerDay(collect.get(0));
            }
        }
        return budgetValue;
    }

    private boolean isSameYearMonth(Budget budget, LocalDate date) {
        LocalDate budgetLocalDate = LocalDate.parse(budget.getYearMonth() + "01", dateTimeFormatter);
        return budgetLocalDate.getYear() == date.getYear() && budgetLocalDate.getMonth() == date.getMonth();
    }

    private boolean isSameYearMonth(LocalDate startDate, LocalDate endDate) {
        return startDate.getYear() == endDate.getYear() && startDate.getMonth() == endDate.getMonth();
    }

    private double getBudgetPerDay(Budget budget) {
        LocalDate budgetLocalDate = LocalDate.parse(budget.getYearMonth() + "01", dateTimeFormatter);
        return Math.round(budget.getAmount() * 1.0 / budgetLocalDate.lengthOfMonth() * 100) / 100.0;
    }
}
