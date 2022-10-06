package com.globits.da.utils;

import com.globits.core.dto.DepartmentDto;
import com.globits.da.Constants;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.utils.responseMessageImpl.EmployeeResponseMessage;
import com.globits.da.validation.EmployeeValidation;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ImportExportExcelUtil {

	//--------------------------------------- Other variable ----------------------------------//
    private static final Hashtable<String, Integer> hashStaffColumnConfig = new Hashtable<String, Integer>();
    private static final Hashtable<String, Integer> hashDepartmentColumnConfig = new Hashtable<String, Integer>();
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final DecimalFormat numberFormatter = new DecimalFormat("######################");
    private static final Hashtable<String, String> hashColumnPropertyConfig = new Hashtable<String, String>();

	//--------------------------------------- Main variable ----------------------------------//
	private static final ModelMapper modelMapper = new ModelMapper();
	private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final CommuneRepository communeRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeValidation employeeValidation;
    @Autowired
    public ImportExportExcelUtil(ProvinceRepository provinceRepository, DistrictRepository districtRepository,
                                 CommuneRepository communeRepository, EmployeeRepository employeeRepository,
                                 EmployeeValidation employeeValidation) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.communeRepository = communeRepository;
        this.employeeRepository = employeeRepository;
        this.employeeValidation = employeeValidation;
    }
	//------------------------------- Import Excel ----------------------------------//
	public ResponseMessage readExcel(MultipartFile file, List<String> errorList) {
		try {
			InputStream inputStream = file.getInputStream();
			Workbook workbook;
			String extension = FilenameUtils.getExtension(file.getOriginalFilename());
			if (Objects.equals(extension, "xlsx")) {
				workbook = new XSSFWorkbook(inputStream);
			} else if (Objects.equals(extension, "xls")) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				return EmployeeResponseMessage.FILE_IS_NOT_EXCEL_FILE;
			}
			Sheet sheet = workbook.getSheetAt(0);
			List<Employee> employeeList = new ArrayList<>();
			boolean skipLoop = false;
			for (Row nextRow : sheet) {
				if (nextRow.getRowNum() == 0) {
					continue;
				}
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				Employee employee = new Employee();
				outer:
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					Object cellValue = getCellValue(cell, workbook);
					if (StringUtils.isEmpty(cellValue.toString())) {
						continue;
					}
					int columnIndex = cell.getColumnIndex();
					switch (columnIndex) {
						case Constants.COLUMN_NAME:
							employee.setName((String) getCellValue(cell, workbook));
							break;
						case Constants.COLUMN_CODE:
							employee.setCode((String) getCellValue(cell, workbook));
							break;
						case Constants.COLUMN_EMAIL:
							employee.setEmail((String) getCellValue(cell, workbook));
							break;
						case Constants.COLUMN_PHONE:
							employee.setPhone((String) getCellValue(cell, workbook));
							break;
						case Constants.COLUMN_AGE:
							int age;
							try {
								age = (int) ((double) getCellValue(cell, workbook));
								employee.setAge(age);
							}
							catch (Exception e) {
								errorList.add("Found error at column " + Constants.COLUMN_AGE + " row " +
										nextRow.getRowNum() + ": Cannot convert string to uuid");
								skipLoop = true;
								break outer;
							}
							break;
						case Constants.COLUMN_PROVINCE_ID:
							UUID uuid = convertStringToUUID(cell, workbook, errorList,
									Constants.COLUMN_PROVINCE_ID, nextRow.getRowNum());
							if (uuid != null) {
								Province province = provinceRepository.findProvincesById(uuid);
								employee.setProvince(province);
								skipLoop = true;
								break outer;
							}
							break;
						case Constants.COLUMN_DISTRICT_ID:
							uuid = convertStringToUUID(cell, workbook, errorList,
									Constants.COLUMN_DISTRICT_ID, nextRow.getRowNum());
							if (uuid != null) {
								District district = districtRepository.findDistrictById(uuid);
								employee.setDistrict(district);
								skipLoop = true;
								break outer;
							}
							break;
						case Constants.COLUMN_COMMUNE_ID:
							uuid = convertStringToUUID(cell, workbook, errorList,
									Constants.COLUMN_COMMUNE_ID, nextRow.getRowNum());
							if (uuid != null) {
								Commune commune = communeRepository.findCommuneById(uuid);
								employee.setCommune(commune);
								skipLoop = true;
							}
							break;
						default:
							break;
					}
				}
				if (skipLoop) continue;
				EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
				ProvinceDto provinceDto = modelMapper.map(employee.getProvince(), ProvinceDto.class);
				employeeDto.setProvince(provinceDto);
				DistrictDto districtDto = modelMapper.map(employee.getDistrict(), DistrictDto.class);
				employeeDto.setDistrict(districtDto);
				CommuneDto communeDto = modelMapper.map(employee.getCommune(), CommuneDto.class);
				employeeDto.setCommune(communeDto);
				ResponseMessage responseMessage = employeeValidation.validate(employeeDto, null);
				if (responseMessage.equals(EmployeeResponseMessage.SUCCESSFUL)) {
					employeeList.add(employee);
				} else {
					String error = gerErrorFromResponse(responseMessage, nextRow.getRowNum());
					if (error != null) {
						errorList.add(error);
					}
				}
			}
			employeeRepository.saveAll(employeeList);
			workbook.close();
			inputStream.close();
		} catch (IOException e) {
			return EmployeeResponseMessage.CAN_NOT_IMPORT_THE_FILE;
		}
		return EmployeeResponseMessage.SUCCESSFUL;
	}

	private UUID convertStringToUUID(Cell cell, Workbook workbook,
									 List<String> errorList, int columnNumber, int rowNumber) {
		try {
			return UUID.fromString((String) getCellValue(cell, workbook));
		}
		catch (Exception e) {
			errorList.add("Found error at column " + columnNumber + " row " + rowNumber +
					": Cannot convert string to uuid");
		}
		return null;
	}

	private String gerErrorFromResponse(ResponseMessage responseMessage, int rowNumber) {
		String responseCode = responseMessage.getCode();
		if (responseCode.equals("400_E2") || responseCode.equals("400_E3")
				|| responseCode.equals("400_4") || responseCode.equals("400_5")) {
			return createErrorMessage(responseMessage, Constants.COLUMN_CODE, rowNumber);
		}
		if (responseCode.equals("400_E5") || responseCode.equals("400_E6")) {
			return createErrorMessage(responseMessage, Constants.COLUMN_NAME, rowNumber);
		}
		if (responseCode.equals("400_E7") || responseCode.equals("400_E8")
				|| responseCode.equals("400_E9")) {
			return createErrorMessage(responseMessage, Constants.COLUMN_EMAIL, rowNumber);
		}
		if (responseCode.equals("400_E10") || responseCode.equals("400_E11")) {
			return createErrorMessage(responseMessage, Constants.COLUMN_PHONE, rowNumber);
		}
		if (responseCode.equals("400_E12") || responseCode.equals("400_E13")) {
			return createErrorMessage(responseMessage, Constants.COLUMN_AGE, rowNumber);
		}
		if (responseCode.equals("400_E14")) {
			return createErrorMessage(responseMessage, Constants.COLUMN_PROVINCE_ID, rowNumber);
		}
		if (responseCode.equals("400_E15") || responseCode.equals("400_E18")) {
			return createErrorMessage(responseMessage, Constants.COLUMN_DISTRICT_ID, rowNumber);
		}
		if (responseCode.equals("400_E16") || responseCode.equals("400_E17")) {
			return createErrorMessage(responseMessage, Constants.COLUMN_COMMUNE_ID, rowNumber);
		}
		return null;
	}

	private String createErrorMessage(ResponseMessage responseMessage, int columnNumber, int rowNumber) {
		return responseMessage.getMessage()+ ": column " + columnNumber + ", row " + rowNumber;
	}

	private Object getCellValue(Cell cell, Workbook workbook) {
		CellType cellType = cell.getCellTypeEnum();
		Object cellValue = null;
		switch (cellType) {
			case BOOLEAN:
				cellValue = cell.getBooleanCellValue();
				break;
			case FORMULA:
				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				cellValue = evaluator.evaluate(cell).getNumberValue();
				break;
			case NUMERIC:
				cellValue = cell.getNumericCellValue();
				break;
			case STRING:
				cellValue = cell.getStringCellValue();
				break;
			default:
				break;
		}
		return cellValue;
	}

	//------------------------------ Export Excel -----------------------------------//

	private CellStyle cellStyleFormatNumber = null;

	public ResponseMessage writeExcel(List<Employee> employeeList, HttpServletResponse response) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("employeeList");
		int rowIndex = 0;
		writeHeader(sheet, rowIndex);
		rowIndex++;
		for (Employee employee : employeeList) {
			Row row = sheet.createRow(rowIndex);
			writeEmployee(employee, row);
			rowIndex++;
		}
		int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
		autosizeColumn(sheet, numberOfColumn);
		try (ServletOutputStream os = response.getOutputStream()) {
			workbook.write(os);
			workbook.close();
		} catch (IOException e) {
			return EmployeeResponseMessage.EMPLOYEE_LIST_CAN_NOT_EXPORT_TO_EXCEL;
		}
		return EmployeeResponseMessage.SUCCESSFUL;
	}

	private void writeHeader(Sheet sheet, int rowIndex) {
		CellStyle cellStyle = createStyleForHeader(sheet);
		Row row = sheet.createRow(rowIndex);
		for (int column : Constants.COLUMN_LIST){
			Cell cell = row.createCell(column);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(Constants.CELL_VALUE_LIST[column]);
		}
	}

	private void writeEmployee(Employee employee, Row row) {
		if (cellStyleFormatNumber == null) {
			short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
			Workbook workbook = row.getSheet().getWorkbook();
			cellStyleFormatNumber = workbook.createCellStyle();
			cellStyleFormatNumber.setDataFormat(format);
		}

		Cell cell = row.createCell(Constants.COLUMN_NAME);
		cell.setCellValue(employee.getName());

		cell = row.createCell(Constants.COLUMN_CODE);
		cell.setCellValue(employee.getCode());

		cell = row.createCell(Constants.COLUMN_EMAIL);
		cell.setCellValue(employee.getEmail());

		cell = row.createCell(Constants.COLUMN_PHONE);
		cell.setCellValue(employee.getPhone());

		cell = row.createCell(Constants.COLUMN_AGE);
		cell.setCellValue(employee.getAge());

		cell = row.createCell(Constants.COLUMN_PROVINCE_ID);
		cell.setCellValue(employee.getProvince().getId().toString());

		cell = row.createCell(Constants.COLUMN_DISTRICT_ID);
		cell.setCellValue(employee.getDistrict().getId().toString());

		cell = row.createCell(Constants.COLUMN_COMMUNE_ID);
		cell.setCellValue(employee.getCommune().getId().toString());
	}

	private CellStyle createStyleForHeader(Sheet sheet) {
		Font font = sheet.getWorkbook().createFont();
		font.setFontName("Times New Roman");
		font.setBold(true);
		font.setFontHeightInPoints((short) 14); // font size
		font.setColor(IndexedColors.BLACK.getIndex()); // text color

		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		return cellStyle;
	}

	private void autosizeColumn(Sheet sheet, int lastColumn) {
		for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
		}
	}

	//------------------------------ Other -----------------------------------//
    private static void scanStaffColumnExcelIndex(Sheet datatypeSheet, int scanRowIndex) {
        Row row = datatypeSheet.getRow(scanRowIndex);
        int numberCell = row.getPhysicalNumberOfCells();

        hashColumnPropertyConfig.put("staffCode".toLowerCase(), "staffCode");
        hashColumnPropertyConfig.put("firstName".toLowerCase(), "firstName");
        hashColumnPropertyConfig.put("lastName".toLowerCase(), "lastName");
        hashColumnPropertyConfig.put("displayName".toLowerCase(), "displayName");
        hashColumnPropertyConfig.put("birthdate".toLowerCase(), "birthdate");
        hashColumnPropertyConfig.put("birthdateMale".toLowerCase(), "birthdateMale");
        hashColumnPropertyConfig.put("birthdateFeMale".toLowerCase(), "birthdateFeMale");
        hashColumnPropertyConfig.put("gender".toLowerCase(), "gender");
        hashColumnPropertyConfig.put("address".toLowerCase(), "address");// Cái này cần xem lại
        hashColumnPropertyConfig.put("userName".toLowerCase(), "userName");
        hashColumnPropertyConfig.put("password".toLowerCase(), "password");
        hashColumnPropertyConfig.put("email".toLowerCase(), "email");
        hashColumnPropertyConfig.put("BirthPlace".toLowerCase(), "BirthPlace");

        hashColumnPropertyConfig.put("departmentCode".toLowerCase(), "departmentCode");
        hashColumnPropertyConfig.put("MaNgach".toLowerCase(), "MaNgach");
        hashColumnPropertyConfig.put("IDCard".toLowerCase(), "IDCard");

        for (int i = 0; i < numberCell; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
                String cellValue = cell.getStringCellValue();
                if (cellValue != null && cellValue.length() > 0) {
                    cellValue = cellValue.toLowerCase().trim();
                    String propertyName = hashColumnPropertyConfig.get(cellValue);
                    if (propertyName != null) {
                        hashStaffColumnConfig.put(propertyName, i);
                    }
                }
            }
        }
    }

    public static List<DepartmentDto> getListDepartmentFromInputStream(InputStream is) {
        try {

            List<DepartmentDto> ret = new ArrayList<DepartmentDto>();
            // FileInputStream excelFile = new FileInputStream(new File(filePath));
            // Workbook workbook = new XSSFWorkbook(excelFile);
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            // Iterator<Row> iterator = datatypeSheet.iterator();
            int rowIndex = 4;

            hashDepartmentColumnConfig.put("code", 0);

            hashDepartmentColumnConfig.put("name", 1);

            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell = null;
                if (currentRow != null) {
                    DepartmentDto department = new DepartmentDto();
                    Integer index = hashDepartmentColumnConfig.get("code");
                    if (index != null) {
                        currentCell = currentRow.getCell(index);// code
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            double value = currentCell.getNumericCellValue();
                            String code = numberFormatter.format(value);
                            department.setCode(code);
                        } else if (currentCell != null && currentCell.getStringCellValue() != null) {
                            String code = currentCell.getStringCellValue();
                            department.setCode(code);
                        }
                    }
                    index = hashDepartmentColumnConfig.get("name");
                    if (index != null) {
                        currentCell = currentRow.getCell(index);// name
                        if (currentCell != null && currentCell.getStringCellValue() != null) {
                            String name = currentCell.getStringCellValue();
                            department.setName(name);
                        }
                    }
                    ret.add(department);
                }
                rowIndex++;
            }
            return ret;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
