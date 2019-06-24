package com.jayce.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jayce.comm.util.FileUtil;
import com.jayce.service.user.UserService;

@Controller
public class FileController {
	
	private Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/turnToUpload", method = RequestMethod.GET)
	public String index() {
		return "upload.html";
	}
	
	// 文件上传
	@RequestMapping(value = "/upload")
	@ResponseBody
	public Map<String,Object> upload(@RequestParam("fileName") MultipartFile file) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", "false");
		map.put("msg", "上传失败!");
		if (file.isEmpty()) {
			map.put("msg", "上传文件不能为空!");
			return map;
		}
		// 获取文件名
		String fileName = file.getOriginalFilename();
		logger.info("上传的文件名为：" + fileName);
		// 获取文件的后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		if(!".txt".equals(suffixName)) {
			map.put("msg", "请上传格式为txt的文件!");
			return map;
		}
		// 文件上传后的路径
		File dest = new File(FileUtil.getFileName("classpath://user.txt"));
		// 检测是否存在目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		try {
			file.transferTo(dest);
			userService.refreshUsers();
			map.put("status", "true");
			map.put("msg", "上传成功!");
			logger.info(fileName+"上传成功");
			return map;
		} catch (Exception e) {
			logger.error("文件上传失败", e);
		}
		return map;
	}

}
