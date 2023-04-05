package com.maybank.pestos.service.jms;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.NumberUtils;

import com.jayway.jsonpath.JsonPath;

public class JMSUtil {

	private JMSUtil() {}
	private static final Logger log = LoggerFactory.getLogger(JMSUtil.class);

	private static final String ESB_TIMESTAMP_FORMAT = "yyMMddHHmmss";
	public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
	private static final String DATE_FORMAT = "ddMMyyyy";
	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	private static final String JSON_PATH_MSG_BODY = "$.Msg.MsgBody.";
	private static final String JSON_PATH_MSG_HEADER = "$.Msg.MsgHeader.";
	private static final String EMPTY = "";
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	private static final String MDM_DATE_FORMAT = "MM/dd/yyyy";
	
	public static String formatDecimalToString(BigDecimal value) {
		if(value == null) {
			return "";
		}
		value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
		return value.multiply(BigDecimal.valueOf(100)).setScale(0).toString();
	}
	
	public static BigDecimal formatStringToDecimal(String value) {
		if(StringUtils.isBlank(value)) {
			return null;
		}
		if(value.length() < 3) {
			value = String.format("%03d", Long.parseLong(value)); 
		}
		StringBuilder str = new StringBuilder(value);
		str.insert(value.length() - 2, ".");
		return new BigDecimal(str.toString());
	}
	
	public static String getCurrentTimestamp() {
		SimpleDateFormat df = new SimpleDateFormat(TIMESTAMP_FORMAT);
		return df.format(new Date());
	}
	
	public static String getCurrentTimestamp(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(new Date());
	}
	
	public static String getCurrentDate() {
		return formatDateToString(new Date());
	}
	
	public static String getCurrentDate2() {
		return formatDateToString2(new Date());
	}
	
	public static LocalDateTime formatTimestamp(String timestamp) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
		return LocalDateTime.parse(timestamp, formatter);
	}
	
	public static String formatLocalDateToString(LocalDate date) {
		if(date == null)
			return "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		return date.format(formatter);
	}
	
	public static String formatLocalDateToStr(LocalDate date) {
		if(date == null)
			return "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
		return date.format(formatter);
	}
	
	public static String formatDateToString(Date date) {
		if(date == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		return df.format(date);
	}
	
	public static String formatDateToString2(Date date) {
		if(date == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return df.format(date);
	}

	public static Date formatStringToDate1(String date) {
		if(StringUtils.isBlank(date))
			return null;
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		try {
			return df.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static LocalDate formatStringToLocalDate(String date) {
		if(StringUtils.isBlank(date))
			return null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		return LocalDate.parse(date, formatter);
	}
	
	public static LocalDate formatESBStringToLocalDate(String date) {
		if(StringUtils.isBlank(date) || date.equals("00000000"))
			return null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD);
		return LocalDate.parse(date, formatter);
	}
	
	public static Date formatStringToDate(String date) {
		Date dateVal = null;
		if(StringUtils.isBlank(date))
			return null;
		
		SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		try {
			dateVal = formatter.parse(date);
		}catch  (ParseException e) {
			log.error("formatStringToDate error >> {} ", e.getMessage());
		}
		return dateVal; 
	}
	
	public static LocalDate formatMDMStringToLocalDate(String date) {
		if(StringUtils.isBlank(date))
			return null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MDM_DATE_FORMAT);
		return LocalDate.parse(date, formatter);
	}
	
	public static Object getJsonPathValue(String json, String path) {
		try {
			return JsonPath.read(json, path);
		} catch (Exception e) {
			log.debug("getJsonPathValue {} -> {}", path, e.getMessage());
		}
		return null;
	}
	
	public static String getMsgBodyValue(String json, String path) {
		try {
			return JsonPath.read(json, JSON_PATH_MSG_BODY + path);
		} catch (Exception e) {
			log.debug("getMsgBodyValue {} -> {}", path, e.getMessage());
		}
		return EMPTY;
	}
	
	public static String getMsgHeaderValue(String json, String path) {
		try {
			return JsonPath.read(json, JSON_PATH_MSG_HEADER + path);
		} catch (Exception e) {
			log.debug("getMsgHeaderValue {} -> {}", path, e.getMessage());
		}
		return EMPTY;
	}

	public static String generateMsgIDwithUUID() {
		return JMSConstants.APP_NAME + UUID.randomUUID().toString();
	}
	
	public static String generateMsgID() {
		SimpleDateFormat df = new SimpleDateFormat(ESB_TIMESTAMP_FORMAT);
		String msgID = JMSConstants.APP_NAME + df.format(new Date()) + (10000 + RANDOM.nextInt(90000));
		return msgID;
	}
	
	public static BigDecimal formatJsonPathDecimalValue(String json, String path) {
		try {
			Number value = JsonPath.read(json, path);
			return convertNumberToBigDecimal(value);
		} catch (Exception e) {
			log.debug("formatJsonPathDecimalValue {} -> {}", path, e.getMessage());
		}
		return null;
	}
	
	public static BigDecimal convertNumberToBigDecimal(Number value) {
		try {
			return NumberUtils.convertNumberToTargetClass(value, BigDecimal.class);
		} catch (Exception e) {
			log.debug("convertNumberToBigDecimal: {}", e.getMessage());
		}
		return null;
	}
	
}
