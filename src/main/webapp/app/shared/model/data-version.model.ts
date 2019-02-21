export interface IDataVersion {
  id?: number;
  version?: string;
  description?: string;
  versionInfo?: string;
}

export const defaultValue: Readonly<IDataVersion> = {};
