package demo;

import java.io.*;
import java.util.Properties;

public class Progress {

    private final String fileName;

    public Progress(String fileName) {
        this.fileName = fileName;
    }

    public void writeInt(String name, int value) throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        properties.setProperty(name, String.valueOf(value));
        properties.store(fos, "");
        fos.close();
    }

    public int readInt(String name, int defaultValue) throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        File file = new File(fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            properties.load(fis);
            fis.close();
            return Integer.valueOf(properties.getProperty(name));
        } catch (FileNotFoundException e) {
            return defaultValue;
        }
    }
}
