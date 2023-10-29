package ru.vados.itcustomtestwork;

import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileListReader {

    public List<String> read(String fileName) {
        try {
            return Files.readAllLines(Paths.get(ResourceUtils.getFile("classpath:"+ fileName).toURI()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
