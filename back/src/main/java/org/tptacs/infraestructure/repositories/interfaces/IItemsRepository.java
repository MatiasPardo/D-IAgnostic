package org.tptacs.infraestructure.repositories.interfaces;

import org.tptacs.domain.entities.Item;

import java.util.List;

public interface IItemsRepository {
    void save(Item item);
    Item get(String id);
    void exists(String id);
    List<Item> getAll();
}
