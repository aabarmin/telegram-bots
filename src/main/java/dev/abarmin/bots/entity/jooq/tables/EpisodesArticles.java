/*
 * This file is generated by jOOQ.
 */
package dev.abarmin.bots.entity.jooq.tables;


import dev.abarmin.bots.entity.jooq.Keys;
import dev.abarmin.bots.entity.jooq.Public;
import dev.abarmin.bots.entity.jooq.tables.Articles.ArticlesPath;
import dev.abarmin.bots.entity.jooq.tables.Episodes.EpisodesPath;
import dev.abarmin.bots.entity.jooq.tables.records.EpisodesArticlesRecord;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpisodesArticles extends TableImpl<EpisodesArticlesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>PUBLIC.EPISODES_ARTICLES</code>
     */
    public static final EpisodesArticles EPISODES_ARTICLES = new EpisodesArticles();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpisodesArticlesRecord> getRecordType() {
        return EpisodesArticlesRecord.class;
    }

    /**
     * The column <code>PUBLIC.EPISODES_ARTICLES.ID</code>.
     */
    public final TableField<EpisodesArticlesRecord, Integer> ID = createField(DSL.name("ID"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>PUBLIC.EPISODES_ARTICLES.EPISODE_ID</code>.
     */
    public final TableField<EpisodesArticlesRecord, Integer> EPISODE_ID = createField(DSL.name("EPISODE_ID"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>PUBLIC.EPISODES_ARTICLES.ARTICLE_ID</code>.
     */
    public final TableField<EpisodesArticlesRecord, Integer> ARTICLE_ID = createField(DSL.name("ARTICLE_ID"), SQLDataType.INTEGER, this, "");

    private EpisodesArticles(Name alias, Table<EpisodesArticlesRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private EpisodesArticles(Name alias, Table<EpisodesArticlesRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>PUBLIC.EPISODES_ARTICLES</code> table reference
     */
    public EpisodesArticles(String alias) {
        this(DSL.name(alias), EPISODES_ARTICLES);
    }

    /**
     * Create an aliased <code>PUBLIC.EPISODES_ARTICLES</code> table reference
     */
    public EpisodesArticles(Name alias) {
        this(alias, EPISODES_ARTICLES);
    }

    /**
     * Create a <code>PUBLIC.EPISODES_ARTICLES</code> table reference
     */
    public EpisodesArticles() {
        this(DSL.name("EPISODES_ARTICLES"), null);
    }

    public <O extends Record> EpisodesArticles(Table<O> path, ForeignKey<O, EpisodesArticlesRecord> childPath, InverseForeignKey<O, EpisodesArticlesRecord> parentPath) {
        super(path, childPath, parentPath, EPISODES_ARTICLES);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class EpisodesArticlesPath extends EpisodesArticles implements Path<EpisodesArticlesRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> EpisodesArticlesPath(Table<O> path, ForeignKey<O, EpisodesArticlesRecord> childPath, InverseForeignKey<O, EpisodesArticlesRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private EpisodesArticlesPath(Name alias, Table<EpisodesArticlesRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public EpisodesArticlesPath as(String alias) {
            return new EpisodesArticlesPath(DSL.name(alias), this);
        }

        @Override
        public EpisodesArticlesPath as(Name alias) {
            return new EpisodesArticlesPath(alias, this);
        }

        @Override
        public EpisodesArticlesPath as(Table<?> alias) {
            return new EpisodesArticlesPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<EpisodesArticlesRecord, Integer> getIdentity() {
        return (Identity<EpisodesArticlesRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<EpisodesArticlesRecord> getPrimaryKey() {
        return Keys.EPISODES_ARTICLES_PK;
    }

    @Override
    public List<ForeignKey<EpisodesArticlesRecord, ?>> getReferences() {
        return Arrays.asList(Keys.EPISODES_ARTICLES_EPISODE_ID_FK, Keys.EPISODES_ARTICLES_ARTICLE_ID_FK);
    }

    private transient EpisodesPath _episodes;

    /**
     * Get the implicit join path to the <code>PUBLIC.EPISODES</code> table.
     */
    public EpisodesPath episodes() {
        if (_episodes == null)
            _episodes = new EpisodesPath(this, Keys.EPISODES_ARTICLES_EPISODE_ID_FK, null);

        return _episodes;
    }

    private transient ArticlesPath _articles;

    /**
     * Get the implicit join path to the <code>PUBLIC.ARTICLES</code> table.
     */
    public ArticlesPath articles() {
        if (_articles == null)
            _articles = new ArticlesPath(this, Keys.EPISODES_ARTICLES_ARTICLE_ID_FK, null);

        return _articles;
    }

    @Override
    public EpisodesArticles as(String alias) {
        return new EpisodesArticles(DSL.name(alias), this);
    }

    @Override
    public EpisodesArticles as(Name alias) {
        return new EpisodesArticles(alias, this);
    }

    @Override
    public EpisodesArticles as(Table<?> alias) {
        return new EpisodesArticles(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpisodesArticles rename(String name) {
        return new EpisodesArticles(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public EpisodesArticles rename(Name name) {
        return new EpisodesArticles(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public EpisodesArticles rename(Table<?> name) {
        return new EpisodesArticles(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public EpisodesArticles where(Condition condition) {
        return new EpisodesArticles(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public EpisodesArticles where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public EpisodesArticles where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public EpisodesArticles where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public EpisodesArticles where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public EpisodesArticles where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public EpisodesArticles where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public EpisodesArticles where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public EpisodesArticles whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public EpisodesArticles whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
