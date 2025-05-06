/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotorsproject.parts.controller;
import com.carmotorsproject.parts.model.Part;
import com.carmotorsproject.parts.model.PartDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class InventoryReportController {
    private PartDAO partDAO;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public InventoryReportController() {
        this.partDAO = new PartDAO();
    }

    public String generateStatusReport() {
        StringBuilder report = new StringBuilder("Spare Parts Report by State\n");
        report.append("================================\n\n");

        List<Part> parts = partDAO.findAll();
        parts.stream()
                .collect(Collectors.groupingBy(Part::getStatus))
                .forEach((status, statusParts) -> {
                    report.append("Estado: ").append(status).append("\n");
                    statusParts.forEach(part -> {
                        report.append("- ").append(part.getName())
                                .append(" (ID: ").append(part.getPartId()).append(")\n");
                    });
                    report.append("Total in this state:").append(statusParts.size()).append("\n\n");
                });

        return report.toString();
    }

    public String generateCriticalStockReport() {
        StringBuilder report = new StringBuilder("Critical Stock Report\n");
        report.append("================================\n\n");

        List<Part> parts = partDAO.findAll();
        List<Part> criticalStockParts = parts.stream()
                .filter(part -> part.getQuantityInStock() <= part.getMinimumStock())
                .toList();

        if (criticalStockParts.isEmpty()) {
            report.append("There are no spare parts in critical stock.\n");
        } else {
            criticalStockParts.forEach(part -> {
                report.append("- ").append(part.getName())
                        .append(" (ID: ").append(part.getPartId())
                        .append("), Stock Actual: ").append(part.getQuantityInStock())
                        .append(", Stock Mínimo: ").append(part.getMinimumStock()).append("\n");
            });
            report.append("Total spare parts with critical stock: ").append(criticalStockParts.size()).append("\n");
        }

        return report.toString();
    }

    public String generateExpirationReport() {
        StringBuilder report = new StringBuilder("Expired or Expired Spare Parts Report\n");
        report.append("================================\n\n");

        Date today = new Date();
        List<Part> parts = partDAO.findAll();
        List<Part> expirationParts = parts.stream()
                .filter(part -> part.getEstimatedLifespan() != null)
                .toList();

        if (expirationParts.isEmpty()) {
            report.append("There are no spare parts with a registered expiration date..\n");
            return report.toString();
        }

        boolean hasIssues = false;
        for (Part part : expirationParts) {
            long diffInMillies = part.getEstimatedLifespan().getTime() - today.getTime();
            long daysUntilExpiration = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (daysUntilExpiration < 0) {
                report.append("- ").append(part.getName())
                        .append(" (ID: ").append(part.getPartId())
                        .append(") ha caducado el ")
                        .append(dateFormat.format(part.getEstimatedLifespan())).append("\n");
                hasIssues = true;
            } else if (daysUntilExpiration <= 30) {
                report.append("- ").append(part.getName())
                        .append(" (ID: ").append(part.getPartId())
                        .append(") is about to expire (")
                        .append(daysUntilExpiration).append(" days remaining). Expiration date: ")
                        .append(dateFormat.format(part.getEstimatedLifespan())).append("\n");
                hasIssues = true;
            }
        }

        if (!hasIssues) {
            report.append("There are no expired or close-to-expiry spare parts.\n");
        }

        return report.toString();
    }
}