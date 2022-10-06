package com.globits.da.utils.responseMessageImpl;

import com.globits.da.utils.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProvinceResponseMessage implements ResponseMessage {
    PROVINCE_DOES_NOT_EXIST("400_P0", "Province does not exist"),
    PROVINCE_CODE_IS_NULL("400_P1", "Province code is null"),
    PROVINCE_CODE_IS_DUPLICATED("400_P2", "Province code is duplicated with other"),
    PROVINCE_CODE_HAS_WHITESPACE("400_P3", "Province code must not have whitespace"),
    PROVINCE_NAME_IS_NULL("400_P4", "Province name must not be null"),
    PROVINCE_DOES_NOT_HAVE_DISTRICT("200_D1", "Successful but province does not have any district"),
    SUCCESSFUL("200", "Successful");
    private final String code;
    private final String message;
}
