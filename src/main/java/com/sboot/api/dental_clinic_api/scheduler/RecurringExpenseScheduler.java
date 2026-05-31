package com.sboot.api.dental_clinic_api.scheduler;

import com.sboot.api.dental_clinic_api.dto.FinancialTransactionDTO;
import com.sboot.api.dental_clinic_api.entity.RecurringExpense;
import com.sboot.api.dental_clinic_api.enums.TransactionType;
import com.sboot.api.dental_clinic_api.repository.FinancialTransactionRepository;
import com.sboot.api.dental_clinic_api.repository.RecurringExpenseRepository;
import com.sboot.api.dental_clinic_api.service.FinancialTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecurringExpenseScheduler {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final FinancialTransactionService financialTransactionService;
    private final FinancialTransactionRepository financialTransactionRepository;

    @Scheduled(cron = "0 0 6 * * ?")
    public void generateMonthlyExpenses() {
        log.info("Running recurring expense scheduler - generating monthly expenses");

        List<RecurringExpense> activeExpenses = recurringExpenseRepository.findByIsActiveTrue();
        log.info("Found {} active recurring expenses", activeExpenses.size());

        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(now);
        LocalDate monthStart = currentMonth.atDay(1);
        LocalDate monthEnd = currentMonth.atEndOfMonth();

        for (RecurringExpense expense : activeExpenses) {
            try {
                boolean exists = financialTransactionRepository
                        .existsByRecurringExpenseIdAndDateBetween(expense.getId(), monthStart, monthEnd);

                if (exists) {
                    log.info("Financial transaction for recurring expense '{}' already exists for {}. Skipping.",
                            expense.getDescription(), currentMonth);
                    continue;
                }

                int dueDay = expense.getDueDay();
                int maxDay = now.lengthOfMonth();
                int day = Math.min(dueDay, maxDay);

                LocalDate transactionDate = LocalDate.of(now.getYear(), now.getMonthValue(), day);

                FinancialTransactionDTO dto = FinancialTransactionDTO.builder()
                        .type(TransactionType.EXPENSE)
                        .recurringExpenseId(expense.getId())
                        .description(expense.getDescription())
                        .amount(expense.getAmount())
                        .date(transactionDate)
                        .paymentMethod(expense.getPaymentMethod())
                        .category(expense.getCategory())
                        .notes(expense.getNotes())
                        .build();

                financialTransactionService.create(dto);
                log.info("Created financial transaction for recurring expense '{}' with date {}", expense.getDescription(), transactionDate);
            } catch (Exception e) {
                log.error("Error creating financial transaction for recurring expense '{}': {}", expense.getDescription(), e.getMessage(), e);
            }
        }

        log.info("Recurring expense scheduler completed");
    }
}
