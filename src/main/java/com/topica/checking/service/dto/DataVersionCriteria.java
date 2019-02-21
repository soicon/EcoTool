package com.topica.checking.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the DataVersion entity. This class is used in DataVersionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /data-versions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DataVersionCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter version;

    private StringFilter description;

    private StringFilter versionInfo;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getVersion() {
        return version;
    }

    public void setVersion(StringFilter version) {
        this.version = version;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(StringFilter versionInfo) {
        this.versionInfo = versionInfo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DataVersionCriteria that = (DataVersionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(version, that.version) &&
            Objects.equals(description, that.description) &&
            Objects.equals(versionInfo, that.versionInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        version,
        description,
        versionInfo
        );
    }

    @Override
    public String toString() {
        return "DataVersionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (version != null ? "version=" + version + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (versionInfo != null ? "versionInfo=" + versionInfo + ", " : "") +
            "}";
    }

}
