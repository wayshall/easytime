package org.onetwo.core.weak.watch;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.onetwo.core.logger.Logger;

@SuppressWarnings({"unchecked"})
public class FileMonitor {
	protected final Logger logger = Logger.getLogger(FileMonitor.class);
	// private static final FileMonitor instance = new FileMonitor();
	private Timer timer;
	private Map timerEntries;

	public FileMonitor() {
		this.timerEntries = new HashMap();
		this.timer = new Timer(true);
	}

	/*
	 * public static FileMonitor getInstance() { return instance; }
	 */

	/**
	 * Add a file to the monitor
	 * 
	 * @param listener
	 *            The file listener
	 * @param file
	 *            The file to watch
	 * @param period
	 *            The watch interval.
	 */
	public void addFileChangeListener(FileChangeListener listener, File file,
			long period) {
		this.removeFileChangeListener(file.getName());

		logger.info("Watching " + file.getName());

		FileMonitorTask task = new FileMonitorTask(listener, file);

		this.timerEntries.put(file.getName(), task);
		this.timer.schedule(task, period, period);
	}

	/**
	 * Stop watching a file
	 * 
	 * @param listener
	 *            The file listener
	 * @param filename
	 *            The filename to keep watch
	 */
	public void removeFileChangeListener(String filename) {
		FileMonitorTask task = (FileMonitorTask) this.timerEntries
				.remove(filename);

		if (task != null) {
			task.cancel();
		}
	}

	private static class FileMonitorTask extends TimerTask {
		private FileChangeListener listener;
		private File monitoredFile;
		private long lastModified;

		public FileMonitorTask(FileChangeListener listener, String fileName) {
			this(listener, new File(fileName));
		}

		public FileMonitorTask(FileChangeListener listener, File file) {
			this.listener = listener;
			this.monitoredFile = file;
			if (!this.monitoredFile.exists()) {
				return;
			}

			this.lastModified = this.monitoredFile.lastModified();
		}

		public void run() {
			long latestChange = this.monitoredFile.lastModified();
			if (this.lastModified != latestChange) {
				this.lastModified = latestChange;

				this.listener.fileChanged(this.monitoredFile);
			}
		}
	}
}
