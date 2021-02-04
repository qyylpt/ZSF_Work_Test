package com.zsf.m_camera.adapter.bean;

/**
 * @author : zsf
 * @date : 2021/2/4 12:16 PM
 * @desc :
 */
public class CollectionFileBean {

    private String fileName;
    private String filePath;
    private String fileSize;
    private String fileMd5;
    private int fileType;
    private long fileCreateTime;
    private String fileReportProgress;
    private int fileReportStatus;
    private String fileReportScenes;
    private String fileReportDescription;
    private String fileReportLongitude;
    private String fileReportLatitude;
    private String fileReportRadius;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public long getFileCreateTime() {
        return fileCreateTime;
    }

    public void setFileCreateTime(long fileCreateTime) {
        this.fileCreateTime = fileCreateTime;
    }

    public String getFileReportProgress() {
        return fileReportProgress;
    }

    public void setFileReportProgress(String fileReportProgress) {
        this.fileReportProgress = fileReportProgress;
    }

    public int getFileReportStatus() {
        return fileReportStatus;
    }

    public void setFileReportStatus(int fileReportStatus) {
        this.fileReportStatus = fileReportStatus;
    }

    public String getFileReportScenes() {
        return fileReportScenes;
    }

    public void setFileReportScenes(String fileReportScenes) {
        this.fileReportScenes = fileReportScenes;
    }

    public String getFileReportDescription() {
        return fileReportDescription;
    }

    public void setFileReportDescription(String fileReportDescription) {
        this.fileReportDescription = fileReportDescription;
    }

    public String getFileReportLongitude() {
        return fileReportLongitude;
    }

    public void setFileReportLongitude(String fileReportLongitude) {
        this.fileReportLongitude = fileReportLongitude;
    }

    public String getFileReportLatitude() {
        return fileReportLatitude;
    }

    public void setFileReportLatitude(String fileReportLatitude) {
        this.fileReportLatitude = fileReportLatitude;
    }

    public String getFileReportRadius() {
        return fileReportRadius;
    }

    public void setFileReportRadius(String fileReportRadius) {
        this.fileReportRadius = fileReportRadius;
    }

    @Override
    public String toString() {
        return "CollectionFileItem{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", fileMd5='" + fileMd5 + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileCreateTime=" + fileCreateTime +
                ", fileReportProgress='" + fileReportProgress + '\'' +
                ", fileReportStatus=" + fileReportStatus +
                ", fileReportScenes='" + fileReportScenes + '\'' +
                ", fileReportDescription='" + fileReportDescription + '\'' +
                ", fileReportLongitude='" + fileReportLongitude + '\'' +
                ", fileReportLatitude='" + fileReportLatitude + '\'' +
                ", fileReportRadius='" + fileReportRadius + '\'' +
                '}';
    }
}
