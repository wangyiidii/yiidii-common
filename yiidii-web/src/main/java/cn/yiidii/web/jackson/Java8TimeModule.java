package cn.yiidii.web.jackson;

import cn.yiidii.web.constant.CommonConstant;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * java 8 时间默认序列化
 *
 * @author L.cm
 * @author lishanbu
 */
public class Java8TimeModule extends SimpleModule {

	public Java8TimeModule() {
		super(PackageVersion.VERSION);
		this.addSerializer(LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(CommonConstant.NORM_DATETIME_PATTERN)));
		this.addSerializer(LocalDate.class,
				new LocalDateSerializer(DateTimeFormatter.ofPattern(CommonConstant.NORM_DATE_PATTERN)));
		this.addSerializer(LocalTime.class,
				new LocalTimeSerializer(DateTimeFormatter.ofPattern(CommonConstant.NORM_TIME_PATTERN)));
		this.addDeserializer(LocalDateTime.class,
				new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(CommonConstant.NORM_DATETIME_PATTERN)));
		this.addDeserializer(LocalDate.class,
				new LocalDateDeserializer(DateTimeFormatter.ofPattern(CommonConstant.NORM_DATE_PATTERN)));
		this.addDeserializer(LocalTime.class,
				new LocalTimeDeserializer(DateTimeFormatter.ofPattern(CommonConstant.NORM_TIME_PATTERN)));
	}

}
