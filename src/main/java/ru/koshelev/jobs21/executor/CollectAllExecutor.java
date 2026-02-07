package ru.koshelev.jobs21.executor;

import ru.koshelev.jobs21.Result;
import ru.koshelev.jobs21.job.Job;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CollectAllExecutor {

    public <T> List<Result<T>> execute(List<Job<T>> jobs) {

        try (ExecutorService executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<Result<T>>> futures = jobs.stream()
                    .map(job -> executor.submit(job::execute))
                    .toList();

            return futures.stream()
                    .map(this::getSafely)
                    .toList();
        }
    }

    private <T> Result<T> getSafely(Future<Result<T>> future) {
        try {
            return future.get();
        } catch (Exception e) {
            return new Result.Failure<>("unknown", e);
        }
    }
}
