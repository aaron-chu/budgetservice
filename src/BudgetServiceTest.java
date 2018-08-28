import org.junit.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BudgetServiceTest {

    private BudgetService budgetService = new BudgetService();

    @Test
    public void same_date() {
        LocalDate today = LocalDate.now();

        assertEquals(budgetService.queryBudget(today, today), 1);
    }

}