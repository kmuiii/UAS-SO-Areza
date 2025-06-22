package app;
import app.MongoConnector;
import app.CSVImporter;
import org.bson.Document;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("[CONNECTING...] Menghubungkan ke MongoDB...");
        MongoConnector mongo = new MongoConnector(
            "mongodb://192.168.1.51:27017", 
            "mydata_jurnal",        // Nama untuk databasenya
            "ranking"               // Nama koleksi
        );
        System.out.println("[CONNECT] Terhubung ke database 'mydata_jurnal' dan koleksi 'ranking'");

        while (true) {
            System.out.println("\n=== MongoDB Client Menu ===");
            System.out.println("1. Insert CSV");
            System.out.println("2. Update Data");
            System.out.println("3. Delete Data");
            System.out.println("4. Show data");
            System.out.println("0. Exit");
            System.out.print("Pilih: ");
            String pilihan = input.nextLine();

            switch (pilihan) {
                case "1":
                    System.out.print("Masukkan file CSV: ");
                    String path = input.nextLine();
                    try {
                        List<Document> docs = CSVImporter.readCSV(path);
                        int count = 0;
                        for (Document doc : docs) {
                            mongo.insert(doc);
                            count++;
                            if (count % 2000 == 0) {
                                System.out.println("[DONE] Berhasil memasukkan" + count + " dokumen ke database...");
                            }
                        }
                        if (count % 2000 != 0 || count == 0) {
                            System.out.println("[DONE] Berhasil memasukkan total " + count + " dokumen ke database [KELAR].");
                        }
                    } catch (Exception e) {
                        System.err.println("[ERROR] Gagal membaca atau insert data: " + e.getMessage());
                    }
                    break;

                case "2":
                    System.out.print("Masukkan Title jurnal untuk di-update: ");
                    String titleToUpdate = input.nextLine();
                    Document newData = new Document();

                    // Semua kolom diisi manual
                  System.out.print("Rank: ");
                    newData.append("Rank", input.nextLine());

                    System.out.print("Sourceid: ");
                    newData.append("Sourceid", input.nextLine());

                    System.out.print("Title: ");
                    newData.append("Title", input.nextLine());

                    System.out.print("Type: ");
                    newData.append("Type", input.nextLine());

                    System.out.print("Issn: ");
                    newData.append("Issn", input.nextLine());

                    System.out.print("SJR: ");
                    newData.append("SJR", input.nextLine());

                    System.out.print("SJR Best Quartile: ");
                    newData.append("SJR_Best_Quartile", input.nextLine());

                    System.out.print("H index: ");
                    newData.append("H_index", input.nextLine());

                    System.out.print("Total Docs. (2024): ");
                    newData.append("Total_Docs_2024", input.nextLine());

                    System.out.print("Total Docs. (3years): ");
                    newData.append("Total_Docs_3years", input.nextLine());

                    System.out.print("Total Refs.: ");
                    newData.append("Total_Refs", input.nextLine());

                    System.out.print("Total Citations (3years): ");
                    newData.append("Total_Citations_3years", input.nextLine());

                    System.out.print("Citable Docs. (3years): ");
                    newData.append("Citable_Docs_3years", input.nextLine());

                    System.out.print("Citations / Doc. (2years): ");
                    newData.append("Citations_Doc_2years", input.nextLine());

                    System.out.print("Ref. / Doc.: ");
                    newData.append("Ref_Doc", input.nextLine());

                    System.out.print("%Female: ");
                    newData.append("Female_percentage", input.nextLine());

                    System.out.print("Overton: ");
                    newData.append("Overton", input.nextLine());

                    System.out.print("SDG: ");
                    newData.append("SDG", input.nextLine());

                    System.out.print("Country: ");
                    newData.append("Country", input.nextLine());

                    System.out.print("Region: ");
                    newData.append("Region", input.nextLine());

                    System.out.print("Publisher: ");
                    newData.append("Publisher", input.nextLine());

                    System.out.print("Coverage: ");
                    newData.append("Coverage", input.nextLine());

                    System.out.print("Categories: ");
                    newData.append("Categories", input.nextLine());

                    System.out.print("Areas: ");
                    newData.append("Areas", input.nextLine());

                    mongo.update(titleToUpdate, newData);
                    System.out.println("[DONE] Data berhasil di-update.");
                    break;

                case "3":
                    System.out.print("Masukkan Title jurnal untuk dihapus: ");
                    String titleToDelete = input.nextLine();
                    mongo.delete(titleToDelete);
                    System.out.println("[DONE] Data berhasil dihapus.");
                    break;

                case "4":
                    try {
                        System.out.print("Masukkan batas Rank minimum: ");
                        int minRank = Integer.parseInt(input.nextLine());

                        System.out.print("Masukkan batas Rank maksimum: ");
                        int maxRank = Integer.parseInt(input.nextLine());

                        System.out.println("[INFO] Menampilkan dokumen dengan Rank antara " + minRank + " dan " + maxRank + ":");

                        int shown = 0;
                        for (Document doc : mongo.getCollection().find(
                                new Document("Rank", new Document("$gte", String.valueOf(minRank))
                                                        .append("$lte", String.valueOf(maxRank)))
                            )) {
                            System.out.println(doc.toJson());
                            shown++;
                        }

                        System.out.println("[INFO] Total dokumen ditampilkan: " + shown);
                    } catch (Exception e) {
                        System.err.println("[INFO] Input tidak valid atau terjadi kesalahan: " + e.getMessage());
                    }
                    break;


                case "0":
                    System.out.println("[KELUAR] Keluar dari aplikasi...");
                    mongo.close();
                    return;

                default:
                    System.out.println("[WARNING] Pilihan tidak dikenal. Silakan coba lagi.");
            }
        }
    }
}
