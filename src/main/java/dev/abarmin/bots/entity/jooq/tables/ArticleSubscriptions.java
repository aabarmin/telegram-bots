/*
 * This file is generated by jOOQ.
 */
package dev.abarmin.bots.entity.jooq.tables;


import dev.abarmin.bots.entity.jooq.Keys;
import dev.abarmin.bots.entity.jooq.Public;
import dev.abarmin.bots.entity.jooq.tables.ArticleSources.ArticleSourcesPath;
import dev.abarmin.bots.entity.jooq.tables.TelegramChats.TelegramChatsPath;
import dev.abarmin.bots.entity.jooq.tables.records.ArticleSubscriptionsRecord;

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
public class ArticleSubscriptions extends TableImpl<ArticleSubscriptionsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>PUBLIC.ARTICLE_SUBSCRIPTIONS</code>
     */
    public static final ArticleSubscriptions ARTICLE_SUBSCRIPTIONS = new ArticleSubscriptions();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ArticleSubscriptionsRecord> getRecordType() {
        return ArticleSubscriptionsRecord.class;
    }

    /**
     * The column <code>PUBLIC.ARTICLE_SUBSCRIPTIONS.ID</code>.
     */
    public final TableField<ArticleSubscriptionsRecord, Integer> ID = createField(DSL.name("ID"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>PUBLIC.ARTICLE_SUBSCRIPTIONS.CHAT_ID</code>.
     */
    public final TableField<ArticleSubscriptionsRecord, Long> CHAT_ID = createField(DSL.name("CHAT_ID"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ARTICLE_SUBSCRIPTIONS.ARTICLE_SOURCE_ID</code>.
     */
    public final TableField<ArticleSubscriptionsRecord, Integer> ARTICLE_SOURCE_ID = createField(DSL.name("ARTICLE_SOURCE_ID"), SQLDataType.INTEGER.nullable(false), this, "");

    private ArticleSubscriptions(Name alias, Table<ArticleSubscriptionsRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private ArticleSubscriptions(Name alias, Table<ArticleSubscriptionsRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>PUBLIC.ARTICLE_SUBSCRIPTIONS</code> table
     * reference
     */
    public ArticleSubscriptions(String alias) {
        this(DSL.name(alias), ARTICLE_SUBSCRIPTIONS);
    }

    /**
     * Create an aliased <code>PUBLIC.ARTICLE_SUBSCRIPTIONS</code> table
     * reference
     */
    public ArticleSubscriptions(Name alias) {
        this(alias, ARTICLE_SUBSCRIPTIONS);
    }

    /**
     * Create a <code>PUBLIC.ARTICLE_SUBSCRIPTIONS</code> table reference
     */
    public ArticleSubscriptions() {
        this(DSL.name("ARTICLE_SUBSCRIPTIONS"), null);
    }

    public <O extends Record> ArticleSubscriptions(Table<O> path, ForeignKey<O, ArticleSubscriptionsRecord> childPath, InverseForeignKey<O, ArticleSubscriptionsRecord> parentPath) {
        super(path, childPath, parentPath, ARTICLE_SUBSCRIPTIONS);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class ArticleSubscriptionsPath extends ArticleSubscriptions implements Path<ArticleSubscriptionsRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> ArticleSubscriptionsPath(Table<O> path, ForeignKey<O, ArticleSubscriptionsRecord> childPath, InverseForeignKey<O, ArticleSubscriptionsRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private ArticleSubscriptionsPath(Name alias, Table<ArticleSubscriptionsRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public ArticleSubscriptionsPath as(String alias) {
            return new ArticleSubscriptionsPath(DSL.name(alias), this);
        }

        @Override
        public ArticleSubscriptionsPath as(Name alias) {
            return new ArticleSubscriptionsPath(alias, this);
        }

        @Override
        public ArticleSubscriptionsPath as(Table<?> alias) {
            return new ArticleSubscriptionsPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<ArticleSubscriptionsRecord, Integer> getIdentity() {
        return (Identity<ArticleSubscriptionsRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<ArticleSubscriptionsRecord> getPrimaryKey() {
        return Keys.ARTICLE_SUBSCRIPTION_PK;
    }

    @Override
    public List<ForeignKey<ArticleSubscriptionsRecord, ?>> getReferences() {
        return Arrays.asList(Keys.ARTICLE_SUBSCRIPTION_CHAT_ID_FK, Keys.ARTICLE_SUBSCRIPTION_SOURCE_ID_FK);
    }

    private transient TelegramChatsPath _telegramChats;

    /**
     * Get the implicit join path to the <code>PUBLIC.TELEGRAM_CHATS</code>
     * table.
     */
    public TelegramChatsPath telegramChats() {
        if (_telegramChats == null)
            _telegramChats = new TelegramChatsPath(this, Keys.ARTICLE_SUBSCRIPTION_CHAT_ID_FK, null);

        return _telegramChats;
    }

    private transient ArticleSourcesPath _articleSources;

    /**
     * Get the implicit join path to the <code>PUBLIC.ARTICLE_SOURCES</code>
     * table.
     */
    public ArticleSourcesPath articleSources() {
        if (_articleSources == null)
            _articleSources = new ArticleSourcesPath(this, Keys.ARTICLE_SUBSCRIPTION_SOURCE_ID_FK, null);

        return _articleSources;
    }

    @Override
    public ArticleSubscriptions as(String alias) {
        return new ArticleSubscriptions(DSL.name(alias), this);
    }

    @Override
    public ArticleSubscriptions as(Name alias) {
        return new ArticleSubscriptions(alias, this);
    }

    @Override
    public ArticleSubscriptions as(Table<?> alias) {
        return new ArticleSubscriptions(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ArticleSubscriptions rename(String name) {
        return new ArticleSubscriptions(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ArticleSubscriptions rename(Name name) {
        return new ArticleSubscriptions(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ArticleSubscriptions rename(Table<?> name) {
        return new ArticleSubscriptions(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ArticleSubscriptions where(Condition condition) {
        return new ArticleSubscriptions(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ArticleSubscriptions where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ArticleSubscriptions where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ArticleSubscriptions where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ArticleSubscriptions where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ArticleSubscriptions where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ArticleSubscriptions where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ArticleSubscriptions where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ArticleSubscriptions whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ArticleSubscriptions whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
