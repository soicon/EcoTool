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
 * Criteria class for the FileConfig entity. This class is used in FileConfigResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /file-configs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FileConfigCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter fileStatusId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getFileStatusId() {
        return fileStatusId;
    }

    public void setFileStatusId(LongFilter fileStatusId) {
        this.fileStatusId = fileStatusId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FileConfigCriteria that = (FileConfigCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fileStatusId, that.fileStatusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fileStatusId
        );
    }

    @Override
    public String toString() {
        return "FileConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fileStatusId != null ? "fileStatusId=" + fileStatusId + ", " : "") +
            "}";
    }

}
