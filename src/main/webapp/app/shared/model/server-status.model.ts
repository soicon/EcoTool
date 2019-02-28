export interface IServerStatus {
  id?: number;
  address?: string;
  status?: number;
  description?: string;
}

export const defaultValue: Readonly<IServerStatus> = {};
