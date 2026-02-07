package ru.koshelev.jobs21;

import ru.koshelev.jobs21.executor.CollectAllExecutor;
import ru.koshelev.jobs21.executor.FailFastExecutor;
import ru.koshelev.jobs21.job.Job;
import ru.koshelev.jobs21.job.SampleJobs;

import java.util.List;

public class Application {

    public static void main(String[] args) {

        List<Job<String>> jobs = List.of(
                SampleJobs.billing(),
                SampleJobs.fraud(),
                SampleJobs.limits(),
                SampleJobs.kyc()
        );

        System.out.println("=== COLLECT ALL ===");
        new CollectAllExecutor()
                .execute(jobs)
                .forEach(Application::print);

        System.out.println("\n=== FAIL FAST ===");
        try {
            new FailFastExecutor()
                    .execute(jobs)
                    .forEach(Application::print);
        } catch (Exception e) {
            System.out.println("Stopped: " + e.getMessage());
        }
    }

    private static void print(Result<?> result) {
        switch (result) {
            case Result.Success<?> s ->
                    System.out.println("SUCCESS " + s.jobName());
            case Result.Failure<?> f ->
                    System.out.println("FAILURE " + f.jobName()
                            + " -> " + f.error().getMessage());
        }
    }
}