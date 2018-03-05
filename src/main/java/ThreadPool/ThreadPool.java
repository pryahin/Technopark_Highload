package ThreadPool;

import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool implements Executor {
    private int currentCount = 0;
    private int countThread = 0;
    private final Queue<Runnable> queueTask = new LinkedBlockingQueue<>();

    public ThreadPool(int countThread) {
        this.countThread = countThread;
    }

    private void createThread() {
        new Thread(new TaskWorker()).start();
    }

    @Override
    public void execute(@NotNull Runnable command) {
        synchronized (this) {
            if (currentCount < countThread) {
                createThread();
                currentCount++;
            }
            if (!queueTask.offer(command)) {
                System.out.println("Task can not add in Queue");
            }
        }
    }

    private final class TaskWorker implements Runnable {
        @Override
        public void run() {
            while (true) {
                final Runnable nextTask;
                synchronized (this) {
                    nextTask = queueTask.poll();
                }

                if (nextTask != null) {
                    nextTask.run();
                } else {
                    synchronized (this) {
                        currentCount--;
                    }
                    return;
                }
            }
        }
    }
}