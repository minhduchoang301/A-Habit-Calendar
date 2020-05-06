package controllers;

import java.io.*;

/**
 * Responsible for basic reading, appending and overwriting from/to a text file.
 */
public class IO {
    /**
     * Takes a filename, reads it. Returns a BufferedReader for it.
     *
     * @param filename filename to be read.
     * @return BufferedReader of filename.
     * @throws FileNotFoundException if filename doesn't exist or isn't found.
     */
    BufferedReader readFile(String filename) throws FileNotFoundException {
        return new BufferedReader(new FileReader("phase2/Database/" + filename));
    }

    /**
     * Takes a filename, opens it for appending. Returns a BufferedWriter for it.
     *
     * @param filename filename to be appended to.
     * @return BufferedWriter of filename
     * @throws IOException If an error happens during I/O.
     */
    BufferedWriter appendToFile(String filename) throws IOException {
        return new BufferedWriter(
                new FileWriter("phase2/Database/" + filename, true)  //Set true for append mode
        );
    }

    /**
     * Takes a filename, opens it for overwriting. Returns a BufferedWriter for it.
     *
     * @param filename filename to overwrite to.
     * @return BufferedWriter of filename.
     * @throws IOException if an error happens during I/O.
     */
    BufferedWriter overwriteToFile(String filename) throws IOException {
        try{
            return new BufferedWriter(
                    new FileWriter("phase2/Database/" + filename, false)  //Set false for overwrite mode
            );
        }
        catch(Exception IOException){
            //https://www.tutorialspoint.com/how-to-create-a-new-directory-by-using-file-object-in-java
            //Creating a File object
            File file = new File("phase2/Database/" + filename.split("/")[0]);
            //Creating the directory
            file.mkdir();

            return new BufferedWriter(
                    new FileWriter("phase2/Database/" + filename, false)  //Set false for overwrite mode
            );
        }
    }
}
