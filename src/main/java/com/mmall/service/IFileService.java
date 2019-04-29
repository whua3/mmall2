package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: whua
 * @create: 2019/04/29 15:33
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
