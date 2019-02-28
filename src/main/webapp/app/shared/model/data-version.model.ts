export interface IDataVersion {
  id?: number;
  version?: string;
  description?: string;
  versionInfo?: string;
  status?: number;
}

export const defaultValue: Readonly<IDataVersion> = {};
