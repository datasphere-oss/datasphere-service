import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 modal-w800\" auto-size=\"false\">
    +             <div className=\"vertical-flex h100\">
    +                 <div dku-modal-header=\"\" modal-title={modalTitle}>
    +                     <div className=\"modal-body flex\">
    +                         {data.maxSeverity == 'ERROR' ? (
    +                             <div className=\"alert alert-danger\">
    +                                 <h4>遇到错误</h4>
    +                             </div>
    +                         ) : null}
    +                         {data.maxSeverity == 'WARNING' ? (
    +                             <div className=\"alert alert-warning\">
    +                                 <h4>遇到警告</h4>
    +                             </div>
    +                         ) : null}
    +                         {data.maxSeverity == 'SUCCESS' ? (
    +                             <div className=\"alert alert-success\">
    +                                 <h4>成功</h4>
    +                             </div>
    +                         ) : null}
    +                         {data.maxSeverity == 'INFO' ? (
    +                             <div className=\"alert alert-info\">
    +                                 <h4>信息</h4>
    +                             </div>
    +                         ) : null}
    + 
    +                         <ul className=\"raw-unstyled-ul\">
    +                             {data.messages.map((message, index: number) => {
    +                                 return (
    +                                     <li key={`item-${index}`} className=\"row-fluid info-message-inline-display\">
    +                                         <div className=\"span2\">
    +                                             <span
    +                                                 className={`${
    +                                                     DKUConstants.design.alertClasses[message.severity]
    +                                                 } severity`}>
    +                                                 {message.severity}
    +                                             </span>
    +                                         </div>
    +                                         <div className=\"span10\">
    +                                             {message.code ? (
    +                                                 <span className=\"pull-right mtop10 smallgrey\" pull-right=\"\">
    +                                                     {message.code}
    +                                                 </span>
    +                                             ) : null}
    +                                             <h5>{message.title}</h5>
    +                                             {message.message && !message.details ? (
    +                                                 <span>{message.message}</span>
    +                                             ) : null}
    +                                             {message.details ? (
    +                                                 <span className=\"message-details\">{message.details}</span>
    +                                             ) : null}
    +                                         </div>
    +                                     </li>
    +                                 );
    +                             })}
    +                         </ul>
    + 
    +                         {log && log.lines.length > 0 ? (
    +                             <div>
    +                                 <pre smart-log-tail=\"log\" style=\"max-height: 250px;\">
    +                                     {' '}
    +                                 </pre>
    +                             </div>
    +                         ) : null}
    +                     </div>
    +                     <div data-block=\"modal-footer-block \" className=\"modal-footer modal-footer-std-buttons noflex\">
    +                         <button
    +                             className=\"btn btn-default\"
    +                             onClick={() => {
    +                                 dismiss();
    +                             }}>
    +                             OK
    +                         </button>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;