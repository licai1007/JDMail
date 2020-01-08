package com.m520it.jdmail.util;
import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 异常信息的记录
 * @author llf
 * 1.闪退    2.收集用户的错误
 */
public class ErrorReport implements UncaughtExceptionHandler {
	private Context mContext;
	private static ErrorReport mErrorReport;
	private UncaughtExceptionHandler mDefaultHandler;
	private ErrorReport() {
	}

	public static synchronized ErrorReport getInstance() {
		if (mErrorReport == null) {
			mErrorReport = new ErrorReport();
		}
		return mErrorReport;
	}
	
	public void init(Context context) {
		this.mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
		Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
//				AllActivityManager.getInstance().exitPrograms();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			}
		}
		
	}


	private boolean handleException(Throwable ex) {
		if (ex == null){
			return false;
		}
		new Thread() {
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "抱歉,系统异常", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		saveCrashInfo(ex);
		return true;
	}

	private void saveCrashInfo(Throwable ex) {
		File path = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			path = mContext.getFilesDir();
			String fileName = "/" + getTime(System.currentTimeMillis())
					+ ".txt";
			try {
				if (!path.exists()) {
					path.mkdirs();
				}
				Writer writer = new StringWriter();
				PrintWriter printWriter = new PrintWriter(writer);
				ex.printStackTrace(printWriter);
				Throwable cause = ex.getCause();
				while (cause != null) {
					cause.printStackTrace(printWriter);
					cause = cause.getCause();
				}
				printWriter.close();
				FileOutputStream mFileOutputStream = new FileOutputStream(path
						+ fileName);
				mFileOutputStream.write(writer.toString().getBytes());
				mFileOutputStream.close();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}
	
	private String getTime(long time) {
		String str = "";
		Date date = new Date(time);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		str = sf.format(date);
		return str;
	}
}
