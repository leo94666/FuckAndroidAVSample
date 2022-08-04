package com.top.ffmpeg.decoder;

import com.top.arch.utils.ThreadUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author leo
 * @version 1.0
 * @className LeoThread
 * @description TODO
 * @date 2022/5/17 15:48
 **/
public class LeoThread implements Callable {
    @Override
    public Object call() throws Exception {


        return null;
    }


    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                5,
                10, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(10),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return null;
                    }
                }, new ThreadPoolExecutor.AbortPolicy()
        );

        executor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}



