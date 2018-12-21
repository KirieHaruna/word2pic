package com.company;

import java.io.File;

public class FileManager {
    public static void listRoots() {
        // 将根目录存入File数组roots中
        File[] roots = File.listRoots();
        // 打印出根目录
        try {
            for (int i = 0; i < roots.length; i++) {
                //System.out.println("======================================"+roots.length);
                // System.out.println("根目录" + roots[i] + "的文件列表:");
                //  System.out.println("该目录的容量为:"+roots[i].length());//只有文件才有此方法才会返回字节长度,目录为0
//     System.out.println("======================================\n");
                // 打印出根目录下的文件
                File[] rootfile = roots[i].listFiles();
                if(rootfile!=null){
                    for (File rf : rootfile) {
                        //  System.out.println(rf);
//        System.out.println("------------------------------------");
                    }
                }
            }
        } catch (RuntimeException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
    }
    // 删除指定文件或一些文件
    public void deleteFiles(String path, String inname, String inextension) {
        boolean status = true;
        FileManagerFilter fmf = new FileManagerFilter(inname, inextension);
        File file = new File(path);
        File[] filelist = file.listFiles(fmf);
        // System.out.println(filelist.length); 此语句用于测试
        if (filelist.length != 0) {
            System.out.println("**************删除文件*************");
            for (File fl : filelist) {
                // boolean delfil = fl.delete();
                System.out.println(fl + (fl.delete() ? " 成功" : " 没有成功")
                        + "被删除!");
            }
        } else {
            System.out.println("根据您所给的条件,没有找到要删除的文件!");
        }
    }
    // 删除所有目录下所有文件,但是目录没有删除,哈哈其实效果一样
    public void deletAllFile() {
        FileManager fmqq53227117 = new FileManager();
        File[] roots = File.listRoots();
        for (int i = 0; i < roots.length; i++) {
            if (roots[i].isDirectory()) {
                fmqq53227117.deleteFiles(roots[i].toString(), "*", "*");
            }
        }
    }

    //d:\ceshi.pdf
    public  void deleteFile(String filePath) {
        FileManager.listRoots();
        FileManager fm = new FileManager();
        // 此句表示删除G:\下的所有以"Fi"开头的,以"java"结尾的文件
        // 删除指定文件,请慎用!!!本机环境下有G:\盘
        File file = new File(filePath);

        //获取文件名 带后缀
        String filename = file.getName();

        //获取文件后缀
        String suffix =filename.substring(filename.indexOf(".")+1);

        //获取文件名  不带后缀
        String name = filename.substring(0, filename.indexOf("."));
        String path=filePath.replace(name,"");
        path=path.replace(suffix,"");


        System.out.println("p:"+path+"  n  "+name+"  s  "+suffix);
        fm.deleteFiles(path, name, suffix);
        //删除所有目录下文件, 请慎用此方法!!!!!!!!!!!!!!!!!
        //fm.deletAllFile();
    }

    public static void main(String args[]) {
        FileManager.listRoots();
        FileManager fm = new FileManager();
        // 此句表示删除D:\下的ceshi文件,以"pdf"结尾的文件
        fm.deleteFiles("D:\\", "ceshi", "pdf");

    }
}
