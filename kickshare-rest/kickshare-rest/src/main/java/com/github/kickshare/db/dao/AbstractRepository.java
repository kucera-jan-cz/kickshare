package com.github.kickshare.db.dao;

import static org.jooq.impl.DSL.using;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.DAO;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertQuery;
import org.jooq.RecordMapper;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;

/**
 * @author Jan.Kucera
 * @since 6.4.2017
 */
public abstract class AbstractRepository<R extends UpdatableRecord<R>, P, T> implements EnhancedDAO<R, P, T> {
    protected DAO<R, P, T> dao;
    protected DSLContext dsl;
    protected Table<R> table;

    public AbstractRepository(DAO<R, P, T> dao) {
        this.dao = dao;
        this.dsl = using(this.configuration());
        this.table = this.getTable();
    }

    @Override
    public T createReturningKey(P entity) {
        InsertQuery<R> insert = dsl.insertQuery(table);
        insert.addRecord(dsl.newRecord(table, entity));
        insert.setReturning(table.getIdentity().getField());
        insert.execute();
        return (T) insert.getReturnedRecord().get(table.getIdentity().getField());
    }

    @Override
    public R createReturning(P entity) {
        InsertQuery<R> insert = dsl.insertQuery(table);
        insert.addRecord(dsl.newRecord(table, entity));
        insert.setReturning();
        insert.execute();
        return insert.getReturnedRecord();
    }

    @Override
    public Configuration configuration() {
        return dao.configuration();
    }

    @Override
    public Settings settings() {
        return dao.settings();
    }

    @Override
    public SQLDialect dialect() {
        return dao.dialect();
    }

    @Override
    public SQLDialect family() {
        return dao.family();
    }

    @Override
    public RecordMapper mapper() {
        return dao.mapper();
    }

    public void insert(final P object) throws DataAccessException {
        dao.insert(object);
    }

    public void insert(final P[] objects) throws DataAccessException {
        dao.insert(objects);
    }

    public void insert(final Collection objects) throws DataAccessException {
        dao.insert(objects);
    }

    public void update(final P object) throws DataAccessException {
        dao.update(object);
    }

    public void update(final P[] objects) throws DataAccessException {
        dao.update(objects);
    }

    public void update(final Collection objects) throws DataAccessException {
        dao.update(objects);
    }

    public void delete(final P object) throws DataAccessException {
        dao.delete(object);
    }

    public void delete(final P[] objects) throws DataAccessException {
        dao.delete(objects);
    }

    public void delete(final Collection objects) throws DataAccessException {
        dao.delete(objects);
    }

    public void deleteById(final T[] ids) throws DataAccessException {
        dao.deleteById(ids);
    }

    public void deleteById(final Collection ids) throws DataAccessException {
        dao.deleteById(ids);
    }

    public boolean exists(final P object) throws DataAccessException {
        return dao.exists(object);
    }

    public boolean existsById(final T id) throws DataAccessException {
        return dao.existsById(id);
    }

    @Override
    public long count() throws DataAccessException {
        return dao.count();
    }

    @Override
    public List<P> findAll() throws DataAccessException {
        return dao.findAll();
    }

    public P findById(final T id) throws DataAccessException {
        return dao.findById(id);
    }

    public <Z> List<P> fetch(final Field<Z> field, final Z... values) throws DataAccessException {
        return dao.fetch(field, values);
    }

    public <Z> P fetchOne(Field<Z> field, Z value) throws DataAccessException {
        return dao.fetchOne(field, value);
    }

    public <Z> Optional<P> fetchOptional(Field<Z> field, Z value) throws DataAccessException {
        return dao.fetchOptional(field, value);
    }

    @Override
    public Table<R> getTable() {
        return dao.getTable();
    }

    @Override
    public Class<P> getType() {
        return dao.getType();
    }
}
