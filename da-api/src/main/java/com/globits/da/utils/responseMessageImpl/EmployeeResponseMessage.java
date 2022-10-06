package com.globits.da.utils.responseMessageImpl;

import com.globits.da.utils.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmployeeResponseMessage implements ResponseMessage {

    EMPLOYEE_DOES_NOT_EXIST("400_E0", "Employee does not exist"),
    EMPLOYEE_CODE_IS_NULL("400_E1", "Employee code is null"),
    EMPLOYEE_CODE_IS_DUPLICATED("400_E2", "Employee code is duplicated with other"),
    EMPLOYEE_CODE_HAS_WHITESPACE("400_E3", "Employee code must not has whitespace"),
    EMPLOYEE_CODE_LENGTH_IS_INVALID("400_E4", "Employee code length must be between 6 and 10 characters"),
    EMPLOYEE_NAME_IS_NULL("400_E5", "Employee name must not be null"),
    EMPLOYEE_NAME_IS_INVALID("400_E6", "Employee name must only have characters"),
    EMPLOYEE_EMAIL_IS_NULL("400_E7", "Employee email must not be null"),
    EMPLOYEE_EMAIL_HAS_WHITESPACES("400_E8", "Employee email must not have whitespace"),
    EMPLOYEE_EMAIL_IS_INVALID("400_E9", "Employee email must have format abc@jkl.xyz"),
    EMPLOYEE_PHONE_IS_NULL("400_E10", "Employee phone must not be null"),
    EMPLOYEE_PHONE_IS_INVALID("400_E11", "Employee phone must have only and 9 to 11 digits"),
    EMPLOYEE_AGE_IS_NULL("400_E12", "Employee age must not be null"),
    EMPLOYEE_AGE_NOT_POSITIVE("400_E13", "Employee age must be positive number"),
    EMPLOYEE_DOES_NOT_HAVE_PROVINCE("400_E14", "Employee does not have province"),
    EMPLOYEE_DOES_NOT_HAVE_DISTRICT("400_E15", "Employee does not have district"),
    EMPLOYEE_DOES_NOT_HAVE_COMMUNE("400_E16", "Employee does not have commune"),
    COMMUNE_DOES_NOT_BELONG_TO_DISTRICT("400_E17", "Commune does not belong to district"),
    DISTRICT_DOES_NOT_BELONG_TO_PROVINCE("400_E18", "District does not belong to province"),
    EMPLOYEE_LIST_CAN_NOT_EXPORT_TO_EXCEL("400_E19", "Employee list cannot export to excel"),
    CAN_NOT_IMPORT_THE_FILE("400_E20", "File cannot be imported"),
    FILE_IS_NOT_EXCEL_FILE("400_E21", "File is not a valid Excel file"),


    SUCCESSFUL("200_E0", "Successful");
    private final String code;
    private final String message;
}
