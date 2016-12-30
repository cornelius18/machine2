/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

public class fileVisitor extends SimpleFileVisitor<Path> {

    String keyword;
    public String hits = "";
    public int pathIndex,fCount;
    File textFile;
    String[] pastHits, tempHits;
    boolean FileExist = false, isEmpty = false;
    int ctr;

    public fileVisitor(String keyword, Path dir, int cntr) throws IOException {
        this.keyword = keyword;
        ctr = cntr;
        pastHits = new String[0];
        textFile = new File(dir + "\\" + keyword + ".txt");
        if (!textFile.exists()) {
            textFile.createNewFile();
            System.out.println("creating " + textFile.getAbsolutePath());
        } else {
            FileExist = true;
            Scanner read = new Scanner(textFile);
            boolean hasNoEntry = true;
            int x = 0;
            while (read.hasNextLine()) {
                if (hasNoEntry) {
                    hits = read.nextLine() + "\n" + hits;
                    hasNoEntry = false;
                } else {
                    if (pastHits.length != 0) {
                        hits = read.nextLine() + "\n" + hits;
                    } else {
                        hits = read.nextLine() + "\n" + hits;
                    }
                    pastHits = hits.split("\n");
                    System.out.println("Past Hits");
                    System.out.println(hits);
                }

            }
            read.close();

            if (textFile.delete()) {
                System.out.println("File was deleted");
            } else {
                System.out.println("Err on deletion");
            }
            if (pastHits.length == 0) {
                isEmpty = true;
            }

        }

    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        System.out.println("Just visited: " + dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        System.out.println("About to visit: " + dir);
        return FileVisitResult.CONTINUE;

    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (ctr++ % 2 == 0) {
            String fileName = file.getFileName().toString();
            boolean contains = false;
            File thisFile = new File(file.toAbsolutePath().toString());
            // System.out.println("this file" + thisFile.lastModified() + "that
            // file" + textFile.lastModified());
            if (thisFile.lastModified() > textFile.lastModified() || !FileExist || fCount==1) {
                fCount++;
                if (fileName.length() > 4
                        && fileName.substring(fileName.length() - 4, fileName.length()).equalsIgnoreCase(".txt")) {
                    System.out.println("Reading: " + file);
                    pathIndex = lookFor(file.toAbsolutePath().toString());
                    File asd = new File(file.toAbsolutePath().toString());
                    System.out.println("Entering: " + file);
                    Scanner read = new Scanner(new File(file.toString()));
                    try {
                        String word = "";
                        while (read.hasNext() && !contains) {
                            word = read.next();
                            for (int x = 0; x + keyword.length() <= word.length(); x++) {
                                if (keyword.equalsIgnoreCase(word.substring(x, x + keyword.length()))) {
                                    contains = true;
                                    System.out.println("The file: " + file.toAbsolutePath() + " has hit");
                                    break;
                                }
                            }
                        }

                    } catch (Exception e) {

                    }
                    System.out.println("path index: " + pathIndex + "::contains " + contains);
                    if (contains) {
                        if (pathIndex == -1) {
                            System.out.println(pathIndex + " index, file: " + file.toString());
                            tempHits = pastHits;
                            pastHits = new String[tempHits.length + 1];
                            for (int x = 0; x < tempHits.length; x++) {
                                pastHits[x] = tempHits[x];
                            }
                            if (pastHits.length > 0) {
                                pastHits[pastHits.length - 1] = file.toString();
                            }
                        }

                    } else if (pathIndex != -1) {
                        delPathStr(pathIndex);
                    }
                }
            } else {
                System.out.println("Not reading " + thisFile + " has not been changed after last identical search.");
            }
        }
        return FileVisitResult.CONTINUE;

    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.err.println(exc.getMessage());
        return FileVisitResult.CONTINUE;
    }

    public int lookFor(String path) {
        int x = 0;
        boolean pathExist = false;
        if (FileExist) {
            for (; x < pastHits.length; x++) {
                if (pastHits[x].equals(path)) {
                    pathExist = true;
                    break;
                }
            }
        }
        if (pathExist) {
            return x;
        } else {
            return -1;
        }
    }

    public void delPathStr(int x) {
        String[] paths;
        paths = new String[pastHits.length - 1];
        int i = 0;
        while (i < paths.length) {
            if (i != x) {
                paths[i] = pastHits[i];
            }
        }
        pastHits = new String[paths.length];
        pastHits = paths;

    }

    public void writeNewFile() throws IOException {
        if (textFile.delete()) {
            System.out.println("text file was deleted, past hits length: " + pastHits.length);
        } else {
            System.out.println("Error on deletion");
        }
        textFile.createNewFile();
        System.out.println("Wrote: " + pastHits.length);
        try (FileWriter fw = new FileWriter(textFile, false);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter write = new PrintWriter(bw)) {
            for (int x = 0; x < pastHits.length; x++) {
                write.println(pastHits[x]);
                write.flush();
                System.out.println(x + 1 + "." + pastHits[x]);
            }
            fw.close();
        } catch (IOException e) {

        }

    }

    public void writeOnFile() throws IOException {
        System.out.println("Wrote: " + pastHits.length);
        try (FileWriter fw = new FileWriter(textFile, false);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter write = new PrintWriter(bw)) {
            for (int x = 0; x < pastHits.length; x++) {
                write.println(pastHits[x]);
                write.flush();
                System.out.println(x + 1 + "." + pastHits[x]);
            }
            fw.close();
        } catch (IOException e) {

        }

    }
}
