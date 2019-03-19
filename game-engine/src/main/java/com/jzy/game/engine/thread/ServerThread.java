package com.jzy.game.engine.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.LoggerFactory;

import com.jzy.game.engine.mail.MailConfig;
import com.jzy.game.engine.mail.MailManager;
import com.jzy.game.engine.thread.queue.QueueThreadManager;
import com.jzy.game.engine.thread.timer.TimerEvent;
import com.jzy.game.engine.thread.timer.TimerThread;

import org.slf4j.Logger;

/**
 * 服务器线程
 * <p>
 * 两类线程模型:<br>
 * 1.为逻辑或接受到的消息预先分配一个线程，所有逻辑放在线程队列中依次执行；{@link ServerThread}<br>
 * 2.为逻辑或消息分配一个队列，再由队列分配线程，依次执行 {@link QueueThreadManager}
 * 
 * </p>
 *
 * @author CruiseDing
 * @date 2017-03-30 QQ:359135103
 */
public class ServerThread extends Thread implements Executor {

	private static final Logger log = LoggerFactory.getLogger(ServerThread.class);

	// 线程名称
	protected final String threadName;
	// 线程心跳间隔
	protected final long heart;
	// 线程处理命令队列
	protected LinkedBlockingQueue<Runnable> command_queue = new LinkedBlockingQueue<>();
	// 是否暂停
	protected boolean stop;

	// 最后一次执行任务的时间
	protected long lastExecuteTime;

	protected TimerThread timer;

	/**
	 * 如果没有定时任务，心跳设为0 ，大于零会启动一个定时检查线程（比较浪费）
	 * @param group
	 * @param threadName
	 * @param heart 心跳
	 * @param commandCount
	 * @param classLogNames
	 */
	@SafeVarargs
	public ServerThread(ThreadGroup group, String threadName, long heart, int commandCount,
			Class<? extends TimerEvent>... classLogNames) {
		super(group, threadName);
		this.threadName = threadName;
		this.heart = heart;

		if (this.heart > 0) {
			timer = new TimerThread(this, classLogNames);
		}
		setUncaughtExceptionHandler((Thread t, Throwable e) -> {
			log.error("ServerThread.setUncaughtExceptionHandler", e);
			MailConfig mailConfig = MailManager.getInstance().getMailConfig();
			MailManager.getInstance().sendTextMailAsync("线程异常", "线程" + threadName,
					mailConfig.getReciveUser().toArray(new String[mailConfig.getReciveUser().size()]));
			if (timer != null) {
				timer.stop(true);
			}
			command_queue.clear();
		});
		command_queue = new LinkedBlockingQueue<>(commandCount);
	}

	public void showStackTrace() {
		StringBuilder buf = new StringBuilder();
		long now = System.currentTimeMillis();
		long procc = now - getLastExecuteTime();
		buf.append("线程[" + getName() + "]可能已卡死1!!!" + procc + "ms，执行任务：" + getCommand().getClass().getName());
		try {
			StackTraceElement[] elements = getStackTrace();
			for (int i = 0; i < elements.length; i++) {
				buf.append("\n    " + elements[i].getClassName() + "." + elements[i].getMethodName() + "("
						+ elements[i].getFileName() + ":" + elements[i].getLineNumber() + ")");
			}
		} catch (Exception e) {
			buf.append(e);
		}
		log.warn(buf.toString());
	}

	/**
	 * 当前执行的线程
	 */
	protected Runnable command;

	public Runnable getCommand() {
		return command;
	}

	@Override
	public void run() {
		if (heart > 0 && timer != null) {
			timer.start();
		}
		stop = false;
		int loop = 0;
		while (!stop && !isInterrupted()) {
			command = command_queue.poll();
			if (command == null) {
				try {
					synchronized (this) {
						loop = 0;
						lastExecuteTime = 0;
						wait();
					}
				} catch (InterruptedException e) {
					log.error("ServerThread.run 1 ", e);
				}
			} else {
				try {
					loop++;
					lastExecuteTime = System.currentTimeMillis();
					command.run();
					long cost = System.currentTimeMillis() - lastExecuteTime;
					if (cost > 30L) {
						log.warn("线程：{} 执行 {} 消耗时间过长 {}毫秒,当前命令数 {} 条", threadName, command.getClass().getName(), cost,
								command_queue.size());
					}
					if (loop > 300) {
						loop = 0;
						log.warn("线程：{} 已循环执行{} 次,当前命令数{}", threadName, loop, command_queue.size());
					}
				} catch (Exception e) {
					log.error("ServerThread[" + threadName + "]执行任务错误 ", e);
				}
			}
		}
	}

	public void stop(boolean flag) {
		stop = flag;
		log.warn("线程{}停止", threadName);
		if (timer != null) {
			timer.stop(flag);
		}
		command_queue.clear();
		try {
			synchronized (this) {
				notify();
				interrupt();
			}
		} catch (Exception e) {
			log.error("Main Thread " + threadName + " Notify Exception:" + e.getMessage());
		}
	}

	public void execute(Runnable command, boolean checkOnly) {
		try {
			if (checkOnly && command_queue.contains(command)) {
				return;
			}
			command_queue.add(command);
			synchronized (this) {
				notify();
			}
		} catch (Exception e) {
			log.error("Main Thread " + threadName + " Notify Exception:" + e.getMessage());
		}
	}

	/**
	 * 执行IHandler. ReqHandler/ResHandler
	 *
	 * @param command
	 */
	@Override
	public void execute(Runnable command) {
		try {
			if (command_queue.contains(command)) {
				return;
			}
			command_queue.add(command);
			synchronized (this) {
				notify();
			}
		} catch (Exception e) {
			log.error("Main Thread " + threadName + " Notify Exception:" + e.getMessage());
		}
	}

	public boolean contains(Runnable runnable) {
		return command_queue.contains(runnable);
	}

	public TimerThread getTimer() {
		return timer;
	}

	public void addTimerEvent(TimerEvent event) {
		if (timer != null) {
			timer.addTimerEvent(event);
		}
	}

	public void removeTimerEvent(TimerEvent event) {
		if (timer != null) {
			timer.removeTimerEvent(event);
		}
	}

	public String getThreadName() {
		return threadName;
	}

	public long getHeart() {
		return heart;
	}

	public long getLastExecuteTime() {
		return lastExecuteTime;
	}

	public LinkedBlockingQueue<Runnable> getCommands() {
		return command_queue;
	}
}
