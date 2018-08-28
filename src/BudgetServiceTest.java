import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BudgetServiceTest {

    private StubRepo stubRepo = new StubRepo();

    private BudgetService budgetService = new BudgetService(stubRepo);

    class StubRepo implements IRepo {
        private List<Budget> budgets;

        @Override
        public List<Budget> getAll() {
            return budgets;
        }

        public void setAll(List<Budget> budgets) {
            this.budgets = budgets;
        }
    }


    @Test
    public void same_date() {
        givenBudgets(new Budget() {{
            setYearMonth("201807");
            setAmount(620);
        }});

        LocalDate today = LocalDate.parse("2018-07-01");

        assertEquals(20, budgetService.queryBudget(today, today));
    }

    @Test
    public void same_date_decimal() {
        givenBudgets(new Budget() {{
            setYearMonth("201810");
            setAmount(300);
        }});

        LocalDate today = LocalDate.parse("2018-10-01");

        assertEquals(9.68, budgetService.queryBudget(today, today));
    }

    @Test
    public void within_1_month() {
        givenBudgets(new Budget() {{
            setYearMonth("201808");
            setAmount(310);
        }});

        LocalDate startDate = LocalDate.parse("2018-08-08");
        LocalDate endDate = LocalDate.parse("2018-08-20");

        assertEquals(130, budgetService.queryBudget(startDate, endDate));
    }

    @Test
    public void across_months() {
        givenBudgets(
                new Budget() {{
                    setYearMonth("201807");
                    setAmount(620);
                }},
                new Budget() {{
                    setYearMonth("201808");
                    setAmount(310);
                }}
        );

        LocalDate startDate = LocalDate.parse("2018-07-31");
        LocalDate endDate = LocalDate.parse("2018-08-01");

        assertEquals(30, budgetService.queryBudget(startDate, endDate));
    }


    @Test
    public void across_years() {
        givenBudgets(
                new Budget() {{
                    setYearMonth("201812");
                    setAmount(310);
                }},
                new Budget() {{
                    setYearMonth("202001");
                    setAmount(31);
                }}
        );

        LocalDate startDate = LocalDate.parse("2018-12-31");
        LocalDate endDate = LocalDate.parse("2020-01-01");

        assertEquals(11, budgetService.queryBudget(startDate, endDate));
    }

    private void givenBudgets(Budget... budgets) {
        stubRepo.setAll(Arrays.asList(budgets));
    }

}