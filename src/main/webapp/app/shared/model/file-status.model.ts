export interface IFileStatus {
  id?: number;
  name?: string;
  url?: string;
  result?: string;
  status?: number;
  download_result_url?: string;
  fileType?: string;
  versionInfo?: string;
  createdDate?: string;
}

export const defaultValue: Readonly<IFileStatus> = {};
