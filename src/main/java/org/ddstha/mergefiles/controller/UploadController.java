package org.ddstha.mergefiles.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ddstha.mergefiles.model.Entry;
import org.ddstha.mergefiles.service.FileEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller
public class UploadController {

  @Resource
  private HttpServletResponse response;

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
    while (iterator.hasNext()) {
      Row currentRow = iterator.next();
      Iterator<Cell> cellIterator = currentRow.iterator();
      Entry entry = null;
      int i = 0;
      entry = new Entry();
      entry.setFileId(file);
      while (cellIterator.hasNext()) {
        Cell currentCell = cellIterator.next();
        switch (i){
          case 0:
            entry.setColumn1(currentCell.getStringCellValue());
            break;
          case 1:
            entry.setColumn2(currentCell.getStringCellValue());
            break;
          case 2:
            entry.setColumn3(currentCell.getStringCellValue());
            break;
          case 3:
            entry.setColumn4(currentCell.getStringCellValue());
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
      for(int i=0; i<=3;i++){
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
  public @ResponseBody void getFile(
          @RequestParam("filename") String fileName) {
    try {
      // get your file as InputStream

      String downloadPath = "/Users/sunnygupta/projects/tmpFiles/processed/";
      InputStream is = new FileInputStream(new File(downloadPath+fileName));
      // copy it to response's OutputStream
      FileCopyUtils.copy(is, response.getOutputStream());
      response.flushBuffer();
    } catch (IOException ex) {
      throw new RuntimeException("IOError writing file to output stream");
    }

  }

}