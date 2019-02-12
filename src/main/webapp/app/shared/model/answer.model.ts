export interface IAnswer {
  id?: number;
  status?: number;
  answer_text?: string;
  reviewer_id?: number;
  user_id?: number;
  questionId?: number;
}

export const defaultValue: Readonly<IAnswer> = {};
