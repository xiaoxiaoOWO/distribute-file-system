package test;

import api.DataNode;
import impl.DataNodeImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataNodeTest {
    static DataNodeImpl dn;

    @Before
    public void setUp() {
        dn = new DataNodeImpl(999);
    }

    @Test
    public void testAppend() throws IOException {
        int blockId = dn.randomBlockId();
        byte[] toWrite = "Hello World".getBytes(StandardCharsets.UTF_8);

        dn.append(blockId, toWrite);
        byte[] read = dn.read(blockId);

        int n = toWrite.length;
        int N = read.length;
        for (int i = 0; i < n; i++) {
            assertEquals("Block ID: " + blockId + ". Read block bytes and appended bytes differ at the " + i
                    + " byte to the eof.", toWrite[n - 1 - i], read[N - 1 - i]);
        }
    }

    @Test
    public void testRead() {
        int blockId = dn.randomAvailableBlockId();
        if(blockId == -1){
            return;
        }
        assertNotNull(dn.read(blockId));
    }
}
