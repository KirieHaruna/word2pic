package com.company;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class WordToJPG {

    //将World格式的文件转换为PDF格式的文件
    public String wordToPDF(String docfile, String toFile, ActiveXComponent app){

        String filePath = null; //PDF文件路径


        String wordFile =docfile;
        String pdfFile = toFile;
        System.out.println("开始转换...");
        // 开始时间
        long start = System.currentTimeMillis();
        try {
            // 打开word
//            app = new ActiveXComponent("Word.Application");
            // 设置word不可见,很多博客下面这里都写了这一句话，其实是没有必要的，因为默认就是不可见的，如果设置可见就是会打开一个word文档，对于转化为pdf明显是没有必要的
            //app.setProperty("Visible", false);
            // 获得word中所有打开的文档
            Dispatch documents = app.getProperty("Documents").toDispatch();
            System.out.println("打开文件: " + wordFile);
            // 打开文档
            Dispatch document = Dispatch.call(documents, "Open", wordFile, false, true).toDispatch();
            // 如果文件存在的话，不会覆盖，会直接报错，所以我们需要判断文件是否存在
            File target = new File(pdfFile);
            if (target.exists()) {
                target.delete();
            }
            System.out.println("另存为: " + pdfFile);
            // 另存为，将文档报错为pdf，其中word保存为pdf的格式宏的值是17
            Dispatch.call(document, "SaveAs", pdfFile, 17);
            // 关闭文档
            Dispatch.call(document, "Close", false);
            // 结束时间
            long end = System.currentTimeMillis();
            System.out.println("转换成功，用时：" + (end - start) + "ms");
        }catch(Exception e) {
            System.out.println("转换失败"+e.getMessage());
        }finally {
            // 关闭office
            //app.invoke("Quit", 0);
        }

        return pdfFile+".pdf";
    }

    //将PDF格式的文件转换为JPG格式的文件
    public void pdfToJPG(String inputFile, String outFile,String name) throws IOException {

        // load a pdf from a byte buffer
        File file = new File(inputFile);

        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();

        //          ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel
        //            .size());
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel
                .size());
        PDFFile pdffile = new PDFFile(buf);

        //输出文件的页数
        System.out.println("页数： " + pdffile.getNumPages());

//        for (int i = 1; i <= pdffile.getNumPages(); i++) {
            int i=1;
            // draw the first page to an image
            //以图片的形式来描绘首页
            PDFPage page = pdffile.getPage(i);
            // get the width and height for the doc at the default zoom
            Rectangle rect = new Rectangle(0, 0, (int) page.getBBox()
                    .getWidth(), (int) page.getBBox().getHeight());
            // generate the image
            //生成图片
            Image img = page.getImage(rect.width, rect.height, // width &
                    // height
                    rect, // clip rect
                    null, // null for the ImageObserver
                    true, // fill background with white
                    true // block until drawing is done
            );
            BufferedImage tag = new BufferedImage(rect.width, rect.height,
                    BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(img, 0, 0, rect.width, rect.height,
                    null);
            FileOutputStream out = new FileOutputStream(outFile +name+ ".jpg"); // 输出到文件流

            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag); // JPEG编码
            //关闭输出流
            out.close();

            System.out.println("PDF文件转换JPG文件成功");


        channel.close();

        raf.close();

        //如果要在转图片之后删除pdf，就必须要这个关闭流和清空缓冲的方法
        unmap(buf);

        // show the image in a frame
        // JFrame frame = new JFrame("PDF Test");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.add(new JLabel(new ImageIcon(img)));
        // frame.pack();
        // frame.setVisible(true);
    }

    /**

     * 清空缓冲

     * @param buffer

     */

    public static void unmap(final Object buffer) {

        AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {

                try {

                    Method getCleanerMethod = buffer.getClass().getMethod(
                            "cleaner", new Class[0]);

                    getCleanerMethod.setAccessible(true);

                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod
                            .invoke(buffer, new Object[0]);

                    cleaner.clean();

                } catch (Exception e) {

                    e.printStackTrace();

                }

                System.out.println("清空缓冲成功");

                return null;

            }

        });

    }
    public static ArrayList<String> getFiles(String path) {
        ActiveXComponent app = new ActiveXComponent("Word.Application");
        ArrayList<String> files = new ArrayList<String>();
        File file1 = new File(path);
        File[] tempList = file1.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                if (tempList[i].toString().indexOf(".docx") != -1) {
                    String docfile = tempList[i].toString();

                    //获取文件全名  带后缀
                    String filename = null;

                    //文件名  不带后缀
                    String name = null;

                    File file = new File(docfile);

                    //获取文件名 带后缀
                    filename = file.getName();

                    //获取文件名  不带后缀
                    name = filename.substring(0, filename.indexOf("."));
                    //        System.out.println(name+"name");

                    //用于存放图片的目录
                    String outFile = path+"\\Pic\\";

                    //如果目录不存在，就创建新的目录
                    if (!new File(outFile).isDirectory()) {
                        new File(outFile).mkdirs();
                        System.out.println("新建上传临时文件夹");
                    }

                    //存放PDF的路径和PDF的文件名
                    String toFile = path +"\\"+ name;

                    //实例化对象WorldToJPG
                    WordToJPG wj = new WordToJPG();

                    //将world文件转换为PDF文件   并返回PDF文件的全路径   17 表示文件格式为PDF
                    String filePath = wj.wordToPDF(docfile, toFile, app);

                    System.out.println(filePath + "\n===========================");

                    try {
                        //将PDF文件转换为JPG文件
                        wj.pdfToJPG(filePath, outFile,name);

                        //删除pdf文件
                        //System.out.println(filePath+"??");
                        new FileManager().deleteFile(filePath);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    files.add(tempList[i].toString());
                }
            }
        }
        return files;
    }

    public static void main(String[] args) {
        String MainDir = System.getProperty("user.dir");
        //源文件全路径
        getFiles(MainDir);

    }

}