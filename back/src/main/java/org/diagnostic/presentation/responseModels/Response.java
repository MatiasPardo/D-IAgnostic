package org.diagnostic.presentation.responseModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class Response {
	
	@Schema(description = "Response internal code")
	@JsonProperty("code")
	private String code;
	
	@Schema(description = "Response message")
	@JsonProperty("message") 
	private String message;
	
	@Schema(description = "Response Date")
	@JsonProperty("date")
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime date;

	@JsonCreator
	public Response(String code,String message,LocalDateTime date) {
		this.code = code;
		this.message = message;
		this.date = date;
	}
	
	@JsonCreator
	public Response() {
		this("200", "Request processed successfully", LocalDateTime.now());
	}
	
	
	

}
