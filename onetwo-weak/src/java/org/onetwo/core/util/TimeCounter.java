package org.onetwo.core.util;

import java.util.Date;

import org.onetwo.core.logger.Logger;

public class TimeCounter {
	private static final Logger log = Logger.getLogger(TimeCounter.class);

	private Date start;
	private Date stop;
	private long costTime;

	public TimeCounter() {
	}

	@SuppressWarnings("deprecation")
	public Date start() {
		long start = System.currentTimeMillis();
		this.start = new Date(start);
		log.info(" ----->>> start time : " + this.start.toLocaleString());
		return this.start;
	}

	@SuppressWarnings("deprecation")
	public Date stop() {
		long stop = System.currentTimeMillis();
		this.stop = new Date(stop);
		this.costTime = this.stop.getTime() - this.start.getTime();
		System.out.print(" ----->>> stop time : " + this.start.toLocaleString() +" cost total time : " + this.costTime+", (second): " + (this.costTime / 1000));
		return this.stop;
	}

}
