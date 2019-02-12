export interface IFileStatus {
  id?: number;
  name?: string;
  url?: string;
  result?: string;
  status?: number;
  download_result_url?: string;
  fileType?: string;
}

export const defaultValue: Readonly<IFileStatus> = {};
