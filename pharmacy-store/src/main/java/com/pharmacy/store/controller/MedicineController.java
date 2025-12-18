package com.pharmacy.store.controller;

import com.pharmacy.store.model.Medicine;
import com.pharmacy.store.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicines")
@CrossOrigin(origins = "*")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    // Basic CRUD Operations

    @GetMapping
    public ResponseEntity<List<Medicine>> getAllMedicines() {
        try {
            List<Medicine> medicines = medicineService.getAllMedicines();
            return ResponseEntity.ok(medicines);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Medicine>> getAllMedicinesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                       Sort.by(sortBy).descending() : 
                       Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Medicine> medicines = medicineService.getAllMedicines(pageable);
            return ResponseEntity.ok(medicines);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        try {
            Medicine medicine = medicineService.getMedicineById(id);
            return ResponseEntity.ok(medicine);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Medicine> createMedicine(@Valid @RequestBody Medicine medicine) {
        try {
            Medicine savedMedicine = medicineService.saveMedicine(medicine);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMedicine);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(@PathVariable Long id, 
                                                 @Valid @RequestBody Medicine medicine) {
        try {
            Medicine updatedMedicine = medicineService.updateMedicine(id, medicine);
            return ResponseEntity.ok(updatedMedicine);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteMedicine(@PathVariable Long id) {
        try {
            medicineService.deleteMedicine(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Medicine deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search Operations

    @GetMapping("/search/name/{name}")
    public ResponseEntity<Medicine> getMedicineByName(@PathVariable String name) {
        try {
            Optional<Medicine> medicine = medicineService.findMedicineByName(name);
            return medicine.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Medicine>> searchMedicines(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean prescriptionRequired) {
        try {
            List<Medicine> medicines = medicineService.searchMedicines(
                name, manufacturer, category, minPrice, maxPrice, prescriptionRequired);
            return ResponseEntity.ok(medicines);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Stock Management

    @GetMapping("/low-stock")
    public ResponseEntity<List<Medicine>> getLowStockMedicines(
            @RequestParam(defaultValue = "10") int threshold) {
        try {
            List<Medicine> medicines = medicineService.getLowStockMedicines(threshold);
            return ResponseEntity.ok(medicines);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Medicine> updateStock(@PathVariable Long id, 
                                              @RequestParam int quantity) {
        try {
            Medicine medicine = medicineService.updateStock(id, quantity);
            return ResponseEntity.ok(medicine);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Sales Operations
    @PostMapping("/{id}/sale")
    public ResponseEntity<Map<String, Object>> processSale(@PathVariable Long id, 
                                                         @RequestParam int quantity) {
        try {
            Medicine medicine = medicineService.processSale(id, quantity);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Sale processed successfully");
            response.put("medicine", medicine);
            response.put("soldQuantity", quantity);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Analytics
    @GetMapping("/analytics/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardAnalytics() {
        try {
            Map<String, Object> analytics = new HashMap<>();
            analytics.put("totalMedicines", medicineService.getTotalMedicineCount());
            analytics.put("expiredMedicines", medicineService.getExpiredMedicineCount());
            analytics.put("lowStockMedicines", medicineService.getLowStockMedicineCount(10));
            analytics.put("totalInventoryValue", medicineService.getTotalInventoryValue());
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/expired")
    public ResponseEntity<List<Medicine>> getExpiredMedicines() {
        try {
            List<Medicine> medicines = medicineService.getExpiredMedicines();
            return ResponseEntity.ok(medicines);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}