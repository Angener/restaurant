package epam.eremenko.restaurant.service;

import epam.eremenko.restaurant.dto.ReportDto;
import epam.eremenko.restaurant.entity.Report;
import epam.eremenko.restaurant.service.exception.ServiceException;

public interface ReportService {
    Report get(ReportDto reportDto) throws ServiceException;
    Report get(ReportDto reportDto, int currentPage, int recordsPerPage) throws ServiceException;
    int getQuantityOfPages(Report report, int recordsPerPage);
}
