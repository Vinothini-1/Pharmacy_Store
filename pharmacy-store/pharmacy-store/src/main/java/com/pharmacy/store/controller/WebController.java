package com.pharmacy.store.controller;

import com.pharmacy.store.model.Medicine;
import com.pharmacy.store.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/")
public class WebController {

    @Autowired
    private MedicineService medicineService;

    // Dashboard
    @GetMapping
    public String dashboard(Model model) {
        long totalMedicines = medicineService.getTotalMedicineCount();
        long expiredCount = medicineService.getExpiredMedicineCount();
        long lowStockCount = medicineService.getLowStockMedicineCount(10);
        BigDecimal totalValue = medicineService.getTotalInventoryValue();
        
        List<Medicine> lowStockMedicines = medicineService.getLowStockMedicines(10);
        List<Medicine> expiredMedicines = medicineService.getExpiredMedicines();

        model.addAttribute("totalMedicines", totalMedicines);
        model.addAttribute("expiredCount", expiredCount);
        model.addAttribute("lowStockCount", lowStockCount);
        model.addAttribute("totalValue", totalValue);
        model.addAttribute("lowStockMedicines", lowStockMedicines);
        model.addAttribute("expiredMedicines", expiredMedicines);

        return "dashboard";
    }

    // Medicine List
    @GetMapping("/medicines")
    public String listMedicines(Model model) {
        List<Medicine> medicines = medicineService.getAllMedicines();
        model.addAttribute("medicines", medicines);
        return "medicines/list";
    }

    // Add Medicine Form
    @GetMapping("/medicines/add")
    public String showAddMedicineForm(Model model) {
        model.addAttribute("medicine", new Medicine());
        return "medicines/form";
    }

    // View Medicine Details
    @GetMapping("/medicines/view/{id}")
    public String viewMedicine(@PathVariable Long id, Model model) {
        try {
            Medicine medicine = medicineService.getMedicineById(id);
            model.addAttribute("medicine", medicine);
            return "medicines/view";
        } catch (RuntimeException e) {
            return "redirect:/medicines?error=Medicine not found";
        }
    }
}