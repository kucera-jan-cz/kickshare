package com.github.kickshare.sheet;

import java.io.FileOutputStream;
import java.io.IOException;

import com.github.kickshare.test.JsonUtil;
import com.github.kickshare.test.TestUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 25.5.2017
 */
public class CalculationFactoryTest {
    @Test
    public void create() throws IOException {
        PledgeCalculationMetadata metadata = JsonUtil.fromJson(TestUtil.toString("data/sheet/pledge_metadata.json"), PledgeCalculationMetadata.class);
        CalculationFactory factory = new CalculationFactory(metadata);
        Workbook workbook = factory.get();

        try (FileOutputStream fileOut = new FileOutputStream("C:\\Temp\\workbook.xlsx")) {
            workbook.write(fileOut);
        }
    }
}
