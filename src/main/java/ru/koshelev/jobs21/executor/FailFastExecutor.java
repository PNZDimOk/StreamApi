package ru.koshelev.jobs21.executor;

import ru.koshelev.jobs21.Result;
import ru.koshelev.jobs21.job.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FailFastExecutor {

    public <T> List<Result<T>> execute(List<Job<T>> jobs) {

        try (ExecutorService executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<Result<T>>> futures = new ArrayList<>();

            for (Job<T> job : jobs) {
                futures.add(executor.submit(job::execute));
            }

            List<Result<T>> results = new ArrayList<>();

            for (Future<Result<T>> future : futures) {
                Result<T> result = future.get();

                if (!result.isSuccess()) {
                    futures.forEach(f -> f.cancel(true));
                    throw new RuntimeException("Fail-fast on " +
                            ((Result.Failure<?>) result).jobName());
                }

                results.add(result);
            }

            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}