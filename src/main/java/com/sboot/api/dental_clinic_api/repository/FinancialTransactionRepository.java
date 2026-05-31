package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.dto.DashboardResponseDTO;
import com.sboot.api.dental_clinic_api.entity.FinancialTransaction;
import com.sboot.api.dental_clinic_api.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, String> {

    @Query("SELECT ft FROM FinancialTransaction ft " +
            "LEFT JOIN ft.patient p " +
            "WHERE (:search IS NULL OR :search = '' " +
            "OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(ft.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(ft.category) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:type IS NULL OR ft.type = :type) " +
            "AND (COALESCE(:startDate, ft.date) <= ft.date) " +
            "AND (COALESCE(:endDate, ft.date) >= ft.date)")
    Page<FinancialTransaction> findAllByFilters(@Param("search") String search,
                                                @Param("type") TransactionType type,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                Pageable pageable);

    List<FinancialTransaction> findByType(TransactionType type);

    List<FinancialTransaction> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<FinancialTransaction> findByTypeAndDateBetween(TransactionType type, LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT " +
            "COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END), 0) AS totalRevenues, " +
            "COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END), 0) AS totalExpenses, " +
            "COALESCE(SUM(CASE WHEN type = 'EXPENSE' AND recurring_expense_id IS NOT NULL THEN amount ELSE 0 END), 0) AS recurringExpenses, " +
            "COALESCE(SUM(CASE WHEN type = 'EXPENSE' AND recurring_expense_id IS NULL THEN amount ELSE 0 END), 0) AS nonRecurringExpenses, " +
            "COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END), 0) - " +
            "COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END), 0) AS netProfit " +
            "FROM financial_transactions " +
            "WHERE date BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    DashboardResponseDTO findDashboardByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Modifying
    @Query("DELETE FROM FinancialTransaction ft WHERE ft.patientProcedure.id = :patientProcedureId")
    void deleteByPatientProcedureId(@Param("patientProcedureId") String patientProcedureId);

    boolean existsByRecurringExpenseIdAndDateBetween(String recurringExpenseId, LocalDate startDate, LocalDate endDate);
}
