package org.neoj4.movieservice.model.dto;

public record ApiResponse<T>(
        T data ,
        String message,
        int status

) {


    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data , "success" , 200);
    }


    public static <T> ApiResponse<T> error(String message , int status) {
        return new ApiResponse<>(null , message , status);
    }


}
