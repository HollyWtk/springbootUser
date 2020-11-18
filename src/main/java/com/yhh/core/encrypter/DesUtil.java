package com.yhh.core.encrypter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public final class DesUtil {

    public static final boolean hisuHexStrToCBCD(byte[] hisuIn, byte[] hisuOut, int hisuLen) {
        byte[] hisuAsciiCode = { 10, 11, 12, 13, 14, 15 };

        if (hisuLen > hisuIn.length) {
            return false;
        }

        if (hisuLen % 2 != 0) {
            return false;
        }

        byte[] hisuTemp = new byte[hisuLen];

        for (int index = 0; index < hisuLen; index++) {
            if ((hisuIn[index] >= 48) && (hisuIn[index] <= 57))
                hisuTemp[index] = (byte) (hisuIn[index] - 48);
            else if ((hisuIn[index] >= 65) && (hisuIn[index] <= 70))
                hisuTemp[index] = hisuAsciiCode[(hisuIn[index] - 65)];
            else if ((hisuIn[index] >= 97) && (hisuIn[index] <= 102))
                hisuTemp[index] = hisuAsciiCode[(hisuIn[index] - 97)];
            else {
                return false;
            }
        }
        for (int index = 0; index < hisuLen / 2; index++) {
            hisuOut[index] = (byte) (hisuTemp[(2 * index)] * 16 + hisuTemp[(2 * index + 1)]);
        }

        return true;
    }

    public static final boolean hisuCBCDToHexStr(byte[] hisuInBytes, byte[] hisuOutBytes, int hisuLen) {
        byte[] asciiCode = { 65, 66, 67, 68, 69, 70 };
        if (hisuLen > hisuInBytes.length) {
            return false;
        }

        byte[] hisuTemp = new byte[2 * hisuLen];
        for (int index = 0; index < hisuLen; index++) {
            hisuTemp[(2 * index)] = (byte) ((hisuInBytes[index] & 0xF0) / 16);
            hisuTemp[(2 * index + 1)] = (byte) (hisuInBytes[index] & 0xF);
        }

        for (int i = 0; i < 2 * hisuLen; i++) {
            if ((hisuTemp[i] <= 9) && (hisuTemp[i] >= 0)) {
                hisuOutBytes[i] = (byte) (hisuTemp[i] + 48);
            } else
                hisuOutBytes[i] = asciiCode[(hisuTemp[i] - 10)];
        }

        return true;
    }

    public static final String hisuLeftAddZero(String hisuStr, int hisuTotalLen) {
        String hisuStrTemp = "";
        int hisuLen = hisuStr.length();
        if (hisuLen >= hisuTotalLen)
            return hisuStr;

        int len = hisuTotalLen - hisuLen;

        for (int count = 0; count < len; count++) {
            hisuStrTemp = hisuStrTemp + "0";
        }
        hisuStrTemp = hisuStrTemp + hisuStr;

        return hisuStrTemp;
    }

    public static final String hisuRightAddBlank(String hisuStr, int TotalLen) {
        String hisuStrTemp = hisuStr;
        int hisuLen = hisuStr.getBytes().length;
        if (hisuLen >= TotalLen)
            return hisuStr;

        int len = TotalLen - hisuLen;

        for (int count = 0; count < len; count++) {
            hisuStrTemp = hisuStrTemp + " ";
        }

        return hisuStrTemp;
    }

    public static final Properties hisuGetEnvVars() throws IOException {
        Process hisuProcess = null;
        Properties hisuEnvVars = new Properties();
        Runtime hisuRuntime = Runtime.getRuntime();
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("windows 9") > -1) {
            hisuProcess = hisuRuntime.exec("command.com /c set");
        } else if ((OS.indexOf("nt") > -1) || (OS.indexOf("windows 2000") > -1) || (OS.indexOf("windows xp") > -1)) {
            hisuProcess = hisuRuntime.exec("cmd.exe /c set");
        } else if ((OS.indexOf("unix") > -1) || (OS.indexOf("linux") > -1)) {
            hisuProcess = hisuRuntime.exec("/bin/env");
        }
        BufferedReader hisuBr = new BufferedReader(new InputStreamReader(hisuProcess.getInputStream()));
        String hisuLine;
        while ((hisuLine = hisuBr.readLine()) != null) {
            int index = hisuLine.indexOf('=');
            String hisuKey = hisuLine.substring(0, index);
            String hisuValue = hisuLine.substring(index + 1);
            hisuEnvVars.setProperty(hisuKey.toUpperCase(), hisuValue);
        }
        hisuProcess.destroy();
        return hisuEnvVars;
    }

    public static final void hisuPrintHexString(String hisuHint, byte[] hisuBytes) {
        System.out.print(hisuHint);
        for (int i = 0; i < hisuBytes.length; i++) {
            String hex = Integer.toHexString(hisuBytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase() + " ");
        }
        System.out.println("");
    }

    public static final String hisuBytes2HexString(byte[] hisuBytes) {
        String hisuRet = "";
        for (int count = 0; count < hisuBytes.length; count++) {
            String hisuHex = Integer.toHexString(hisuBytes[count] & 0xFF);
            if (hisuHex.length() == 1) {
                hisuHex = '0' + hisuHex;
            }
            hisuRet = hisuRet + hisuHex.toUpperCase();
        }
        return hisuRet;
    }

    public static final byte hisuUniteBytes(byte hisuSrc0, byte hisuSrc1) {
        byte hisuByte = Byte.decode("0x" + new String(new byte[] { hisuSrc0 })).byteValue();
        hisuByte = (byte) (hisuByte << 4);
        byte hisuByte1 = Byte.decode("0x" + new String(new byte[] { hisuSrc1 })).byteValue();
        byte hisuRet = (byte) (hisuByte ^ hisuByte1);
        return hisuRet;
    }

    public static final byte[] hisuHexString2Bytes(String hisuSrc) {
        byte[] hisuRet = new byte[hisuSrc.length() / 2];
        byte[] hisuTmpBytes = hisuSrc.getBytes();
        for (int index = 0; index < hisuSrc.length() / 2; index++) {
            hisuRet[index] = hisuUniteBytes(hisuTmpBytes[(index * 2)], hisuTmpBytes[(index * 2 + 1)]);
        }
        return hisuRet;
    }

    public static final byte[] hisuBytesCopy(byte[] hisuBytes, int hisuStart, int hisuLen) throws Exception {
        if ((hisuBytes == null) || (hisuBytes.length < hisuStart + hisuLen))
            throw new Exception("BytesSub bytes为空，或长度不够！");
        byte[] subBytes = new byte[hisuLen];
        System.arraycopy(hisuBytes, hisuStart, subBytes, 0, hisuLen);
        return subBytes;
    }

    public static final byte[] hisuBytesCopy(byte[] hisuBytes, int hisuStart) throws Exception {
        if ((hisuBytes == null) || (hisuBytes.length < hisuStart))
            throw new Exception("BytesSub bytes为空，或长度不够！");
        byte[] subBytes = new byte[hisuBytes.length - hisuStart];
        System.arraycopy(hisuBytes, hisuStart, subBytes, 0, hisuBytes.length - hisuStart);
        return subBytes;
    }

    public static final byte[] hisuAllRightZreoTo8Multiple(byte[] hisuBytes) {
        if (hisuBytes.length % 8 == 0)
            return hisuBytes;
        int len = hisuBytes.length + 8 - hisuBytes.length % 8;
        byte[] hisuNewBytes = new byte[len];
        for (int count = 0; count < len; count++)
            hisuNewBytes[count] = 0;
        System.arraycopy(hisuBytes, 0, hisuNewBytes, 0, hisuBytes.length);
        return hisuNewBytes;
    }

    public static final byte[] hisuAllTrimZreoFrom8Multiple(byte[] hisuBytes) {
        int hisuZreoCount = 0;
        if (hisuBytes.length == 0) {
            return hisuBytes;
        }
        for (int index = hisuBytes.length - 1; index >= hisuBytes.length - 8; index--) {
            if (hisuBytes[index] != 0)
                break;
            hisuZreoCount++;
        }
        byte[] hisuNewBytes = new byte[hisuBytes.length - hisuZreoCount];
        System.arraycopy(hisuBytes, 0, hisuNewBytes, 0, hisuBytes.length - hisuZreoCount);
        return hisuNewBytes;
    }

    public static void hisuDelAllFile(String hisuFilePath) {
        File file = new File(hisuFilePath);
        File[] fileList = file.listFiles();

        String hisuDirPath = null;
        if (fileList != null) {
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isFile()) {
                    fileList[i].delete();
                }

                if (fileList[i].isDirectory()) {
                    hisuDirPath = fileList[i].getPath();

                    hisuDelAllFile(hisuDirPath);
                }
            }

        }

        file.delete();
    }

    public static String hisuCopyfile(String hisuSource, String hisuDestdir) {
        String hisuFileName = "";
        try {
            String hisuFilePath = hisuSource;
            File hisuOpenFile = new File(hisuFilePath);
            File hisuSysPath = new File(hisuDestdir);

            FileInputStream hisuFis = new FileInputStream(hisuOpenFile);
            FileOutputStream hisuFos = new FileOutputStream(hisuSysPath);
            byte[] tempch = new byte[hisuFis.available()];
            hisuFis.read(tempch);
            hisuFos.write(tempch);
            hisuFos.flush();
            hisuFos.close();
            hisuFis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hisuFileName;
    }

    public static String hisuTestRunTime(long[] timeArr) {
        int index = 0;
        long maxTime = 0L;
        long cmpTemp = 0L;
        String message = "比较结果如下:";
        if (timeArr.length < 2)
            return "数组长度不够！至少要有两个时间参数！";
        for (; (index < timeArr.length - 1) && (timeArr[(index + 1)] > 0L); index++) {
            cmpTemp = timeArr[(index + 1)] - timeArr[index];
            if (cmpTemp > maxTime)
                maxTime = cmpTemp;
            message = message + index + "-" + (index + 1) + ":" + cmpTemp + ", ";
        }
        return message;
    }
}
