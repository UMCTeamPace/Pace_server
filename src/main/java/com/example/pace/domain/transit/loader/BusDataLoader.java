package com.example.pace.domain.transit.loader;

import com.example.pace.domain.transit.entity.BusInfo;
import com.example.pace.domain.transit.repository.BusInfoRepository;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BusDataLoader implements CommandLineRunner {
    private final BusInfoRepository busInfoRepository;
    private static final String EXCEL_FILE_PATH = "data/bus_line_info.xlsx";
    private final DataFormatter dataFormatter = new DataFormatter();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (busInfoRepository.count() > 0) {
            log.info("이미 버스 노선 데이터가 있습니다.");
            return;
        }

        log.info("버스 노선 데이터를 불러오는 중...");
        loadBusDataFromExcel();
    }

    private void loadBusDataFromExcel() {
        ClassPathResource resource = new ClassPathResource(EXCEL_FILE_PATH);
        if (!resource.exists()) {
            log.warn("해당 엑셀 파일을 찾지 못했습니다.");
            return;
        }

        List<BusInfo> busInfoList = new ArrayList<>();
        int batchSize = 1000; // 1000개씩 끊어서 저장

        try (InputStream fis = resource.getInputStream()) { // 엑셀 파일 열기
            Workbook workbook = new XSSFWorkbook(fis); // 워크북 객체 생성
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 선택
            Iterator<Row> rowIterator = sheet.iterator(); // 행 반복자 생성

            // 헤더 스킵
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            int rowCount = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next(); // 다음 행으로

                // 필수 값이(노선ID, 순번)이 비어있는 행은 넘어가기(일단 모두 넣는걸로)
//                if (isCellEmpty(row.getCell(0)) || isCellEmpty(row.getCell(2))) {
//                    continue;
//                }

                try {
                    String routeId = getCellValue(row.getCell(0));
                    String lineName = getCellValue(row.getCell(1));
                    Integer sequence = (int) Double.parseDouble(getCellValue(row.getCell(2))); // 엑셀 숫자는 기본적으로 Double형
                    String arsId = getCellValue(row.getCell(4));
                    String stationName = getCellValue(row.getCell(5));
                    BigDecimal stationLng = new BigDecimal(getCellValue(row.getCell(6))); // x좌표(경도)
                    BigDecimal stationLat = new BigDecimal(getCellValue(row.getCell(7))); // y좌표(위도)

                    BusInfo busInfo = BusInfo.builder()
                            .busRouteId(routeId)
                            .lineName(lineName)
                            .sequence(sequence)
                            .arsId(arsId)
                            .stationName(stationName)
                            .stationLng(stationLng)
                            .stationLat(stationLat)
                            .build();

                    busInfoList.add(busInfo);
                    rowCount++;

                    if (busInfoList.size() >= batchSize) {
                        busInfoRepository.saveAll(busInfoList);
                        busInfoList.clear();
                        log.info("버스 노선 데이터 {}개를 저장하였습니다.", rowCount);
                    }
                } catch (Exception e) {
                    log.warn("{} 번째 행 파싱 에러: {}", row.getRowNum(), e.getMessage());
                }
            }

            if (!busInfoList.isEmpty()) {
                busInfoRepository.saveAll(busInfoList);
                log.info("마지막 남은 버스 노선 데이터 {}개를 저장하였습니다.", rowCount);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        // DataFormatter을 통해 엑셀에 보이는 그대로의 문자열을 반환하도록 함
        return dataFormatter.formatCellValue(cell);
    }

    private boolean isCellEmpty(Cell cell) {
        return cell == null || cell.getCellType() == CellType.BLANK;
    }
}
