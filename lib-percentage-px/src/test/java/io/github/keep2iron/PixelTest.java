package io.github.keep2iron;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * ��Ļ����������
 */
public class PixelTest {
    public static final String WRITE_ROOT_PATH = "c:\\pixel";

    public static final int BASE_WIDTH = 750;

    public static final int BASE_HEIGHT = 1334;

    public static final DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public static final String[] ALL_SIZE = new String[]{
            "400x320",
            "480x320",
            "800x480",
            "854x480",
            "960x540",
            "1024x600",
            "1024x768",
            "1184x720",
            "1196x720",
            "1280x720",
            "1280x800",
            "1776x1080",
            "1800x1080",
            "1812x1080",
            "1920x1080",
            "2560x1440",
            "2392x1440"
    };

    public static void main(String[] args) {
        new PixelTest().test();
    }


    @Test
    public void test() {
        try {
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            File file = new File(WRITE_ROOT_PATH);

            //���ж��ļ��Ƿ����
            if (!file.exists()) {
                file.mkdirs();
            }

            System.out.println("BASE_WIDTH : " + BASE_WIDTH + " BASE_HEIGHT : " + BASE_HEIGHT);
            //Ȼ��Ϳ�ʼ�����ļ�
            for (String size : ALL_SIZE) {
                String[] dimens = size.split("x");
                int height = Integer.valueOf(dimens[0]);
                int width = Integer.valueOf(dimens[1]);

                generate(width, height);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * �����ԭ�����ڣ������ҵ��������ҵ�ͼƬ�Ļ�׼�ֱ�����1280 * 720��
     * �����أ�����һ��800 * 600���ص��ֻ�
     * ��ʱ������ʵ�����ǰ� 800���س��ȷֳ��� 1280�ݡ�
     * 800���صĵ�λ1 �� 1280�ĵ�λ1��Ȼ�ھ�����������ǲ�ͬ�ģ���������Ļ�İٷֱ�����һ����
     *
     * @param width
     * @param height
     */
    void generate(int width, int height) throws IOException {
        File parentFile = new File(WRITE_ROOT_PATH + "\\values-" + height + "x" + width);
        if (!parentFile.exists())
            parentFile.mkdirs();

        File generateXFile = new File(parentFile, "dimens_x.xml");
        PrintWriter writerX = new PrintWriter(new FileWriter(generateXFile));
        float scaleX = width * 1.0f / BASE_WIDTH;
        System.out.println("scaleX : " + scaleX + " width : " + width + " height : " + height);
        write(writerX, "x", scaleX, BASE_WIDTH);
        writerX.close();

        File generateYFile = new File(parentFile, "dimens_y.xml");
        PrintWriter writerY = new PrintWriter(new FileWriter(generateYFile));
        float scaleY = height * 1.0f / BASE_HEIGHT;
        write(writerY, "y", scaleY, BASE_HEIGHT);
        writerY.close();
    }

    /**
     * @param writer
     * @param xy       �������x�ʹ���"x"
     * @param scale    ���ű���
     * @param baseSize ��׼�Ĵ�С
     */
    void write(PrintWriter writer, String xy, float scale, int baseSize) {
        writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writer.println("<resources>");
        for (int i = 1; i <= baseSize; i++) {
            writer.println("<dimen name=\"" + xy + i + "\">" + computeSize(i, scale) + "px</dimen>");
        }
        writer.write("</resources>");
    }

    float computeSize(int size, float scale) {
        String value = decimalFormat.format(size * scale);
        return Float.valueOf(value);
    }
}
