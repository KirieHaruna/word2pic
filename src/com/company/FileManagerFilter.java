package com.company;

import java.io.File;
import java.io.FilenameFilter;
public class FileManagerFilter implements FilenameFilter {
    private String name;
    private String extension;
    public FileManagerFilter(String name, String extension) {
        this.name = name;
        this.extension = extension;
    }
    public boolean accept(File dir, String filename) {
        boolean fileOK = true;
        String str;
        char c;

        if (name == "*"&&extension=="*") {
            return fileOK = true;
        }

        //遍历filename字符串
        for(int i=0;i<filename.length();i++){
            //找出filename字符串中的每个字符
            c =filename.charAt(i);
            //转换为string类型
            str = String.valueOf(c);
            if (name != null&&str.equals(".")) {
                // 不大解理"&="的运行过程,
                //找出文件夹中name相同的
                fileOK &= filename.substring(0, filename.indexOf(".")).equals(name);

                //匹配以name开头的文件名称
//            fileOK &= filename.startsWith(name);
            }
        }

        if (extension != null) {
            //匹配以extension 结尾的文件后缀
            fileOK &= filename.endsWith('.' + extension);
        }
        return fileOK;
    }



}