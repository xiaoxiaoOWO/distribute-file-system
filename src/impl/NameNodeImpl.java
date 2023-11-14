package impl;
//TODO: your implementation

import api.NameNodePOA;
import utils.FileDesc;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class NameNodeImpl extends NameNodePOA {
    File FsImageData;
    File FsImage;
    private final Map<String, FileDesc> files;
    private final Map<Long, FileDesc> openFiles;
    private long idCounter = 0;

    public NameNodeImpl() {
        openFiles = new java.util.HashMap<>();
        FsImageData = new File("FsImage.data");
        FsImage = new File("FsImage.txt");
        if (!FsImageData.exists()) {
            files = new java.util.HashMap<>();
        } else {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FsImageData))) {
                // Assuming the file contains a serialized Map<String, FileDesc>
                files = (Map<String, FileDesc>) ois.readObject();
                //System.out.println(files);
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize from FsImage.data", e);
            }
        }
    }


    @Override
    public String open(String filepath, int mode) {
        if (mode == 0b10 || mode == 0b11) {
            for (FileDesc fileDesc : openFiles.values()) {
                if (fileDesc.getFilePath().equals(filepath) && (fileDesc.getMode() == 0b10 || fileDesc.getMode() == 0b11)) {
                    return null;
                }
            }
        }
        if (files.get(filepath) != null) {
            FileDesc fileDesc = files.get(filepath);
            FileDesc newFileDesc = new FileDesc(idCounter++, mode, fileDesc);
            openFiles.put((long) idCounter, newFileDesc);
            return newFileDesc.toString();
        } else {
            long id = idCounter++;
            int size = 0;
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1);
            List<String> blockLocations = new java.util.ArrayList<>();
            LocalDateTime created = LocalDateTime.now();
            LocalDateTime lastModified = LocalDateTime.now();
            LocalDateTime lastAccessed = LocalDateTime.now();
            FileDesc fileDesc = new FileDesc(id, size, mode, filepath, filename, created, blockLocations, lastModified, lastAccessed);
            openFiles.put(id, fileDesc);
            return fileDesc.toString();
        }
    }

    @Override
    public void close(String fileInfo) {
        FileDesc fileDesc = FileDesc.fromString(fileInfo);
        String filepath = fileDesc.getFilePath();
        files.put(filepath, fileDesc);
        openFiles.remove(fileDesc.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FsImageData))) {
            oos.writeObject(files);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write to FsImage.data", e);
        }
        try {
            FileWriter fw = new FileWriter(FsImage, true);
            fw.write(fileDesc.toStringForStore() + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
