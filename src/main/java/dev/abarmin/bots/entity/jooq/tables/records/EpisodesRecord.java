/*
 * This file is generated by jOOQ.
 */
package dev.abarmin.bots.entity.jooq.tables.records;


import dev.abarmin.bots.entity.jooq.tables.Episodes;

import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpisodesRecord extends UpdatableRecordImpl<EpisodesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>PUBLIC.EPISODES.EPISODE_ID</code>.
     */
    public void setEpisodeId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>PUBLIC.EPISODES.EPISODE_ID</code>.
     */
    public Integer getEpisodeId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>PUBLIC.EPISODES.EPISODE_NAME</code>.
     */
    public void setEpisodeName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>PUBLIC.EPISODES.EPISODE_NAME</code>.
     */
    public String getEpisodeName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>PUBLIC.EPISODES.EPISODE_STATUS</code>.
     */
    public void setEpisodeStatus(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>PUBLIC.EPISODES.EPISODE_STATUS</code>.
     */
    public String getEpisodeStatus() {
        return (String) get(2);
    }

    /**
     * Setter for <code>PUBLIC.EPISODES.CREATED_AT</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>PUBLIC.EPISODES.CREATED_AT</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>PUBLIC.EPISODES.UPDATED_AT</code>.
     */
    public void setUpdatedAt(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>PUBLIC.EPISODES.UPDATED_AT</code>.
     */
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EpisodesRecord
     */
    public EpisodesRecord() {
        super(Episodes.EPISODES);
    }

    /**
     * Create a detached, initialised EpisodesRecord
     */
    public EpisodesRecord(Integer episodeId, String episodeName, String episodeStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(Episodes.EPISODES);

        setEpisodeId(episodeId);
        setEpisodeName(episodeName);
        setEpisodeStatus(episodeStatus);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        resetChangedOnNotNull();
    }
}
