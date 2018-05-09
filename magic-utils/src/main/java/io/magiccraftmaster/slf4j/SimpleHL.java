/*
 * Copyright (c) 2018. MagicCraftMaster
 */

package io.magiccraftmaster.slf4j; // Make sure to replace this with the path to the file in your project.

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

import static ch.qos.logback.classic.Level.*;
import static ch.qos.logback.core.pattern.color.ANSIConstants.*;

/**
 * A simple highlighter for console text using logback classic.
 *
 * @author Kaidan Gustave
 */
public class SimpleHL extends ForegroundCompositeConverterBase<ILoggingEvent>
{
	@Override
	protected String getForegroundColorCode(ILoggingEvent event)
	{
		switch(event.getLevel().levelInt)
		{
			case ERROR_INT:
				return BOLD + RED_FG;
			case WARN_INT:
				return YELLOW_FG;
			case INFO_INT:
				return GREEN_FG;
			case DEBUG_INT:
				return CYAN_FG;
			default:
				return DEFAULT_FG;
		}
	}
}
