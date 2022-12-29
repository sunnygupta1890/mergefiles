package org.ddstha.mergefiles.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ddstha.mergefiles.model.Entry;
import org.ddstha.mergefiles.service.FileEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UploadController {

  @Autowired
  private FileEntryService fileEntryService;

  @GetMapping("uploadfiles")
  public String uploadFiles(Map<String, Object> model) {
    return "uploadfiles";
  }

  @RequestMapping(value = "/savefile", method = RequestMethod.POST)
  public ModelAndView upload(@RequestParam MultipartFile file1, @RequestParam MultipartFile file2) throws Exception{
    String path = "/Users/sunnygupta/projects/tmpFiles/";
    String downloadPath = "/Users/sunnygupta/projects/tmpFiles/processed/";

    String filename1 = uploadFile(file1, path);
    String filename2 = uploadFile(file2, path);

    org.ddstha.mergefiles.model.File fileObj1 = new org.ddstha.mergefiles.model.File();
    fileObj1.setFileName(path + "/" + filename1);

    org.ddstha.mergefiles.model.File fileObj2 = new org.ddstha.mergefiles.model.File();
    fileObj2.setFileName(path + "/" + filename2);

    List<Entry> file1Entries = readExcelFile(fileObj1);
    List<Entry> file2Entries = readExcelFile(fileObj2);

    String fileP1 = fileEntryService.persistFile(fileObj1);
    String fileP2 = fileEntryService.persistFile(fileObj2);

    for (Entry entry1 : file1Entries){
      fileEntryService.persistEntry(entry1, fileP1);
    }

    for (Entry entry1 : file2Entries){
      fileEntryService.persistEntry(entry1, fileP2);
    }
    List<String> fileIds = Arrays.asList(fileP1, fileP2);
    writeToExcelFile(fileEntryService.fetchAllEntries(fileIds),downloadPath + fileP1 + "_"+fileP2 + ".xlsx");
    return new ModelAndView(
        "upload-success", "filename", fileP1 + "_"+fileP2 + ".xlsx");
  }

  private String uploadFile(MultipartFile file1, String path) {
    String filename1 = file1.getOriginalFilename();

    System.out.println(path + " " + filename1);
    try {
      byte barr[] = file1.getBytes();

      BufferedOutputStream bout =
              new BufferedOutputStream(new FileOutputStream(path + "/" + filename1));
      bout.write(barr);
      bout.flush();
      bout.close();

    } catch (Exception e) {
      System.out.println(e);
    }
    return filename1;
  }

  public List<Entry> readExcelFile(org.ddstha.mergefiles.model.File file) throws Exception {
    FileInputStream excelFile = new FileInputStream(new File(file.getFileName()));

    List<Entry> list = new ArrayList<>();
    Workbook workbook = new XSSFWorkbook(excelFile);
    Sheet datatypeSheet = workbook.getSheetAt(0);
    Iterator<Row> iterator = datatypeSheet.iterator();
    int k = 0;
    while (iterator.hasNext()) {
      Row currentRow = iterator.next();
      Iterator<Cell> cellIterator = currentRow.iterator();
      if(k == 0) {
    	  k++;
    	  continue;
      }
      Entry entry = null;
      int i = 0;
      entry = new Entry();
      entry.setFileId(file);
      while (cellIterator.hasNext()) {
        Cell currentCell = cellIterator.next();
        switch (i){
          case 0:
        	  if(currentCell.getCellTypeEnum() == CellType.STRING) {
        		  entry.setColumn1(currentCell.getStringCellValue());
        	  }else {
        		  entry.setColumn1(String.valueOf(currentCell.getNumericCellValue()));
        	  }
            break;
          case 1:
        	  if(currentCell.getCellTypeEnum() == CellType.STRING) {
        		  entry.setColumn2(currentCell.getStringCellValue());
        	  }else {
        		  entry.setColumn2(String.valueOf(currentCell.getNumericCellValue()));
        	  }
            break;
          case 2:
        	  if(currentCell.getCellTypeEnum() == CellType.STRING) {
        		  entry.setColumn3(currentCell.getStringCellValue());
        	  }else {
        		  entry.setColumn3(String.valueOf(currentCell.getNumericCellValue()));
        	  }
            break;
          case 3:
        	  if(currentCell.getCellTypeEnum() == CellType.STRING) {
        		  entry.setColumn4(currentCell.getStringCellValue());
        	  }else {
        		  entry.setColumn4(String.valueOf(currentCell.getNumericCellValue()));
        	  }
            break;
          case 4:
        	  if(currentCell.getCellTypeEnum() == CellType.STRING) {
        		  entry.setColumn5(currentCell.getStringCellValue());
        	  }else {
        		  entry.setColumn5(String.valueOf(currentCell.getNumericCellValue()));
        	  }
            break;
          case 5:
        	  if(currentCell.getCellTypeEnum() == CellType.STRING) {
        		  entry.setColumn6(currentCell.getStringCellValue());
        	  }else {
        		  entry.setColumn6(String.valueOf(currentCell.getNumericCellValue()));
        	  }
            break;
          case 6:
        	  if(currentCell.getCellTypeEnum() == CellType.STRING) {
        		  entry.setColumn7(currentCell.getStringCellValue());
        	  }else {
        		  entry.setColumn7(String.valueOf(currentCell.getNumericCellValue()));
        	  }
            break;
          case 7:
        	  if(currentCell.getCellTypeEnum() == CellType.STRING) {
        		  entry.setColumn8(currentCell.getStringCellValue());
        	  }else {
        		  entry.setColumn8(String.valueOf(currentCell.getNumericCellValue()));
        	  }
            break;
          case 8:
        	  if(currentCell.getCellTypeEnum() == CellType.STRING) {
        		  entry.setColumn9(currentCell.getStringCellValue());
        	  }else {
        		  entry.setColumn9(String.valueOf(currentCell.getNumericCellValue()));
        	  }
            break;
          case 9:
        	  if(currentCell.getCellTypeEnum() == CellType.STRING) {
        		  entry.setColumn10(currentCell.getStringCellValue());
        	  }else {
        		  entry.setColumn10(String.valueOf(currentCell.getNumericCellValue()));
        	  }
            break;
           
        }
        i++;
      }
      list.add(entry);
    }
    workbook.close();
    return list;
  }

  private void writeToExcelFile(List<Entry> entries, String fileName){
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Merged File");
    int rowNum = 0;
    System.out.println("Creating excel");
    for(Entry entry : entries){
      Row row = sheet.createRow(rowNum++);
      int colNum = 0;
      for(int i=0; i<=9;i++){
        Cell cell = row.createCell(colNum++);
        switch (i){
          case 0:
            cell.setCellValue(entry.getColumn1());
            break;
          case 1:
            cell.setCellValue(entry.getColumn2());
            break;
          case 2:
            cell.setCellValue(entry.getColumn3());
            break;
          case 3:
            cell.setCellValue(entry.getColumn4());
            break;
          case 4:
            cell.setCellValue(entry.getColumn5());
            break;
          case 5:
            cell.setCellValue(entry.getColumn6());
            break;
          case 6:
            cell.setCellValue(entry.getColumn7());
            break;
          case 7:
            cell.setCellValue(entry.getColumn8());
            break;
          case 8:
            cell.setCellValue(entry.getColumn9());
            break;
          case 9:
            cell.setCellValue(Double.parseDouble(entry.getColumn10()));
            break;
        }

      }
    }
    try {
      FileOutputStream outputStream = new FileOutputStream(fileName);
      workbook.write(outputStream);
      workbook.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @RequestMapping(value = "/download", method = RequestMethod.GET)
  public ResponseEntity<org.springframework.core.io.Resource> getFile(@RequestParam("filename") String fileName) {
    try {
      String downloadPath = "/Users/sunnygupta/projects/tmpFiles/processed/";
      java.nio.file.Path filePath = Paths.get(downloadPath).toAbsolutePath().normalize().resolve(fileName).normalize();
      org.springframework.core.io.Resource resource = new UrlResource(filePath.toUri());
      String contentType = null;
      if (contentType == null) {
        contentType = "application/octet-stream";
      }
      return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(contentType))
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
              .body(resource);
    } catch (IOException ex) {
      throw new RuntimeException("IOError writing file to output stream");
    }

  }

}
