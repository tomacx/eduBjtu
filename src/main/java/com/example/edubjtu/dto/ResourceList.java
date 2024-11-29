package com.example.edubjtu.dto;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceList {
    private Long id;

    private Long courseId; // 假设你有 Course 实体类

    private String fileName;

    private String fileType;

    public ResourceList(Long id, Long courseId, String fileName, String fileType) {
        this.id = id;
        this.courseId = courseId;
        // 从文件路径中提取文件名，去掉路径部分
        this.fileName = extractFileName(fileName);

        // 根据 MIME 类型获取文件扩展名
        this.fileType = getFileExtension(fileType);
    }

    public ResourceList() {

    }

    // 提取文件名，不包含路径和扩展名
    private String extractFileName(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        // 从路径中提取文件名
        String[] parts = filePath.split("\\\\");
        String fileWithExtension = parts[parts.length - 1];

        // 去掉扩展名
        int dotIndex = fileWithExtension.lastIndexOf(".");
        if (dotIndex > 0) {
            return fileWithExtension.substring(0, dotIndex);
        }
        return fileWithExtension;
    }

    // 根据 MIME 类型获取文件扩展名
    private String getFileExtension(String mimeType) {
        if (mimeType == null) {
            return null;
        }
        switch (mimeType) {
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                return "pptx";
            case "application/pdf":
                return "pdf";
            case "application/msword":
                return "docx";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return "docx";
            case "application/vnd.ms-excel":
                return "xls";
            case "application/zip":
                return "zip";
            // 其他 MIME 类型可以继续添加
            default:
                return "unknown";
        }
    }
}
