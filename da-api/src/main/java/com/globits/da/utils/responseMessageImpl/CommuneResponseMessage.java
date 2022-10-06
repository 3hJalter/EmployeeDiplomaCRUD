package com.globits.da.utils.responseMessageImpl;

import com.globits.da.utils.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommuneResponseMessage implements ResponseMessage {
    COMMUNE_DOES_NOT_EXIST("400_P0", "Commune does not exist"),
    COMMUNE_CODE_IS_NULL("400_P1", "Commune code is null"),
    COMMUNE_CODE_IS_DUPLICATED("400_P2", "Commune code is duplicated with other"),
    COMMUNE_CODE_HAS_WHITESPACE("400_P3", "Commune code must not have whitespace"),
    COMMUNE_NAME_IS_NULL("400_P4", "Commune name must not be null"),
    COMMUNE_DOES_NOT_HAVE_DISTRICT("400_P5", "Commune must have a valid district id"),
    SUCCESSFUL("200", "Successful");
    private final String code;
    private final String message;
}
