package org.tptacs.infraestructure.repositories.mongoImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.tptacs.domain.entities.Item;
import org.tptacs.domain.exceptions.ResourceNotFoundException;
import org.tptacs.infraestructure.repositories.interfaces.IItemsRepository;
import org.tptacs.infraestructure.repositories.mongoImpl.interfaces.IItemsMongoRepository;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Repository
@Primary
public class ItemsMongoRepository implements IItemsRepository {
    @Autowired
    private IItemsMongoRepository IItemsMongoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void save(Item item) {
        IItemsMongoRepository.save(item);
    }

    public Item get(String id) {
        var item = IItemsMongoRepository.findById(id);
        if (item.isEmpty()) throw new ResourceNotFoundException(id, "item");
        return item.get();
    }

    @Override
    public void exists(String id) {
        if (!IItemsMongoRepository.existsById(id)) throw new ResourceNotFoundException(id, "item");
    }

    @Override
    public List<Item> getAll() {
        return IItemsMongoRepository.findAll();
    }

    @SneakyThrows
    @PostConstruct
    private void loadMockData() {
        if (IItemsMongoRepository.count() != 0) {
            return;
        }
        var path = getRepositoryPath("/mocks/ItemsData.json");
        var file = new File(path);
        List<Item> itemsMock = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Item.class));
        itemsMock.forEach(this::save);
    }

    private String getRepositoryPath(String name) {
        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
        Path filePath = Paths.get(root.toString(),"src", "main", "resources", "data", name);
        return filePath.toString();
    }
}
