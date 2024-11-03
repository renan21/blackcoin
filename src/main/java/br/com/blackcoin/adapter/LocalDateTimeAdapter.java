package br.com.blackcoin.adapter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	
	@Override
	public void write(JsonWriter out, LocalDateTime value) throws IOException {
		if(value == null) {
			out.nullValue();
		} else {
			out.value(value.format(FORMATTER));
		}
	}
	
	@Override
	public LocalDateTime read(JsonReader in) throws IOException {
		if(in.peek() == null) {
			in.nextNull();
			return null;
		}
		return LocalDateTime.parse(in.nextString(), FORMATTER);
	}

}
