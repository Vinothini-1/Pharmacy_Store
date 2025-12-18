-- Sample data for Pharmacy Store
INSERT INTO medicines (name, description, manufacturer, price, stock_quantity, expiry_date, category, prescription_required, batch_number, created_at, updated_at) VALUES
-- Pain Relief Medicines
('Paracetamol 500mg', 'Pain relief and fever reducer', 'Sun Pharma', 25.50, 150, '2025-12-31', 'Pain Relief', false, 'PAR001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ibuprofen 400mg', 'Anti-inflammatory pain relief', 'Cipla', 45.75, 100, '2025-11-30', 'Pain Relief', false, 'IBU001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Aspirin 300mg', 'Pain relief and blood thinner', 'Bayer', 35.25, 200, '2026-01-15', 'Pain Relief', false, 'ASP001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Antibiotics (Prescription Required)
('Amoxicillin 500mg', 'Broad spectrum antibiotic', 'Ranbaxy', 120.00, 75, '2025-10-30', 'Antibiotics', true, 'AMX001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Azithromycin 250mg', 'Macrolide antibiotic', 'Dr. Reddy', 180.50, 50, '2025-09-15', 'Antibiotics', true, 'AZI001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ciprofloxacin 500mg', 'Fluoroquinolone antibiotic', 'Lupin', 95.75, 60, '2025-08-20', 'Antibiotics', true, 'CIP001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Cold & Cough
('Cough Syrup', 'Relief from dry and wet cough', 'Dabur', 65.00, 80, '2026-03-15', 'Cold & Cough', false, 'CSY001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lozenges Honey', 'Throat soothing lozenges', 'Strepsils', 40.25, 120, '2026-02-28', 'Cold & Cough', false, 'LOZ001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Digestive Health
('Antacid Tablets', 'Relief from acidity and heartburn', 'Eno', 30.75, 180, '2026-04-30', 'Digestive', false, 'ANT001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Probiotic Capsules', 'Digestive health support', 'Yakult', 250.00, 35, '2025-11-15', 'Digestive', false, 'PRO001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Vitamins & Supplements
('Vitamin C 1000mg', 'Immune system support', 'Nature Made', 450.00, 90, '2026-06-30', 'Vitamins', false, 'VTC001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Vitamin D3 2000 IU', 'Bone health support', 'Carlson Labs', 380.75, 70, '2026-05-15', 'Vitamins', false, 'VTD001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Multivitamin Tablets', 'Complete nutrition support', 'Centrum', 520.50, 55, '2026-07-20', 'Vitamins', false, 'MLT001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Diabetes Care (Prescription Required)
('Metformin 500mg', 'Type 2 diabetes medication', 'Glenmark', 85.00, 120, '2026-01-31', 'Diabetes', true, 'MET001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Glimepiride 2mg', 'Diabetes control medication', 'Torrent', 125.50, 80, '2025-12-20', 'Diabetes', true, 'GLM001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Blood Pressure (Prescription Required)
('Amlodipine 5mg', 'Calcium channel blocker', 'Alkem', 65.75, 95, '2026-02-15', 'Cardiovascular', true, 'AML001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Losartan 50mg', 'ARB for hypertension', 'Micro Labs', 110.25, 85, '2025-12-25', 'Cardiovascular', true, 'LOS001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Skin Care
('Antiseptic Cream', 'Wound healing and infection prevention', 'Savlon', 45.00, 75, '2026-08-15', 'Skin Care', false, 'ASC001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Moisturizing Lotion', 'Dry skin treatment', 'Cetaphil', 320.75, 40, '2026-09-30', 'Skin Care', false, 'MOL001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Low Stock Items (for testing)
('Emergency Inhaler', 'Asthma relief inhaler', 'GSK', 450.00, 5, '2025-11-20', 'Respiratory', true, 'INH001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Rare Medicine X', 'Specialty medication', 'Specialty Pharma', 1250.00, 3, '2025-12-05', 'Specialty', true, 'RMX001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Expired Items (for testing - Note: These dates are in the past)
('Expired Cough Syrup', 'Old stock cough syrup', 'Generic', 50.00, 10, '2024-12-31', 'Cold & Cough', false, 'EXP001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Old Painkiller', 'Expired pain relief tablets', 'Generic', 30.00, 8, '2024-11-15', 'Pain Relief', false, 'EXP002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);