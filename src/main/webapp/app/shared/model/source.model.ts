import { IQuestion } from 'app/shared/model//question.model';

export interface ISource {
  id?: number;
  path?: string;
  device_id?: string;
  type?: number;
  status?: number;
  need_re_answer?: number;
  question_id?: number;
  questions?: IQuestion[];
}

export const defaultValue: Readonly<ISource> = {};
