package org.tptacs.infraestructure.repositories;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import org.tptacs.domain.entities.Item;
import org.tptacs.domain.exceptions.ResourceNotFoundException;
import org.tptacs.infraestructure.repositories.interfaces.IItemsRepository;

@Repository
public class ItemsRepository extends FileRepository<Item> implements IItemsRepository {
    public ItemsRepository() {
        super(ItemsRepository.class.getSimpleName(), Item.class);
        if (count() == 0) {
            loadMockData();
        }
    }

    public void save(Item item) {
        super.put(item.getId(), item);
    }

    public Item get(String id) {
        var item = super.get(id);
        if (item == null) throw new ResourceNotFoundException(id, "item");
        return item;
    }

    @Override
    public void exists(String id) {
        if (!super.exist(id)) throw new ResourceNotFoundException(id, "item");
    }

    @Override
    public List<Item> getAll() {
        return values();
    }

    @SneakyThrows
    private void loadMockData() {
        var path = getRepositoryPath("/mocks/ItemsData.json");
        var file = new File(path);
        List<Item> itemsMock = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Item.class));
        itemsMock.forEach(this::save);
    }
}
