package com.github.mtzw.filecleaner.unsplit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mtzw.filecleaner.Config;
import com.github.mtzw.filecleaner.ExitStatus;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Queues;

/**
 * 俗に言うExcel仕様のCSVをきれいにします。
 */
public class FileCleaner {
	static final Logger LOGGER = LoggerFactory.getLogger("app");

	public static void main(String[] args) {
		LOGGER.info("CSVファイルのクリーニングを開始します。");
		final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		final BlockingQueue<List<String>> queue = Queues.newArrayBlockingQueue(50);
		final Config config = new Config();
		final Path inDir = Paths.get(config.getInDir());
		final Stopwatch stopwatch = Stopwatch.createStarted();
		int systemExitCode = 0;
		try {
			Files.list(inDir).map(file -> file.getFileName().toString()).forEach(fileName -> {
				LOGGER.info(String.format("処理開始 [%s]", fileName));

				ExitStatus result = null;;
				try {
					Future<ExitStatus> iFuture = threadPool.submit(new UnsplitInThread(fileName, queue));
					Future<ExitStatus> oFuture = threadPool.submit(new UnsplitOutThread(fileName, queue, iFuture));
					result = oFuture.get();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				} finally {
					if (result.equals(ExitStatus.正常終了)) {
						LOGGER.info(String.format("処理終了 [%s] (%s)", fileName, stopwatch.toString()));
					} else {
						LOGGER.info(String.format("処理中断 [%s] (%s)", fileName, stopwatch.toString()));
					}
				}
			});
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			systemExitCode = 9;
		} finally {
			threadPool.shutdown();
			stopwatch.stop();
		}
		LOGGER.info(String.format("CSVファイルのクリーニングが終了しました。トータルの処理時間は %s でした。", stopwatch.toString()));
		if (args == null) {
			System.exit(systemExitCode);
		}
	}

}
