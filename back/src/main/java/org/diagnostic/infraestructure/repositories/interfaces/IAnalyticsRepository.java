package org.diagnostic.infraestructure.repositories.interfaces;

public interface IAnalyticsRepository {
    void addUser();
    void addOrder();
    long countUsers();
    long countOrders();
}
