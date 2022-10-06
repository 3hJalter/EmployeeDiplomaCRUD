package com.globits.da.utils.responseMessageImpl;

import com.globits.da.utils.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiplomaResponseMessage implements ResponseMessage {
    DIPLOMA_DOES_NOT_EXIST("400_DIP0", "Diploma does not exist"),
    DIPLOMA_IS_NULL("400_DIP1", "Diploma is null"),
    DIPLOMA_NAME_IS_NULL("400_DIP2", "Diploma name is null"),
    DIPLOMA_NAME_IS_INVALID("400_DIP3", "Diploma name is invalid"),
    DIPLOMA_EFFECTIVE_DATE_IS_NULL("400_DIP4", "Diploma effective date is null"),
    DIPLOMA_EFFECTIVE_DATE_IS_INVALID("400_DIP5", "Diploma effective date is invalid"),
    DIPLOMA_EXPIRATION_DATE_IS_NULL("400_DIP6", "Diploma expiration date is null"),
    DIPLOMA_EXPIRATION_DATE_IS_INVALID("400_DIP7", "Diploma expiration date is invalid"),
    DIPLOMA_PROVINCE_IS_NULL("400_DIP8", "Diploma province is null"),
    DIPLOMA_PROVINCE_DOES_NOT_EXIST("400_DIP9", "Diploma province does not exist"),
    DIPLOMA_EMPLOYEE_IS_NULL("400_DIP10", "Diploma employee is null"),
    DIPLOMA_EMPLOYEE_DOES_NOT_EXIST("400_DIP11", "Diploma employee does not exist"),
    EMPLOYEE_HAS_THE_SAME_PROVINCE_DIPLOMA("400_DIP12", "Employee has the same diploma in this province"),
    EMPLOYEE_HAS_MORE_THAN_3_EFFECTIVE_DIPLOMAS("400_DIP13", "Employee has more than three effective diplomas"),
    DIPLOMA_DOES_NOT_EFFECTIVE("400_DIP14", "Diploma does not effective"),
    SUCCESSFUL("200", "Successful");
    private final String code;
    private final String message;
}
