package com.sboot.api.dental_clinic_api.repository;

import com.sboot.api.dental_clinic_api.entity.RecurringExpense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, String> {

    @Query("SELECT re FROM RecurringExpense re " +
            "WHERE (:search IS NULL OR :search = '' " +
            "OR LOWER(re.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(re.category) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR CAST(re.dueDay AS string) LIKE CONCAT('%', :search, '%'))")
    Page<RecurringExpense> findAllByFilters(@Param("search") String search, Pageable pageable);

    List<RecurringExpense> findByIsActiveTrue();

    List<RecurringExpense> findByCategory(String category);

    List<RecurringExpense> findByDueDay(Integer dueDay);
}
