package com.mizzle.blogrest.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ApiResponse {

    @Schema( type = "boolean", example = "true")
    private boolean check;
    
    private Object information;
    
    public ApiResponse(){};

    @Builder
    public ApiResponse(boolean check, Object information) {
        this.check = check;
        this.information = information;
    }
}
