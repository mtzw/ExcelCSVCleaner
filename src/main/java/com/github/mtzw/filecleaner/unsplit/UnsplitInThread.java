package com.github.mtzw.filecleaner.unsplit;

import static com.github.mtzw.filecleaner.ExitStatus.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mtzw.filecleaner.Config;
import com.github.mtzw.filecleaner.ExitStatus;
import com.github.mtzw.filecleaner.StringUtils;
import com.opencsv.CSVReader;

public class UnsplitInThread implements Callable<ExitStatus> {
	static final Logger LOGGER = LoggerFactory.getLogger("app");
	static final Logger TRACE = LoggerFactory.getLogger("trace");

	private final Config config = new Config();
	private String inputFileName;
	private BlockingQueue<List<String>> queue;

	public UnsplitInThread(String fileName, BlockingQueue<List<String>> queue) {
		this.inputFileName = fileName;
		this.queue = queue;
	}

	@Override
	public ExitStatus call() throws Exception {
		try (CSVReader reader = new CSVReader(Files.newBufferedReader(
				Paths.get(config.getInDir(), inputFileName),
				config.getInCharset()))) {
			int csvElementSize = 0;
			long count = 0;
			List<String> lines = new ArrayList<String>();
			for (;;) {
				String[] csv = reader.readNext();
				if (csv == null) {
					offer(lines);
					LOGGER.info(String.format("入力完了 [%s] - %s 件入力しました。", inputFileName, count));
					break;
				} else {
					if (count < 1) {
						csvElementSize = csv.length;
					} else if (csvElementSize != csv.length) {
						LOGGER.error(String.format("CSVの要素数が途中で切り替わりました。入力ファイルの%s行目を確認して下さい。", count + 1));
						return 入力異常;
					}
				}

				List<String> cleaned = new ArrayList<String>();
				for (int i = 0; i < csv.length; i++) {
					cleaned.add(StringUtils.trimWhiteSpace(csv[i].replaceAll("\n", "\\\\r\\\\n")));
				}

				String line = StringUtils.listToCsv(cleaned);
				if (lines.size() == config.getInterval().intValue()) {
					offer(lines);
					TRACE.info(String.format("入力中... [%s] - %s", inputFileName, count));
					lines = new ArrayList<String>();
				}
				count++;
				lines.add(line);
			}
		}
		return 正常終了;
	}

	private void offer(List<String> lines) throws InterruptedException {
		try {
			queue.offer(lines, 10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			LOGGER.error("キューへの挿入がタイムアウトしました。デッドロックが発生している可能性があります。", e);
			throw e;
		}
	}

}
