package com.adcubum.timerecording.importexport.in.file;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.importexport.out.file.FileExportResult;
import com.adcubum.timerecording.importexport.out.file.FileExporterImpl;

class FileImporterImplTest {

   @Test
   void testImportFile_ImportFailed() {
      // Given
      String path = "ZZZ:\\ölkj";

      // When
      FileExportResult fileExportResult = FileExporterImpl.INTANCE.exportWithResult(Collections.emptyList(), path);

      // Then
      assertThat(fileExportResult.isSuccess(), is(false));
   }

   @Test
   void testImportFile_ImportSuccess() {
      // Given
      File testImportFile = new File("src\\test\\resources\\testImportFile.csv");
      FileImporterImpl fileImporter = new FileImporterImpl();

      // When
      List<String> importedContent = fileImporter.importFile(testImportFile);

      // Then
      assertThat(importedContent.size(), is(7));
   }
}
