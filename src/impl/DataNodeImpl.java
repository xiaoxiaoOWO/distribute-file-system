package impl;
//TODO: your implementation

import api.DataNodePOA;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

public class DataNodeImpl extends DataNodePOA {
    //数据节点的目录
    private final File directory;
    //已经存在的block的id
    private final List<Integer> blockIds;

    public DataNodeImpl(int id) {
        directory = new File("dataNode" + id);
        blockIds = new java.util.ArrayList<>();
        if (!directory.exists()) {
            directory.mkdir();
        }
        if (directory.listFiles() == null) {
            return;
        }
        for (File file : directory.listFiles()) {
            String filename = file.getName();
            int blockId = Integer.parseInt(filename.substring(0, filename.indexOf('.')));
            blockIds.add(blockId);
        }
    }

    @Override
    public byte[] read(int block_id) {
        File file = new File(directory, block_id + ".data");
        try {
            // 读取文件内容并返回
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            // 处理异常，例如打印错误信息
            e.printStackTrace();
            // 或返回空数组或null，根据您的应用逻辑
            return new byte[0];
        }
    }

    @Override
    public void append(int block_id, byte[] bytes) {
        File file = new File(directory, block_id + ".data");
        try {
            if (!file.exists()) {
                file.createNewFile();
                blockIds.add(block_id);
            }
            try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                long position = 0; // 起始位置
                raf.seek(position);

                // 寻找第一个0x00字节
                while (position < file.length() && raf.readByte() != 0x00) {
                    position++;
                }

                // 重新定位到0x00字节的位置
                raf.seek(position);

                // 从此位置开始写入
                raf.write(bytes);
            }
        } catch (IOException e) {
            // 处理文件操作异常
            e.printStackTrace();
            // 根据您的应用逻辑，您可能需要在这里抛出异常或执行其他错误处理
        }
    }

    @Override
    public int randomBlockId() {
        //获取一个随机的blockId，这个blockId不能已经存在
        int blockId;
        do {
            blockId = (int) (Math.random() * Integer.MAX_VALUE);
        } while (blockIds.contains(blockId));
        return blockId;
    }

    public int randomAvailableBlockId() {
        //获取一个随机的blockId，这个blockId已经存在
        Random random = new Random();
        if (blockIds.isEmpty()) {
            return -1;
        }
        int randomIndex = random.nextInt(blockIds.size());
        return blockIds.get(randomIndex);
    }

}