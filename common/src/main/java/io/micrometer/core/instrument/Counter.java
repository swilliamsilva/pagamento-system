package io.micrometer.core.instrument;

public class Counter {
    private final String name;
    private double count;

    private Counter(String name) {
        this.name = name;
    }

    public interface Builder {
        Builder description(String description);
        Builder tag(String key, String value);
        Counter register(MeterRegistry registry);
    }

    public static Builder builder(String name) {
        return new Builder() {
            private String description;
            
            @Override
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            @Override
            public Builder tag(String key, String value) {
                return this;
            }

            @Override
            public Counter register(MeterRegistry registry) {
                return new Counter(name);
            }
        };
    }

    public void increment() {
        count++;
    }

    public double count() {
        return count;
    }
}