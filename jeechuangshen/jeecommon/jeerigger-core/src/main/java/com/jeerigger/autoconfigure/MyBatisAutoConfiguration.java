package com.jeerigger.autoconfigure;

  import com.jeerigger.datasource.annotation.DataSource;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MyBatisAutoConfiguration {

    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer(Environment environment){
        MapperScannerConfigurer mapperScannerConfigurer=new MapperScannerConfigurer();
        String basePackage=environment.getProperty("jeerigger.mybatis.scanBasePackage");
        if(basePackage==null || basePackage.equals("")){
            basePackage="com.jeerigger.module";
        }
        if(!basePackage.contains("com.jeerigger.module")){
            basePackage="com.jeerigger.module," + basePackage;
        }
        mapperScannerConfigurer.setBasePackage(basePackage);
        mapperScannerConfigurer.setAnnotationClass(DataSource.class);
        return mapperScannerConfigurer;
    }
}
