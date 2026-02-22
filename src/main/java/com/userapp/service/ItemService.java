package com.userapp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userapp.model.Item;
import com.userapp.model.User;
import com.userapp.payload.request.ItemRequest;
import com.userapp.payload.response.ItemResponse;
import com.userapp.repository.ItemRepository;
import com.userapp.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<ItemResponse> getAllItemsByUser(Long userId) {
        return itemRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public ItemResponse getItemById(Long id, Long userId) {
        Item item = itemRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        return convertToResponse(item);
    }
    
    public ItemResponse createItem(ItemRequest itemRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Item item = new Item();
        item.setTitle(itemRequest.getTitle());
        item.setDescription(itemRequest.getDescription());
        item.setCompleted(itemRequest.getCompleted() != null ? itemRequest.getCompleted() : false);
        item.setUser(user);
        
        Item savedItem = itemRepository.save(item);
        return convertToResponse(savedItem);
    }
    
    public ItemResponse updateItem(Long id, ItemRequest itemRequest, Long userId) {
        Item item = itemRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        
        item.setTitle(itemRequest.getTitle());
        item.setDescription(itemRequest.getDescription());
        item.setCompleted(itemRequest.getCompleted());
        
        Item updatedItem = itemRepository.save(item);
        return convertToResponse(updatedItem);
    }
    
    @Transactional
    public void deleteItem(Long id, Long userId) {
        Item item = itemRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        itemRepository.delete(item);
    }
    
    private ItemResponse convertToResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setTitle(item.getTitle());
        response.setDescription(item.getDescription());
        response.setCompleted(item.getCompleted());
        response.setUserId(item.getUser().getId());
        response.setUsername(item.getUser().getUsername());
        response.setCreatedAt(item.getCreatedAt());
        response.setUpdatedAt(item.getUpdatedAt());
        return response;
    }
}
