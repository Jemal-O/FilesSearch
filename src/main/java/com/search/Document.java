package com.search;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class Document {

    private String filePath;
    private LocalDate date;
    private long size;

    public Document(String file, long dateInLong, long size) {
        this.filePath = file;
        this.date = Instant.ofEpochMilli(dateInLong).atZone(ZoneId.systemDefault()).toLocalDate();
        this.size = size;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "[" + "\n" +
                "filePath='" + filePath + "\n" +
                "date=" + date + "\n" +
                "size=" + size + ']';
    }

    @Override
    public boolean equals(Object o) {
        return o.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        int result = filePath.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}
