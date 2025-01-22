package com.test.back.hesi.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Component
public final class DateUtil {
	
	public final int UTIL_VERSION = 1;

	public String getToday() 
	{
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
	public String getDatetime() 
	{
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	public String getDatetimeDetail() 
	{
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}
	@SuppressWarnings("deprecation")
	public String getDatetimeDetailAfterMinute(int afterMinute) 
	{
		Date date = new Date();
		date.setMinutes(afterMinute);
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
	}
	public String getDatetimeZoneDetail()
	{
		Calendar calendar = Calendar.getInstance(Locale.KOREA);
		StringBuilder stb = new StringBuilder();
		stb.append(calendar.get(Calendar.YEAR));
		stb.append(
				(calendar.get(Calendar.MONTH)+1 < 10 ? "0" : "")
					+ calendar.get(Calendar.MONTH)+1
		);
		stb.append(
				(calendar.get(Calendar.DATE) < 10 ? "0" : "")
					+ calendar.get(Calendar.DATE)
		);
		stb.append(
				(calendar.get(Calendar.HOUR) < 10 ? "0" : "")
					+ calendar.get(Calendar.HOUR)
		);
		stb.append(
				(calendar.get(Calendar.MINUTE) < 10 ? "0" : "")
					+ calendar.get(Calendar.MINUTE)
		);
		stb.append(
				(calendar.get(Calendar.SECOND) < 10 ? "0" : "")
					+ calendar.get(Calendar.SECOND)
		);
		return stb.toString();
	}
    
    public LocalDateTime getThisTime() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }
    
    public LocalDateTime getMaxEndTime() {
        return LocalDateTime.MAX.minusDays(1);
    }
    
    public LocalDateTime getTimeIfNull(LocalDateTime time) {
        return time == null ? this.getThisTime() : time;
    }
}
