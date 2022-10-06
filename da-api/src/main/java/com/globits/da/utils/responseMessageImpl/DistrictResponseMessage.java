package com.globits.da.utils.responseMessageImpl;

import com.globits.da.utils.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DistrictResponseMessage implements ResponseMessage {
    DISTRICT_DOES_NOT_EXIST("400_D0", "District does not exist"),
    DISTRICT_CODE_IS_NULL("400_D1", "District code is null"),
    DISTRICT_CODE_IS_DUPLICATED("400_D2", "District code is duplicated with other"),
    DISTRICT_CODE_HAS_WHITESPACE("400_D3", "District code must not have whitespace"),
    DISTRICT_NAME_IS_NULL("400_D4", "District name must not be null"),
    DISTRICT_DOES_NOT_HAVE_PROVINCE("400_D5", "District must have a valid province id"),
    DISTRICT_DOES_NOT_HAVE_COMMUNE("200_D1", "Successful but district does not have any commune"),
    SUCCESSFUL("200_D0", "Successful");
    private final String code;
    private final String message;
}
