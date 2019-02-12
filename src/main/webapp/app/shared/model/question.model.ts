export interface IQuestion {
  id?: number;
  question_text?: string;
  visible?: number;
  sourceId?: number;
  answerId?: number;
}

export const defaultValue: Readonly<IQuestion> = {};
