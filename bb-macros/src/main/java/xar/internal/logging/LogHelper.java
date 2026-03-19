/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package xar.internal.logging;

import org.slf4j.Logger;

public final class LogHelper {
	private static final String SEPARATOR = "**********";

	private LogHelper() {
	}

	public static void debug(Logger logger, String title, Object... lines) {
		if (logger.isDebugEnabled()) {
			logger.debug(format(title, lines));
		}
	}

	public static void info(Logger logger, String title, Object... lines) {
		logger.info(format(title, lines));
	}

	public static void warn(Logger logger, String title, Object... lines) {
		logger.warn(format(title, lines));
	}

	public static void error(Logger logger, String title, Exception e, Object... lines) {
		logger.error(format(title, lines), e);
	}

	private static String format(String title, Object... lines) {
		StringBuilder sb = new StringBuilder();
		sb.append("*****").append(title).append("*****").append(System.lineSeparator());
		for (Object line : lines) {
			sb.append(line).append(System.lineSeparator());
		}
		sb.append(SEPARATOR);
		return sb.toString();
	}
}
