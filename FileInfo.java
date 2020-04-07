package com.kayakwise.gray.api.util;

import java.io.File;
import java.util.Date;

public class FileInfo {
	private String fileName;
    private String filePath;
    private String fullFileName;
    private Date createTime;
    private Date updateTime;

    protected FileInfo() {
	}

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

	public String getFullFileName() {
		return fullFileName;
	}

	public void setFullFileName(String fullFileName) {
		this.fullFileName = fullFileName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public FileInfo(String fileName)
    {
        this.fullFileName = fileName;
        this.fileName = FileUtil.getFileName(fileName);
        this.filePath = FileUtil.getParentPath(fileName);
        File file=new File(fileName);
        this.updateTime = new Date(file.lastModified());
    }
}
