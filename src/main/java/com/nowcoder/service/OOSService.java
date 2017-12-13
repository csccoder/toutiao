package com.nowcoder.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OOSService {
    String saveImage(MultipartFile file) throws IOException;
}
