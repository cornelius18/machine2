/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author PC
 */
public class visitorThread extends Thread {

    public fileVisitor visitor;
    int fCount;
    
    public visitorThread(String keyword, Path dir,int cntr) throws IOException {
    visitor = new fileVisitor(keyword, dir, cntr);
    fCount=cntr;
    Files.walkFileTree(dir, visitor);
    }
    
}
