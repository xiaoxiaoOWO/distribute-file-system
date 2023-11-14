package test;

import api.Client;
import impl.ClientImpl;
import org.junit.Before;
import org.junit.Test;
import utils.FileSystem;

import java.io.IOException;

import static org.junit.Assert.*;

public class ClientTest {
    static Client client;

    @Before
    public void setUp() {
        client = new ClientImpl();
    }

    @Test
    public void testWriteRead() throws IOException {
        String filename = FileSystem.newFilename();
        int fd = client.open(filename, 0b11);
        client.append(fd, "hello".getBytes("GBK"));
        assertEquals(new String(client.read(fd)), "hello");
        client.append(fd, " world".getBytes("GBK"));
        assertEquals(new String(client.read(fd)), "hello world");
        client.close(fd);
    }

    @Test
    public void testWriteFail() throws IOException {
        String filename = FileSystem.newFilename();
        int fd = client.open(filename, 0b01);
        client.append(fd, "Lala-land".getBytes("GBK"));
        assertArrayEquals(client.read(fd), "".getBytes("GBK"));
        client.close(fd);
    }

    @Test
    public void testReadFail() {
        String filename = FileSystem.newFilename();
        int fd = client.open(filename, 0b10);
        assertNull(client.read(fd));
        client.close(fd);
    }
}
