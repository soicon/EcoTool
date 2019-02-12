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
 * Criteria class for the AppVersion entity. This class is used in AppVersionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /app-versions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AppVersionCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter apiVer;

    private StringFilter dataVer;

    private StringFilter inputVer;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getApiVer() {
        return apiVer;
    }

    public void setApiVer(StringFilter apiVer) {
        this.apiVer = apiVer;
    }

    public StringFilter getDataVer() {
        return dataVer;
    }

    public void setDataVer(StringFilter dataVer) {
        this.dataVer = dataVer;
    }

    public StringFilter getInputVer() {
        return inputVer;
    }

    public void setInputVer(StringFilter inputVer) {
        this.inputVer = inputVer;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AppVersionCriteria that = (AppVersionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(apiVer, that.apiVer) &&
            Objects.equals(dataVer, that.dataVer) &&
            Objects.equals(inputVer, that.inputVer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        apiVer,
        dataVer,
        inputVer
        );
    }

    @Override
    public String toString() {
        return "AppVersionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (apiVer != null ? "apiVer=" + apiVer + ", " : "") +
                (dataVer != null ? "dataVer=" + dataVer + ", " : "") +
                (inputVer != null ? "inputVer=" + inputVer + ", " : "") +
            "}";
    }

}
