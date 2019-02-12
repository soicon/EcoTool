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
 * Criteria class for the FileStatus entity. This class is used in FileStatusResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /file-statuses?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FileStatusCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter url;

    private StringFilter result;

    private IntegerFilter status;

    private StringFilter download_result_url;

    private StringFilter fileType;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getResult() {
        return result;
    }

    public void setResult(StringFilter result) {
        this.result = result;
    }

    public IntegerFilter getStatus() {
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public StringFilter getDownload_result_url() {
        return download_result_url;
    }

    public void setDownload_result_url(StringFilter download_result_url) {
        this.download_result_url = download_result_url;
    }

    public StringFilter getFileType() {
        return fileType;
    }

    public void setFileType(StringFilter fileType) {
        this.fileType = fileType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FileStatusCriteria that = (FileStatusCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(url, that.url) &&
            Objects.equals(result, that.result) &&
            Objects.equals(status, that.status) &&
            Objects.equals(download_result_url, that.download_result_url) &&
            Objects.equals(fileType, that.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        url,
        result,
        status,
        download_result_url,
        fileType
        );
    }

    @Override
    public String toString() {
        return "FileStatusCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (result != null ? "result=" + result + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (download_result_url != null ? "download_result_url=" + download_result_url + ", " : "") +
                (fileType != null ? "fileType=" + fileType + ", " : "") +
            "}";
    }

}
