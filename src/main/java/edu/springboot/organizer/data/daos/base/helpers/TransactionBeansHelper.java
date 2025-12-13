package edu.springboot.organizer.data.daos.base.helpers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Type parameters:
 * <T> – the type of the first argument to the function
 * <U> – the type of the second argument to the function
 * <R> – the type of the result of the function
 */
@Component
public class TransactionBeansHelper {

    @Transactional(propagation = Propagation.REQUIRED)
    public <T> T runInTransactionSupplier(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T runInNewTransactionSupplier(Supplier<T> supplier) {
        return supplier.get();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public <T, R> R runInTransactionFunction(Function<T, R> supplier, T firstArgument) {
        return supplier.apply(firstArgument);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T, R> R runInNewTransactionFunction(Function<T, R> supplier, T firstArgument) {
        return supplier.apply(firstArgument);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public <T, U, R> R runInTransactionBiFunction(BiFunction<T, U, R> supplier, T firstArgument, U secondArgument) {
        return supplier.apply(firstArgument, secondArgument);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T, U, R> R runInNewTransactionBiFunction(BiFunction<T, U, R> supplier, T firstArgument, U secondArgument) {
        return supplier.apply(firstArgument, secondArgument);
    }

}
