package com.codepath.simpletodoapp.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

/**
 * Utility class for file read/write operation
 *
 * @author tejalpar
 */
public final class FileUtility {

    public static ArrayList<String> readItemsFromFile(File filesDir) {
        File toDoFile = new File(filesDir, "todo.txt");
        try {
            return new ArrayList<>(FileUtils.readLines(toDoFile));
        } catch (IOException ex) {
            return new ArrayList<>();
        }
    }

    public static void writeItemsToFile(File filesDir, ArrayList<String> items) {
        File toDoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(toDoFile, items);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
