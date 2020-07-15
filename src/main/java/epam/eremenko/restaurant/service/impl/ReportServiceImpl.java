package epam.eremenko.restaurant.service.impl;

import epam.eremenko.restaurant.dao.Dao;
import epam.eremenko.restaurant.dao.exception.DaoException;
import epam.eremenko.restaurant.dao.impl.DaoFactory;
import epam.eremenko.restaurant.dao.table.OrderTable;
import epam.eremenko.restaurant.dto.ReportDto;
import epam.eremenko.restaurant.entity.BeanFactory;
import epam.eremenko.restaurant.entity.Order;
import epam.eremenko.restaurant.entity.Report;
import epam.eremenko.restaurant.service.ReportService;
import epam.eremenko.restaurant.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class ReportServiceImpl implements ReportService {
    private final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final Dao<ReportDto, OrderTable> REPORT_DAO = DaoFactory.getInstance().getReporter();

    @Override
    public Report get(ReportDto reportDto) throws ServiceException {
        try {
            reportDto = REPORT_DAO.get(reportDto);
        } catch (DaoException ex) {
            LOGGER.debug(ex.toString());
            throw new ServiceException(ex.getMessage());
        }
        return BeanFactory.getInstance().getReport(reportDto);
    }

    public Report get(ReportDto reportDto, int currentPage, int recordsPerPage) throws ServiceException{
        Report report = get(reportDto);
        cutReport(report, currentPage, recordsPerPage);
        return report;
    }

    private void cutReport(Report report, int currentPage, int recordsPerPage) {
        int startPosition = (currentPage - 1) * recordsPerPage;
        int threshold = setThreshold(report.getOrders().size(), startPosition, recordsPerPage);
        report.setOrders(collectPartOrdersOfReport(report, startPosition, threshold));
    }

    private int setThreshold(int menuSize, int startPosition, int recordsPerPage) {
        int threshold = startPosition + recordsPerPage;
        return Math.min(menuSize, threshold);
    }

    private List<Order> collectPartOrdersOfReport(Report report, int startPosition, int threshold) {
        List<Order> result = new ArrayList<>();
        for (int i = startPosition; i < threshold; i++) {
            result.add(report.getOrders().get(i));
        }
        return result;
    }

    public int getQuantityOfPages(Report report, int recordsPerPage) {
        int ordersQuantity = report.getOrdersQuantity();
        return (int) Math.ceil(ordersQuantity * 1.0 / recordsPerPage);
    }
}
