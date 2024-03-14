package ru.lightdigital.semenov.proc_sys;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication
public class ProcSysApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ProcSysApplication.class)
				.beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
				.run(args);
	}

}
