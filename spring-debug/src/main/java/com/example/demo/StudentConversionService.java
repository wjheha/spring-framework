package com.example.demo;

import com.example.service.StudentService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

/**
 * @author wangjie
 * Created on 2020/4/13 21:26
 */
public class StudentConversionService implements Converter<String, StudentService> {

	@Override
	public StudentService convert(String source) {
		if(StringUtils.hasLength(source)){
			String[] sources = source.split("#");

			StudentService studentService = new StudentService();
			studentService.setAge(Integer.parseInt(sources[0]));
			studentService.setName(sources[1]);

			return studentService;
		}
		return null;
	}
}
