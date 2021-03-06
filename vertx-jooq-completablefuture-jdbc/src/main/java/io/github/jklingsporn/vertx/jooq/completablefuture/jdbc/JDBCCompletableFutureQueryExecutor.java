package io.github.jklingsporn.vertx.jooq.completablefuture.jdbc;

import io.github.jklingsporn.vertx.jooq.shared.internal.QueryExecutor;
import io.vertx.core.Vertx;
import org.jooq.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Created by jensklingsporn on 20.12.17.
 */
public class JDBCCompletableFutureQueryExecutor<R extends UpdatableRecord<R>,P,T> extends JDBCCompletableFutureGenericQueryExecutor implements QueryExecutor<R,T,CompletableFuture<List<P>>,CompletableFuture<P>,CompletableFuture<Integer>,CompletableFuture<T>> {

    private final Class<P> daoType;

    public JDBCCompletableFutureQueryExecutor(Class<P> daoType, Configuration configuration, Vertx vertx) {
        super(configuration,vertx);
        this.daoType = daoType;
    }


    @Override
    public CompletableFuture<List<P>> findMany(ResultQuery<R> query) {
        return executeBlocking(h -> h.complete(query.fetchInto(daoType)));
    }

    @Override
    public CompletableFuture<P> findOne(ResultQuery<R> query) {
        return executeBlocking(h -> h.complete(query.fetchOneInto(daoType)));
    }

    @Override
    public CompletableFuture<Integer> execute(Query query) {
        return executeBlocking(h -> h.complete(query.execute()));
    }

    @Override
    public CompletableFuture<T> insertReturning(InsertResultStep<R> query,Function<Object,T> keyMapper) {
        return executeBlocking(h -> h.complete(keyMapper.apply(query.fetchOne())));
    }

}
