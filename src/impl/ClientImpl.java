package impl;
//TODO: your implementation

import api.Client;
import api.DataNode;
import api.NameNode;
import api.NameNodeHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import utils.FileDesc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ClientImpl implements Client {
    final int MAX_CHUNK_SIZE = 4096;
    private NameNode nameNode;
    private final DataNode[] dataNode = new DataNode[2];
    private int fdCounter = 0;
    private Map<Integer, FileDesc> fdDescMap = new HashMap<>();

    public ClientImpl() {

        try {
            String args[] = {};
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
            props.put("org.omg.CORBA.ORBInitialPort", "1050");
            ORB orb = ORB.init(args, props);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = org.omg.CosNaming.NamingContextExtHelper.narrow(objRef);

            nameNode = NameNodeHelper.narrow(ncRef.resolve_str("NameNode"));
            System.out.println("nameNode is obtained");

            for (int i = 0; i < 2; i++) {
                int j = i + 1;
                dataNode[i] = api.DataNodeHelper.narrow(ncRef.resolve_str("DataNode" + j));
                System.out.println("dataNode" + j + " is obtained");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @Override
    public int open(String filepath, int mode) {
        String string = nameNode.open(filepath, mode);
        if (string == null) {
            return -1;
        }
        FileDesc fileDesc = FileDesc.fromString(string);
        fileDesc.setLastAccessed(java.time.LocalDateTime.now());
        fdDescMap.put(++fdCounter, fileDesc);
        return fdCounter;
    }

    @Override
    public void append(int fd, byte[] bytes) throws IOException {
        FileDesc fileDesc = fdDescMap.get(fd);
        if (fileDesc == null || fileDesc.getMode() == 0b01) {
            // Either file descriptor is not found or the mode is not suitable for appending
            return;
        }
        int totalSize = bytes.length;
        int processedSize = 0;
        int dataNodeIndex = 1;
        long size = fileDesc.getSize();
        if ((size % 4096) != 0) {
            // If the file size is not a multiple of 4096, then we need to append to the last block
            List<String> blockLocations = fileDesc.getBlockLocations();
            String blockLocation = blockLocations.get(blockLocations.size() - 1);
            String[] parts = blockLocation.split(" ");
            int dataNodeIndex1 = Integer.parseInt(parts[1]);
            int blockId = Integer.parseInt(parts[3]);
            long offset = size % 4096;
            long neededSize = Math.min(4096-offset,totalSize);
            byte[] chunk = new byte[MAX_CHUNK_SIZE];
            System.out.println("neededSize: " + neededSize);
            System.arraycopy(bytes, 0, chunk, 0, (int) neededSize);
            DataNode dataNode = this.dataNode[dataNodeIndex1 - 1];
            dataNode.append(blockId, chunk);
            processedSize += neededSize;
        }
        while (processedSize < totalSize) {
            // Calculate size of the current chunk
            int chunkSize = Math.min(MAX_CHUNK_SIZE, totalSize - processedSize);

            // Create a chunk
            byte[] chunk = new byte[MAX_CHUNK_SIZE];
            System.arraycopy(bytes, processedSize, chunk, 0, chunkSize);

            DataNode dataNode = this.dataNode[dataNodeIndex - 1];
            int blockId = dataNode.randomBlockId();
            String blockLocation = "dataNode: " + dataNodeIndex + " block: " + blockId;
            dataNode.append(blockId, chunk);
            fileDesc.addBlockLocations(blockLocation);

            // Update the processed size
            processedSize += chunkSize;
            dataNodeIndex = dataNodeIndex % 2 + 1;
        }


        fileDesc.setSize(size + totalSize);
        fileDesc.setLastModified(java.time.LocalDateTime.now());
    }

    @Override
    public byte[] read(int fd) {
        FileDesc fileDesc = fdDescMap.get(fd);
        if (fileDesc == null || fileDesc.getMode() == 0b10) {
            // Either file descriptor is not found or the mode is not suitable for appending
            return null;
        }
        int totalSize = (int) fileDesc.getSize();
        byte[] bytes = new byte[(int) totalSize];
        List<String> blockLocations = fileDesc.getBlockLocations();
        if (blockLocations.isEmpty()) {
            return null;
        }
        int processedSize = 0;
        for (String blockLocation : blockLocations) {
            String[] parts = blockLocation.split(" ");
            int dataNodeIndex = Integer.parseInt(parts[1]);
            int blockId = Integer.parseInt(parts[3]);
            DataNode dataNode = this.dataNode[dataNodeIndex - 1];
            byte[] bytesRead = dataNode.read(blockId);
            int sizeNeeded = Math.min(totalSize - processedSize, 4096);
            System.arraycopy(bytesRead, 0, bytes, processedSize, sizeNeeded);
            processedSize += sizeNeeded;
        }
        return bytes;
    }

    @Override
    public void close(int fd) {
        FileDesc fileDesc = fdDescMap.get(fd);
        if (fileDesc == null) {
            return;
        }
        nameNode.close(fileDesc.toString());
        fdDescMap.remove(fd);
    }
}
