package com.globits.da.utils;

import com.globits.da.utils.responseMessageImpl.CommuneResponseMessage;
import com.globits.da.utils.responseMessageImpl.DistrictResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {
    private T object;
    private String errorCode;
    private String errorMessage;

    public Response(T object, CommuneResponseMessage communeResponse) {
        this.object = object;
        this.errorCode = communeResponse.getCode();
        this.errorMessage = communeResponse.getMessage();
    }

    public Response(T object, DistrictResponseMessage districtResponse) {
        this.object = object;
        this.errorCode = districtResponse.getCode();
        this.errorMessage = districtResponse.getMessage();
    }

    public Response(T object, ResponseMessage responseMessage) {
        this.object = object;
        this.errorCode = responseMessage.getCode();
        this.errorMessage = responseMessage.getMessage();
    }
}
