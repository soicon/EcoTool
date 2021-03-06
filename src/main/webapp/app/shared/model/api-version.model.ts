export interface IApiVersion {
  id?: number;
  version?: string;
  description?: string;
}

export const defaultValue: Readonly<IApiVersion> = {};
