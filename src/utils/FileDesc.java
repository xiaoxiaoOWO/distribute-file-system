package utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//TODO: According to your design, complete the FileDesc class, which wraps the information returned by NameNode open()
public class FileDesc implements java.io.Serializable {
    /* the id should be assigned uniquely during the lifetime of NameNode,
     * so that NameNode can know which client's open has over at close
     * e.g., on nameNode1
     * client1 opened file "Hello.txt" with mode 'w' , and retrieved a FileDesc with 0x889
     * client2 tries opening the same file "Hello.txt" with mode 'w' , and since the 0x889 is not closed yet, the return
     * value of open() is null.
     * after a while client1 call close() with the FileDesc of id 0x889.
     * client2 tries again and get a new FileDesc with a new id 0x88a
     */
    transient final long id;
    transient int mode;
    String filePath;
    String filename;
    long size;
    List<String> blockLocations;
    LocalDateTime created;
    LocalDateTime lastModified;
    LocalDateTime lastAccessed;

    public FileDesc(long id, int mode, FileDesc fileDesc) {
        this.id = id;
        this.mode = mode;
        this.filename = fileDesc.filename;
        this.size = fileDesc.size;
        this.blockLocations = fileDesc.blockLocations;
        this.created = fileDesc.created;
        this.lastModified = fileDesc.lastModified;
        this.lastAccessed = fileDesc.lastAccessed;
    }


    public FileDesc(long id, long size, int mode, String filePath, String filename, LocalDateTime created, List<String> blockLocations, LocalDateTime lastModified, LocalDateTime lastAccessed) {
        this.id = id;
        this.size = size;
        this.mode = mode;
        this.filePath = filePath;
        this.filename = filename;
        this.blockLocations = blockLocations;
        this.created = created;
        this.lastModified = lastModified;
        this.lastAccessed = lastAccessed;
    }

    public int getMode() {
        return mode;
    }

    public long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getSize() {
        return size;
    }

    public List<String> getBlockLocations() {
        return blockLocations;
    }

    public void addBlockLocations(String blockLocation) {
        blockLocations.add(blockLocation);
    }


    public void setSize(long size) {
        this.size = size;
    }


    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public void setLastAccessed(LocalDateTime lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    /* The following method is for conversion, so we can have interface that return string, which is easy to write in idl */
    @Override
    public String toString() {
        StringBuilder blockLocationsStr = new StringBuilder();
        for (String blockLocation : blockLocations) {
            blockLocationsStr.append(blockLocation).append("\n");
        }

        return "filepath: " + filePath
                + "\nfilename: " + filename
                + "\nsize: " + size
                + "\nmode: " + mode
                + "\nid: " + id
                + "\ncreated: " + created
                + "\nlastModified: " + lastModified
                + "\nlastAccessed: " + lastAccessed
                + "\n" + blockLocationsStr;
    }

    public String toStringForStore() {
        StringBuilder blockLocationsStr = new StringBuilder();
        for (String blockLocation : blockLocations) {
            blockLocationsStr.append(blockLocation).append("\n");
        }

        return "filepath: " + filePath
                + "\nfilename: " + filename
                + "\nsize: " + size
                + "\ncreated: " + created
                + "\nlastModified: " + lastModified
                + "\nlastAccessed: " + lastAccessed
                + "\n" + blockLocationsStr;
    }


    public static FileDesc fromString(String str) {
        if (str == null) {
            return null;
        }
        String[] lines = str.split("\n");
        long id = 0;
        long size = 0;
        int mode = 0;
        String filepath = null;
        String filename = null;
        List<String> blockLocations = new ArrayList<>();
        LocalDateTime created = null;
        LocalDateTime lastModified = null;
        LocalDateTime lastAccessed = null;

        for (String line : lines) {
            if (line.startsWith("id: ")) {
                id = Long.parseLong(line.substring(4));
            } else if (line.startsWith("size: ")) {
                size = Long.parseLong(line.substring(6));
            } else if (line.startsWith("filepath: ")) {
                filepath = line.substring(10);
            } else if (line.startsWith("mode: ")) {
                mode = Integer.parseInt(line.substring(6));
            } else if (line.startsWith("filename: ")) {
                filename = line.substring(10);
            } else if (line.startsWith("created: ")) {
                created = LocalDateTime.parse(line.substring(9));
            } else if (line.startsWith("lastModified: ")) {
                lastModified = LocalDateTime.parse(line.substring(14));
            } else if (line.startsWith("lastAccessed: ")) {
                lastAccessed = LocalDateTime.parse(line.substring(14));
            } else if (!line.trim().isEmpty()) {
                blockLocations.add(line.trim());
            }
        }

        return new FileDesc(id, size, mode, filepath, filename, created, blockLocations, lastModified, lastAccessed);
    }

}
