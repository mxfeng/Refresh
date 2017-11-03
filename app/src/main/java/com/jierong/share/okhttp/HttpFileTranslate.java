package com.jierong.share.okhttp;

import android.os.Environment;
import android.text.TextUtils;
import com.jierong.share.Constants;
import com.jierong.share.util.LogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.utils.HttpUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Response;

/**
 * 文件的回调下载进度监听管理类
 * @author qingf
 */
public abstract class HttpFileTranslate extends AbsCallback<File> {
	public static final String Default_Path = File.separator + Constants.New_Down_Path + File.separator; // 下载目标文件夹
	private String destFileDir; // 目标文件存储的文件夹路径
	private String destFileName; // 目标文件存储的文件名

	public HttpFileTranslate() {
		this(null);
	}

	public HttpFileTranslate(String destFileName) {
		this(Environment.getExternalStorageDirectory() + Default_Path, destFileName);
	}

	public HttpFileTranslate(String destFileDir, String destFileName) {
		this.destFileDir = destFileDir;
		this.destFileName = destFileName;
	}

	@Override
	public File convertSuccess(Response response) throws Exception {
		return saveFile(response);
	}

	private File saveFile(Response response) throws IOException {
		if (TextUtils.isEmpty(destFileDir))
			destFileDir = Environment.getExternalStorageDirectory() + Default_Path;
		if (TextUtils.isEmpty(destFileName))
			destFileName = HttpUtils.getNetFileName(response, response.request().url().toString());
		File dir = new File(destFileDir);
		if (!dir.exists()) dir.mkdirs();
		File file = new File(dir, destFileName);
		if (file.exists()) file.delete();
		long lastRefreshUiTime = 0;	// 最后一次刷新的时间
		long lastWriteBytes = 0;	// 最后一次写入字节数据

		InputStream is = null;
		byte[] buf = new byte[2048];
		FileOutputStream fos = null;
		try {
			is = response.body().byteStream();
			final long total = response.body().contentLength();
			long sum = 0;
			int len;
			fos = new FileOutputStream(file);
			while ((len = is.read(buf)) != -1) {
				sum += len;
				fos.write(buf, 0, len);
				final long finalSum = sum;

				long curTime = System.currentTimeMillis();
				// 每200毫秒刷新一次数据
				if (curTime - lastRefreshUiTime >= 200 || finalSum == total) {
					// 计算下载速度
					long diffTime = (curTime - lastRefreshUiTime) / 1000;
					if (diffTime == 0) diffTime += 1;
					long diffBytes = finalSum - lastWriteBytes;
					final long networkSpeed = diffBytes / diffTime;
					OkGo.getInstance().getDelivery().post(new Runnable() {
						@Override
						public void run() {
							downloadProgress(finalSum, total, finalSum * 1.0f / total, networkSpeed); // 进度回调的方法
						}
					});

					lastRefreshUiTime = System.currentTimeMillis();
					lastWriteBytes = finalSum;
				}
			}
			fos.flush();
			return file;
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException e) {
				LogUtil.e(e.toString());
				e.printStackTrace();
			}
			try {
				if (fos != null) fos.close();
			} catch (IOException e) {
				LogUtil.e(e.toString());
				e.printStackTrace();
			}
		}
	}
	
}