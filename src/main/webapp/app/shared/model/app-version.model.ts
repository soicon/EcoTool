export interface IAppVersion {
  id?: number;
  apiVer?: string;
  dataVer?: string;
  inputVer?: string;
}

export const defaultValue: Readonly<IAppVersion> = {};
