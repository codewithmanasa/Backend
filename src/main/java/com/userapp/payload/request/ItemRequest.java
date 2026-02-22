package com.userapp.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemRequest {
    
    @NotBlank
    @Size(max = 100)
    private String title;
    
    @Size(max = 500)
    private String description;
    
    private Boolean completed = false;

	public ItemRequest(@NotBlank @Size(max = 100) String title, @Size(max = 500) String description,
			Boolean completed) {
		super();
		this.title = title;
		this.description = description;
		this.completed = completed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
    
    
}
