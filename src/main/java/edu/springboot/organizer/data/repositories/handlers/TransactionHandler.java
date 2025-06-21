package edu.springboot.organizer.data.repositories.handlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class TransactionHandler {

    @Transactional(propagation = Propagation.REQUIRED)
    public <T> T runInTransactionSupplier(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T runInNewTransactionSupplier(Supplier<T> supplier) {
        return supplier.get();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public <T, R> R runInTransactionFunction(Function<T, R> supplier, T t) {
        return supplier.apply(t);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T, R> R runInNewTransactionFunction(Function<T, R> supplier, T t) {
        return supplier.apply(t);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public <T, U, R> R runInTransactionBiFunction(BiFunction<T, U, R> supplier, T t, U u) {
        return supplier.apply(t, u);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T, U, R> R runInNewTransactionBiFunction(BiFunction<T, U, R> supplier, T t, U u) {
        return supplier.apply(t, u);
    }

}
