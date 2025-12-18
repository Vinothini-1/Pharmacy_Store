package com.pharmacy.store.service;

import com.pharmacy.store.model.Medicine;
import com.pharmacy.store.repository.MedicineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicineServiceTest {

    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private MedicineService medicineService;

    private Medicine testMedicine;

    @BeforeEach
    void setUp() {
        testMedicine = new Medicine();
        testMedicine.setId(1L);
        testMedicine.setName("Test Medicine");
        testMedicine.setManufacturer("Test Manufacturer");
        testMedicine.setPrice(new BigDecimal("100.00"));
        testMedicine.setStockQuantity(50);
        testMedicine.setExpiryDate(LocalDate.now().plusMonths(6));
        testMedicine.setCategory("Test Category");
        testMedicine.setPrescriptionRequired(false);
        testMedicine.setBatchNumber("TEST001");
    }

    @Test
    void testSaveMedicine_Success() {
        // Given
        when(medicineRepository.findByNameIgnoreCase(testMedicine.getName()))
                .thenReturn(Optional.empty());
        when(medicineRepository.save(testMedicine)).thenReturn(testMedicine);

        // When
        Medicine savedMedicine = medicineService.saveMedicine(testMedicine);

        // Then
        assertNotNull(savedMedicine);
        assertEquals(testMedicine.getName(), savedMedicine.getName());
        verify(medicineRepository).save(testMedicine);
    }

    @Test
    void testGetMedicineById_Success() {
        // Given
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(testMedicine));

        // When
        Medicine foundMedicine = medicineService.getMedicineById(1L);

        // Then
        assertNotNull(foundMedicine);
        assertEquals(testMedicine.getId(), foundMedicine.getId());
        assertEquals(testMedicine.getName(), foundMedicine.getName());
    }

    @Test
    void testProcessSale_Success() {
        // Given
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(testMedicine));
        when(medicineRepository.save(any(Medicine.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Medicine soldMedicine = medicineService.processSale(1L, 10);

        // Then
        assertEquals(40, soldMedicine.getStockQuantity()); // 50 - 10
        verify(medicineRepository).save(any(Medicine.class));
    }

    @Test
    void testGetLowStockMedicines() {
        // Given
        Medicine lowStockMedicine = new Medicine();
        lowStockMedicine.setName("Low Stock Medicine");
        lowStockMedicine.setStockQuantity(5);

        List<Medicine> lowStockMedicines = Arrays.asList(lowStockMedicine);
        when(medicineRepository.findByStockQuantityLessThan(10))
                .thenReturn(lowStockMedicines);

        // When
        List<Medicine> result = medicineService.getLowStockMedicines(10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(lowStockMedicine.getName(), result.get(0).getName());
    }
}