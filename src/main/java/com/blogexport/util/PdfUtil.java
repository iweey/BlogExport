package com.blogexport.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;


public class PdfUtil {

    /**
     * 合并目录下的所有pdf文件
     * @param pdfDic  pdf目录
     * @param mergeFilePath  合并的pdf文件保存路径
     * @return
     * @throws Exception
     */
    public static boolean  mergePdf(String pdfDic,String mergeFilePath) throws Exception {
        //判断是否是目录
        File file = new File(pdfDic);
        if(file.isFile()){
            throw new Exception("参数目录错误");
        }
        String[] strArray = file.list();

        //按照文件名排序
        long[] arr = new long[strArray.length];
        for (int i = 0; i < arr.length; i++) {
            String[] tmpArr = strArray[i].split("\\.");
            if(tmpArr==null || tmpArr.length!=2){
                continue;
            }
            arr[i] = Long.parseLong(tmpArr[0]);
        }

        Arrays.sort(arr);

        for (int i = 0; i < arr.length; i++) {
            String filename = arr[i] + ".pdf";
            System.out.println(filename);
            strArray[i] = pdfDic +File.separator+ filename;
        }

        return mergePdfFiles(strArray, mergeFilePath);

    }

    private static boolean mergePdfFiles(String[] files, String newfile) {
        boolean retValue = false;
        Document document = null;
        try {
            document = new Document(new PdfReader(files[0]).getPageSize(1));
            PdfSmartCopy copy = new PdfSmartCopy(document, new FileOutputStream(newfile));
            document.open();
            for (int i = 0; i < files.length; i++) {
                PdfReader reader = new PdfReader(files[i]);
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
            retValue = true;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("执行结束");
            document.close();
        }
        return retValue;
    }


    /**
     * 批量转换目录下的html文件为pdf
     * @param htmlDic  要转换的html文件的目录
     * @param pdfDic   要保存的pdf文件的目录
     * @throws InterruptedException
     */

    public static void dirHtmlToPdf(String htmlDic,String pdfDic) throws InterruptedException {
        File file = new File(htmlDic);
        String[] list = file.list();
        CountDownLatch countDownLatch = new CountDownLatch(list.length / 100 + 1);
        for (int i = 0; i < countDownLatch.getCount(); i++) {
            int finalI = i;
            new Thread(() -> {
                for (int j = finalI * 100; j < list.length && j < (finalI + 1) * 100; j++) {
                    String filename = list[j];
                    System.out.println(filename);
                    String htmlpath = htmlDic +File.separator+ filename;
                    String[] name = filename.split("\\.");
                    String pdfpath = pdfDic+File.separator + name[0] + ".pdf";
                    new PdfUtil().doHtml2pdf(htmlpath, pdfpath);
                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }

    public byte[] doHtml2pdf(String src, String dest) {
        String space = " ";
        String wkhtmltopdf = findExecutable();

        if (StringUtils.isEmpty(wkhtmltopdf)) {
            System.out.println("no wkhtmltopdf found!");
            throw new RuntimeException("html转换pdf出错了");
        }

        File file = new File(dest);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            boolean dirsCreation = parent.mkdirs();
            System.out.println("create dir for new file," + dirsCreation);
        }

        StringBuilder cmd = new StringBuilder();
        cmd.append(findExecutable()).append(space)
                .append("--disable-external-links").append(space)
                .append("--disable-internal-links").append(space)
                .append("--lowquality ").append(space)
                .append("--enable-local-file-access").append(space)
                .append("--image-dpi").append(space).append(300).append(space)
                .append("--image-quality").append(space).append(94).append(space)
                .append("--log-level").append(space).append("info").append(space)
                .append(src)
                .append(space)
                .append(dest);

        InputStream is = null;

        try {
            String finalCmd = cmd.toString();
            System.out.println("final cmd:" + finalCmd);

            Process proc = Runtime.getRuntime().exec(finalCmd);

            new Thread(new ProcessStreamHandler(proc.getInputStream())).start();
            new Thread(new ProcessStreamHandler(proc.getErrorStream())).start();

            proc.waitFor();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            is = new FileInputStream(file);
            byte[] buf = new byte[1024];

            while (is.read(buf, 0, buf.length) != -1) {
                baos.write(buf, 0, buf.length);
            }

            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("html转换pdf出错了");

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据当前系统返回 wkhtmltopdf 执行命令
     *
     */
    public String findExecutable() {
        Process p;
        try {
            String osname = System.getProperty("os.name").toLowerCase();
            String cmd = osname.contains("windows") ? "where wkhtmltopdf" : "which wkhtmltopdf";
            p = Runtime.getRuntime().exec(cmd);
            new Thread(new ProcessStreamHandler(p.getErrorStream())).start();
            p.waitFor();
            return IOUtils.toString(p.getInputStream(), Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public class ProcessStreamHandler implements Runnable {
        public InputStream is;

        public ProcessStreamHandler(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            try {
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                reader = new BufferedReader(isr);
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}


