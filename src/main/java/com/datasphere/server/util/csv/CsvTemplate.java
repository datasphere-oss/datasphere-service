/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.server.util.csv;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CsvTemplate {

  private File targetFile;

  Integer csvMaxCharsPerColumn;

  private static int MAX_CSV_COLUMNS = 2048;

  public CsvTemplate(File targetFile) {
    this.targetFile = targetFile;
  }

  public Integer getCsvMaxCharsPerColumn() {
    return csvMaxCharsPerColumn;
  }

  public void setCsvMaxCharsPerColumn(Integer csvMaxCharsPerColumn) {
    this.csvMaxCharsPerColumn = csvMaxCharsPerColumn;
  }

  public <T> List<T> getRows(String lineSep, String delimiter, CsvRowMapper<T> rowMapper) {
    CsvParserSettings settings = new CsvParserSettings();
    settings.getFormat().setLineSeparator(lineSep);
    settings.getFormat().setDelimiter(delimiter.charAt(0));
    if(csvMaxCharsPerColumn != null && csvMaxCharsPerColumn > 0){
      settings.setMaxCharsPerColumn(csvMaxCharsPerColumn);
    }
    settings.setMaxColumns(MAX_CSV_COLUMNS);

    RowListProcessor rowProcessor = new RowListProcessor();
    settings.setProcessor(rowProcessor);

    CsvParser parser = new CsvParser(settings);
    parser.beginParsing(targetFile);

    List<T> rows = new ArrayList<>();
    String[] row;

    int rowNumber = 1;
    while ((row = parser.parseNext()) != null) {
      T mappedRow = rowMapper.mapRow(rowNumber, row);

      if(mappedRow != null) {
        rows.add(rowMapper.mapRow(rowNumber, row));
      }
      rowNumber++;
    }
    parser.stopParsing();

    return rows;
  }

}
