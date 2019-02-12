export interface IRunnerLog {
  id?: number;
  apiversionVersion?: string;
  apiversionId?: number;
  dataversionVersion?: string;
  dataversionId?: number;
  inputversionVersion?: string;
  inputversionId?: number;
  sourcePath?: string;
  sourceId?: number;
  questionQuestion_text?: string;
  questionId?: number;
  answerAnswer_text?: string;
  answerId?: number;
}

export const defaultValue: Readonly<IRunnerLog> = {};
