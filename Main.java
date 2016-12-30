/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    static String folderPath, keyword;
    static visitorThread vt1;
    static visitorThread vt2;

    public static void main(String[] args) throws IOException, InterruptedException {

        Long st, end;
        Scanner read = new Scanner(System.in);
        String again;
        do {
            do {
                System.out.print("Enter folder path (Ex: C:\\Users\\folder: ");
                folderPath = read.next();
            } while (Files.notExists(Paths.get(folderPath)));
            Path fileDir = Paths.get(folderPath);
            System.out.print("Enter keyword to be searched: ");
            keyword = read.next();
            st = System.currentTimeMillis();
            vt1 = new visitorThread(keyword, fileDir, 0);
            vt1.start();
            vt2 = new visitorThread(keyword, fileDir, 1);
            vt2.start();
            vt1.join();
            vt2.join();
            //Files.walkFileTree(fileDir, visitor);
            writeNewFile();
            //visitor.writeNewFile();
            end = System.currentTimeMillis();
            System.out.println("\nSearch time: " + ((end - st)) + " millis");
            System.out.print("Search again? Y/N: ");
            again = read.next();
        } while (again.equalsIgnoreCase("Y"));
    }

    public static void writeNewFile() throws IOException {
        String vt1Hits = "";
        File textFile = new File(folderPath + "\\" + keyword + ".txt");
        if (textFile.delete()) {
            System.out.println("text file was deleted");
        } else {
            System.out.println("Error on deletion");
        }
        System.out.println("\n\n_________________________________\nFiles has hits:");
        textFile.createNewFile();
        try (FileWriter fw = new FileWriter(textFile, false);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter write = new PrintWriter(bw)) {
            for (int x = 0; x < vt1.visitor.pastHits.length; x++) {
                write.println(vt1.visitor.pastHits[x]);
                vt1Hits+=vt1.visitor.pastHits[x];
                write.flush();
                System.out.println(x + 1 + "." + vt1.visitor.pastHits[x]);
            }
            for (int x = 0; x < vt2.visitor.pastHits.length; x++) {
                if (vt1Hits.contains(vt2.visitor.pastHits[x])) {
                    write.println(vt2.visitor.pastHits[x]);
                }
                write.flush();
                System.out.println(x + 1 + "." + vt1.visitor.pastHits[x]);
            }
            fw.close();
        } catch (IOException e) {

        }

    }
}
