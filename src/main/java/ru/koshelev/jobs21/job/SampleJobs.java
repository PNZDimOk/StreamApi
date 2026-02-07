package ru.koshelev.jobs21.job;

import ru.koshelev.jobs21.Result;

import java.util.Random;

public final class SampleJobs {

    private static final Random RANDOM = new Random();

    public static Job<String> billing() {
        return () -> simulate("Billing");
    }

    public static Job<String> fraud() {
        return () -> simulate("Fraud");
    }

    public static Job<String> limits() {
        return () -> simulate("Limits");
    }

    public static Job<String> kyc() {
        return () -> simulate("KYC");
    }

    private static Result<String> simulate(String name) {
        try {
            Thread.sleep(300 + RANDOM.nextInt(500));

            if (RANDOM.nextBoolean()) {
                throw new IllegalStateException(name + " failed");
            }

            return new Result.Success<>(name, "OK");
        } catch (Exception e) {
            return new Result.Failure<>(name, e);
        }
    }

    private SampleJobs() {}
}