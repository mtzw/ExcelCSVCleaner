package com.github.mtzw.filecleaner.unsplit;

import static com.github.mtzw.filecleaner.ExitStatus.*;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mtzw.filecleaner.Config;
import com.github.mtzw.filecleaner.ExitStatus;

public class UnsplitOutThread implements Callable<ExitStatus> {
	static final Logger LOGGER = LoggerFactory.getLogger("app");
	static final Logger TRACE = LoggerFactory.getLogger("trace");

	private final Config config = new Config();
	private String outputFileName;
	private BlockingQueue<List<String>> queue;
	private Future<ExitStatus> iFuture;

	public UnsplitOutThread(String fileName, BlockingQueue<List<String>> queue,
			Future<ExitStatus> iFuture) {
		this.outputFileName = fileName;
		this.queue = queue;
		this.iFuture = iFuture;
	}

	@Override
	public ExitStatus call() throws Exception {
		long count = 0;
		try (BufferedWriter writer = Files.newBufferedWriter(
				Paths.get(config.getOutDir(), outputFileName),
				config.getOutCharset())) {
			for (;;) {
				if (queue.isEmpty()) {
					if (iFuture.isDone()) {
						ExitStatus iFinishStatus = iFuture.get();
						if (iFinishStatus.equals(正常終了)) {
							LOGGER.info(String.format("出力完了 [%s] - %s 件出力しました。", outputFileName, count));
							break;
						} else {
							return iFinishStatus;
						}
					}
					continue;
				}

				List<String> lines = queue.take();
				for (String line : lines) {
					count++;
					writer.append(line).append(System.lineSeparator());
				}
				if (count % config.getInterval().intValue() == 0) {
					TRACE.info(String.format("出力中... [%s] - %s", outputFileName, count));
				}
			}
		}
		return 正常終了;
	}

}
