package com.pharmacy.store.repository;

import com.pharmacy.store.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    
    // Find by name (case-insensitive)
    Optional<Medicine> findByNameIgnoreCase(String name);
    
    // Find by manufacturer
    List<Medicine> findByManufacturerIgnoreCase(String manufacturer);
    
    // Find by category
    List<Medicine> findByCategoryIgnoreCase(String category);
    
    // Find medicines requiring prescription
    List<Medicine> findByPrescriptionRequiredTrue();
    
    // Find medicines not requiring prescription
    List<Medicine> findByPrescriptionRequiredFalse();
    
    // Find by price range
    List<Medicine> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Find by stock quantity less than threshold (low stock)
    List<Medicine> findByStockQuantityLessThan(Integer threshold);
    
    // Find expired medicines
    List<Medicine> findByExpiryDateBefore(LocalDate date);
    
    // Find medicines expiring soon
    List<Medicine> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Search by name containing (partial match)
    List<Medicine> findByNameContainingIgnoreCase(String nameFragment);
    
    // Custom query to find medicines by multiple criteria
    @Query("SELECT m FROM Medicine m WHERE " +
           "(:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:manufacturer IS NULL OR LOWER(m.manufacturer) = LOWER(:manufacturer)) AND " +
           "(:category IS NULL OR LOWER(m.category) = LOWER(:category)) AND " +
           "(:minPrice IS NULL OR m.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR m.price <= :maxPrice) AND " +
           "(:prescriptionRequired IS NULL OR m.prescriptionRequired = :prescriptionRequired)")
    List<Medicine> findMedicinesByCriteria(
            @Param("name") String name,
            @Param("manufacturer") String manufacturer,
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("prescriptionRequired") Boolean prescriptionRequired
    );
    
    // Native SQL query to get medicine statistics
    @Query(value = "SELECT COUNT(*) FROM medicines WHERE expiry_date < CURRENT_DATE", nativeQuery = true)
    long countExpiredMedicines();
    
    @Query(value = "SELECT COUNT(*) FROM medicines WHERE stock_quantity < :threshold", nativeQuery = true)
    long countLowStockMedicines(@Param("threshold") int threshold);
    
    // Get total value of inventory
    @Query("SELECT COALESCE(SUM(m.price * m.stockQuantity), 0) FROM Medicine m")
    BigDecimal getTotalInventoryValue();
    
    // Find top medicines by stock quantity
    List<Medicine> findTop10ByOrderByStockQuantityDesc();
    
    // Find medicines by batch number
    Optional<Medicine> findByBatchNumber(String batchNumber);
    
    // Delete expired medicines
    void deleteByExpiryDateBefore(LocalDate date);
    
    // Check if medicine exists by name
    boolean existsByNameIgnoreCase(String name);
}