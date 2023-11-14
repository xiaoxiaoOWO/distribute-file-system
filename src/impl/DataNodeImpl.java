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
    //���ݽڵ��Ŀ¼
    private final File directory;
    //�Ѿ����ڵ�block��id
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
            // ��ȡ�ļ����ݲ�����
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            // �����쳣�������ӡ������Ϣ
            e.printStackTrace();
            // �򷵻ؿ������null����������Ӧ���߼�
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
                long position = 0; // ��ʼλ��
                raf.seek(position);

                // Ѱ�ҵ�һ��0x00�ֽ�
                while (position < file.length() && raf.readByte() != 0x00) {
                    position++;
                }

                // ���¶�λ��0x00�ֽڵ�λ��
                raf.seek(position);

                // �Ӵ�λ�ÿ�ʼд��
                raf.write(bytes);
            }
        } catch (IOException e) {
            // �����ļ������쳣
            e.printStackTrace();
            // ��������Ӧ���߼�����������Ҫ�������׳��쳣��ִ������������
        }
    }

    @Override
    public int randomBlockId() {
        //��ȡһ�������blockId�����blockId�����Ѿ�����
        int blockId;
        do {
            blockId = (int) (Math.random() * Integer.MAX_VALUE);
        } while (blockIds.contains(blockId));
        return blockId;
    }

    public int randomAvailableBlockId() {
        //��ȡһ�������blockId�����blockId�Ѿ�����
        Random random = new Random();
        if (blockIds.isEmpty()) {
            return -1;
        }
        int randomIndex = random.nextInt(blockIds.size());
        return blockIds.get(randomIndex);
    }

}