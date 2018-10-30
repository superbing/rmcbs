package com.bfd.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class CodeConvert extends ClassicConverter {
    
    @Override
    public String convert(ILoggingEvent event) {
        if (event.getLevel() == Level.ERROR) {
            return "1500";
        }
        return "1200";
    }
}
