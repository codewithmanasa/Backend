package com.userapp.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String message;

//	public MessageResponse(String message) {
//		super();
//		this.message = message;
//	}
//
//	public String getMessage() {
//		return message;
//	}
    
}
