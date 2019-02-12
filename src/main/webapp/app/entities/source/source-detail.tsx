import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import { IRootState } from 'app/shared/reducers';
import { getQA } from './source.reducer';
import './react-tabs.css';
// tslint:disable-next-line:no-unused-variable

export interface ISourceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class SourceDetail extends React.Component<ISourceDetailProps> {
  constructor(props, prevState) {
    super(props, prevState);

    this.state = {
      ...prevState,
      key: 0
    };
  }

  componentDidMount() {
    this.props.getQA(this.props.match.params.id);
  }

  handleSelect = key => {
    this.setState({ key });
  };

  render() {
    const { sourceEntity } = this.props;
    return (
      <Tabs defaultActiveKey={0} onSelect={this.handleSelect} id="controlled-tab-example">
        <TabList>
          {Array.from(Array(sourceEntity.length)).map((key, index) => (
            <Tab eventKey={index} key={key}>
              Q&A
              {index}
            </Tab>
          ))}
        </TabList>
        {Array.from(Array(sourceEntity.length)).map((key, index) => (
          <TabPanel key={key}>
            <p>
              <strong>Đề bài</strong>
            </p>
            <img src={sourceEntity[index][1]} />
            <p>
              <strong>Câu hỏi</strong>
            </p>
            <div dangerouslySetInnerHTML={{ __html: sourceEntity[index][2] }} />
            <p>
              <strong>Đáp án</strong>
            </p>

            <div dangerouslySetInnerHTML={{ __html: sourceEntity[index][3] }} />
            <p>
              Developed By:{' '}
              <a href="https://facebook.com/dung.dmr/" target="_blank">
                Nguyễn Đình Dũng
              </a>
            </p>
          </TabPanel>
        ))}
      </Tabs>
    );
  }
}

const mapStateToProps = ({ source }: IRootState) => ({
  sourceEntity: source.entities,
  key: 0
});

const mapDispatchToProps = { getQA };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SourceDetail);
