package api;

import java.io.IOException;

public interface Client {
    int open(String filepath, int mode);
    void append(int fd, byte[] bytes) throws IOException;

    /**
     * @param fd
     * @return All bytes in file, or null if the read is not allowed (file opened without r).
     */
    byte[] read(int fd);
    void close(int fd);
}
