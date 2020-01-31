import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 future-progress-modal\">
    +             <div dku-modal-header=\"\" modal-title=\"Project export\">
    +                 <div className=\"modal-body\">
    +                     <div block-api-error=\"\">
    +                         {!finalResponse && !done ? (
    +                             <div>
    +                                 {futureResponse != null ? (
    +                                     <ProgressBar
    +                                         neutral={percentage}
    +                                         allow-empty=\"true\"
    +                                         className=\"progress-striped\"
    +                                         active=\"true\"
    +                                     />
    +                                 ) : null}
    +                                 {futureResponse == null ? (
    +                                     <ProgressBar
    +                                         error={percentage}
    +                                         allow-empty=\"true\"
    +                                         className=\"progress\"
    +                                         active=\"false\"
    +                                     />
    +                                 ) : null}
    +                                 <div className=\"future-progress-bar\">
    +                                     <div style={`width: ${percentage}%`} />
    +                                 </div>
    +                                 <p dangerouslySetInnerHTML={{__html: 'stateLabels'}} />
    +                             </div>
    +                         ) : null}
    +                         {!finalResponse && done ? <div>{aborted ? <p>导出已放弃</p> : null}</div> : null}
    + 
    +                         {finalResponse ? (
    +                             <div>
    +                                 <p>导出已准备</p>
    +                                 <div info-messages-raw-list-with-alert=\"finalResponse\" />
    +                             </div>
    +                         ) : null}
    + 
    +                         <div className=\"modal-footer modal-footer-std-buttons\">
    +                             {!finalResponse && !done ? (
    +                                 <div className=\"running-time pull-left\">
    +                                     开始 {friendlyDurationShort(futureResponse.runningTime)} 之前
    +                                 </div>
    +                             ) : null}
    + 
    +                             {!finalResponse && !done ? (
    +                                 <button
    +                                     type=\"button\"
    +                                     onClick={() => {
    +                                         abort();
    +                                     }}
    +                                     className=\"btn btn-danger\">
    +                                     放弃
    +                                 </button>
    +                             ) : null}
    +                             {!finalResponse && done ? (
    +                                 <button
    +                                     type=\"button\"
    +                                     onClick={() => {
    +                                         dismiss();
    +                                     }}
    +                                     className=\"btn btn-danger\">
    +                                     关闭
    +                                 </button>
    +                             ) : null}
    +                             {finalResponse ? (
    +                                 <button
    +                                     type=\"button\"
    +                                     onClick={() => {
    +                                         download();
    +                                     }}
    +                                     className=\"btn btn-primary\">
    +                                     下载
    +                                 </button>
    +                             ) : null}
    +                         </div>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;