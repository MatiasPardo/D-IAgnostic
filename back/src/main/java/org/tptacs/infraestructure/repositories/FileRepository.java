package org.tptacs.infraestructure.repositories;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.tptacs.infraestructure.config.ObjectMapperConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileRepository<T> {
    protected ObjectMapper objectMapper = new ObjectMapperConfiguration().getObjectMapper();
    private String name = "";
    private Class<T> type;

    public FileRepository(String name, Class<T> type) {
        this.name =  "/repositories/" + name + ".json";
        this.type = type;
    }

    private List<KvsItem> read() {
        return getFileData();
    }

    private void write(List<KvsItem> data) {
        try {
            objectMapper.writeValue(new File(getRepositoryPath(this.name)), data);
        } catch (IOException e) {
            throw new RuntimeException("Error al acceder al repositorio: " + name);
        }
    }

    protected void put(String id, Object data) {
        var kvsItem = new KvsItem(id, data);
        var items = read();
        items.add(kvsItem);
        write(items);
    }

    protected void replace(String id, Object data) {
        var kvsItem = new KvsItem(id, data);
        var items = read();
        var index = items.indexOf(kvsItem);
        items.set(index, kvsItem);
        write(items);
    }

    protected boolean exist(String id) {
        var items = read().stream().map(KvsItem::getKey).toList();
        var index = items.indexOf(id);
        return index != -1;
    }

    protected T get(String id) {
        var items = read();
        var item = items.stream().filter(i -> i.getKey().equals(id)).findFirst().orElse(null);
        return item != null ? build(item) : null;
    }

    protected List<T> values() {
        var items = read();
        if (items == null) return null;
        return items.stream().map(this::build).collect(Collectors.toList());
    }

    protected Long count() {
        var items = read();
        return items != null ? items.size() : 0L;
    }

    private T build(KvsItem kvsItem) {
        return objectMapper.convertValue(kvsItem.getValue(), type);
    }

    private List<KvsItem> getFileData() {
        List<KvsItem> loadedObject;
        var file = new File(getRepositoryPath(this.name));
        try {
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(getRepositoryPath(this.name));
                writer.write("[]");
                writer.close();
            }
            loadedObject = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, KvsItem.class));
        } catch (IOException e) {
            throw new RuntimeException("Error al acceder al repositorio: " + name);
        }
        return loadedObject;
    }

    protected String getRepositoryPath(String name) {
        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
        Path filePath = Paths.get(root.toString(),"src", "main", "resources", "data", name);
        return filePath.toString();
    }

	
}
