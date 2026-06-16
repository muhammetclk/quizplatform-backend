
package com.quizplatform.quizservice.exception;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class ApiResponse<T> {

 private boolean success;

 private String message;

 private T data;

 private LocalDateTime timestamp;


 public static <T> ApiResponse<T> success(T data, String message) {
  ApiResponse<T> response = new ApiResponse<>();
  response.setSuccess(true);
  response.setMessage(message);
  response.setData(data);
  response.setTimestamp(LocalDateTime.now());
  return response;
 }

 public static <T> ApiResponse<T> error(String message) {
  ApiResponse<T> response = new ApiResponse<>();
  response.setSuccess(false);
  response.setMessage(message);
  response.setData(null);
  response.setTimestamp(LocalDateTime.now());
  return response;
 }




    
}