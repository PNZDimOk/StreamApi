package ru.koshelev.jobs21;

public sealed interface Result<T>
        permits Result.Success, Result.Failure {

    record Success<T>(String jobName, T value) implements Result<T> {}

    record Failure<T>(String jobName, Throwable error) implements Result<T> {}

    default boolean isSuccess() {
        return this instanceof Success<?>;
    }
}
