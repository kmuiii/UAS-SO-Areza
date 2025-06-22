package app;

import com.opencsv.*;
import org.bson.Document;

import java.io.FileReader;
import java.util.*;

public class CSVImporter {
    public static List<Document> readCSV(String path) throws Exception {
        CSVReader reader = new CSVReaderBuilder(new FileReader(path))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1)
                .build();

        List<Document> result = new ArrayList<>();
       String[] headers = {
            "Rank", "Sourceid", "Title", "Type", "Issn", "SJR", "SJR_Best_Quartile", "H_index",
            "Total_Docs_2024", "Total_Docs_3years", "Total_Refs", "Total_Citations_3years",
            "Citable_Docs_3years", "Citations_Doc_2years", "Ref_Doc", "Female_percentage", "Overton",
            "SDG", "Country", "Region", "Publisher", "Coverage", "Categories", "Areas"
        };

        String[] row;
        while ((row = reader.readNext()) != null) {
            Document doc = new Document();
            for (int i = 0; i < headers.length && i < row.length; i++) {
                doc.append(headers[i], row[i]);
            }
            result.add(doc);
        }
        return result;
    }
}
