package com.magdyradwan.my_file_sender.dto;

import android.icu.util.UniversalTimeScale;

import com.magdyradwan.my_file_sender.helpers.Utility;
import com.magdyradwan.my_file_sender.models.IJsonConvertable;

public class UploadFileDTO implements IJsonConvertable {
    private String fileName;
    private String bytes;
    private String mimeType;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public UploadFileDTO(String fileName, String bytes, String mimeType) {
        this.fileName = fileName;
        this.bytes = bytes;
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    @Override
    public String convertToJson() {
        StringBuilder stringBuilder = new StringBuilder("{\"fileName\":\"");
        stringBuilder.append(fileName).append("\",")
                .append("\"Base64\":\"")
                .append(bytes)
                .append("\",")
                .append("\"MimeType\":\"")
                .append(mimeType).append("\"}");
        return stringBuilder.toString();
    }
}
