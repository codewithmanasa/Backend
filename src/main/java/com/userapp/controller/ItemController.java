package com.userapp.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.userapp.payload.request.ItemRequest;
import com.userapp.payload.response.ItemResponse;
import com.userapp.payload.response.MessageResponse;
import com.userapp.security.services.UserDetailsImpl;
import com.userapp.service.ItemService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/items")
public class ItemController {
    
    @Autowired
    private ItemService itemService;
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
    
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        Long userId = getCurrentUserId();
        List<ItemResponse> items = itemService.getAllItemsByUser(userId);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            ItemResponse item = itemService.getItemById(id, userId);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody ItemRequest itemRequest) {
        Long userId = getCurrentUserId();
        ItemResponse createdItem = itemService.createItem(itemRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequest itemRequest) {
        try {
            Long userId = getCurrentUserId();
            ItemResponse updatedItem = itemService.updateItem(id, itemRequest, userId);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            itemService.deleteItem(id, userId);
            return ResponseEntity.ok(new MessageResponse("Item deleted successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}