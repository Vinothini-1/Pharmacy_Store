package com.pharmacy.store.service;

import com.pharmacy.store.model.Medicine;
import com.pharmacy.store.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicineService {
    
    @Autowired
    private MedicineRepository medicineRepository;
    
    // Basic CRUD Operations
    
    public Medicine saveMedicine(Medicine medicine) {
        validateMedicine(medicine);
        return medicineRepository.save(medicine);
    }
    
    public Medicine updateMedicine(Long id, Medicine updatedMedicine) {
        Medicine existingMedicine = getMedicineById(id);
        
        // Update fields
        existingMedicine.setName(updatedMedicine.getName());
        existingMedicine.setDescription(updatedMedicine.getDescription());
        existingMedicine.setManufacturer(updatedMedicine.getManufacturer());
        existingMedicine.setPrice(updatedMedicine.getPrice());
        existingMedicine.setStockQuantity(updatedMedicine.getStockQuantity());
        existingMedicine.setExpiryDate(updatedMedicine.getExpiryDate());
        existingMedicine.setCategory(updatedMedicine.getCategory());
        existingMedicine.setPrescriptionRequired(updatedMedicine.getPrescriptionRequired());
        existingMedicine.setBatchNumber(updatedMedicine.getBatchNumber());
        
        validateMedicine(existingMedicine);
        return medicineRepository.save(existingMedicine);
    }
    
    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
    }
    
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }
    
    public Page<Medicine> getAllMedicines(Pageable pageable) {
        return medicineRepository.findAll(pageable);
    }
    
    public void deleteMedicine(Long id) {
        Medicine medicine = getMedicineById(id);
        medicineRepository.delete(medicine);
    }
    
    // Search Operations
    
    public Optional<Medicine> findMedicineByName(String name) {
        return medicineRepository.findByNameIgnoreCase(name);
    }
    
    public List<Medicine> findMedicinesByManufacturer(String manufacturer) {
        return medicineRepository.findByManufacturerIgnoreCase(manufacturer);
    }
    
    public List<Medicine> findMedicinesByCategory(String category) {
        return medicineRepository.findByCategoryIgnoreCase(category);
    }
    
    public List<Medicine> searchMedicinesByName(String nameFragment) {
        return medicineRepository.findByNameContainingIgnoreCase(nameFragment);
    }
    
    public List<Medicine> findMedicinesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return medicineRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    public List<Medicine> findPrescriptionMedicines() {
        return medicineRepository.findByPrescriptionRequiredTrue();
    }
    
    public List<Medicine> findOverTheCounterMedicines() {
        return medicineRepository.findByPrescriptionRequiredFalse();
    }
    
    // Advanced Search
    public List<Medicine> searchMedicines(String name, String manufacturer, String category, 
                                        BigDecimal minPrice, BigDecimal maxPrice, 
                                        Boolean prescriptionRequired) {
        return medicineRepository.findMedicinesByCriteria(
                name, manufacturer, category, minPrice, maxPrice, prescriptionRequired);
    }
    
    // Stock Management
    
    public List<Medicine> getLowStockMedicines(int threshold) {
        return medicineRepository.findByStockQuantityLessThan(threshold);
    }
    
    public Medicine updateStock(Long medicineId, int quantity) {
        Medicine medicine = getMedicineById(medicineId);
        medicine.updateStock(quantity);
        return medicineRepository.save(medicine);
    }
    
    public Medicine reduceStock(Long medicineId, int quantity) {
        return updateStock(medicineId, -quantity);
    }
    
    public Medicine increaseStock(Long medicineId, int quantity) {
        return updateStock(medicineId, quantity);
    }
    
    // Expiry Management
    
    public List<Medicine> getExpiredMedicines() {
        return medicineRepository.findByExpiryDateBefore(LocalDate.now());
    }
    
    public List<Medicine> getMedicinesExpiringSoon(int days) {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(days);
        return medicineRepository.findByExpiryDateBetween(today, futureDate);
    }
    
    @Transactional
    public int removeExpiredMedicines() {
        List<Medicine> expiredMedicines = getExpiredMedicines();
        int count = expiredMedicines.size();
        medicineRepository.deleteByExpiryDateBefore(LocalDate.now());
        return count;
    }
    
    // Analytics and Reports
    
    public long getTotalMedicineCount() {
        return medicineRepository.count();
    }
    
    public long getExpiredMedicineCount() {
        return medicineRepository.countExpiredMedicines();
    }
    
    public long getLowStockMedicineCount(int threshold) {
        return medicineRepository.countLowStockMedicines(threshold);
    }
    
    public BigDecimal getTotalInventoryValue() {
        return medicineRepository.getTotalInventoryValue();
    }
    
    public List<Medicine> getTopStockedMedicines() {
        return medicineRepository.findTop10ByOrderByStockQuantityDesc();
    }
    
    // Business Logic Methods
    
    public boolean isMedicineAvailable(Long medicineId, int requestedQuantity) {
        Medicine medicine = getMedicineById(medicineId);
        return medicine.getStockQuantity() >= requestedQuantity && !medicine.isExpired();
    }
    
    public Medicine processSale(Long medicineId, int quantity) {
        Medicine medicine = getMedicineById(medicineId);
        
        if (medicine.isExpired()) {
            throw new RuntimeException("Cannot sell expired medicine: " + medicine.getName());
        }
        
        if (medicine.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + medicine.getStockQuantity() + 
                                     ", Requested: " + quantity);
        }
        
        medicine.updateStock(-quantity);
        return medicineRepository.save(medicine);
    }
    
    // Validation
    
    private void validateMedicine(Medicine medicine) {
        if (medicine.getName() == null || medicine.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Medicine name is required");
        }
        
        if (medicine.getPrice() == null || medicine.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Medicine price must be greater than zero");
        }
        
        if (medicine.getStockQuantity() == null || medicine.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        
        if (medicine.getExpiryDate() == null || medicine.getExpiryDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiry date must be in the future");
        }
        
        // Check for duplicate names (excluding current medicine in case of update)
        Optional<Medicine> existing = medicineRepository.findByNameIgnoreCase(medicine.getName());
        if (existing.isPresent() && !existing.get().getId().equals(medicine.getId())) {
            throw new IllegalArgumentException("Medicine with name '" + medicine.getName() + "' already exists");
        }
    }
    
    // Utility Methods
    
    public boolean medicineExists(Long id) {
        return medicineRepository.existsById(id);
    }
    
    public boolean medicineExistsByName(String name) {
        return medicineRepository.existsByNameIgnoreCase(name);
    }
}