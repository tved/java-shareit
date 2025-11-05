package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private Long currentId = 1L;
    private final HashMap<Long, Item> items = new HashMap<>();

    @Override
    public Item create(Item item) {
        item.setId(currentId);
        items.put(currentId, item);
        currentId++;
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> get(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> getItemsByUser(Long userId) {
        return items.values().stream().filter(it -> it.getOwner().equals(userId)).toList();
    }

    @Override
    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        String request = text.toLowerCase();
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(it ->
                        it.getName().toLowerCase().contains(request)
                        || it.getDescription().toLowerCase().contains(request)
                )
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return items.containsKey(id);
    }
}
