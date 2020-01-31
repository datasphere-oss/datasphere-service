import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\" auto-size=\"false\">
    +             <form className=\"dkuform-modal-wrapper h100\" global-keydown=\"{'esc': 'cancel();' }\">
    +                 {/* Eat the focus so that a dangerous button don't get it */}
    +                 <input type=\"hidden\" autoFocus={true} />
    + 
    +                 <div className=\"vertical-flex h100\">
    +                     <div dku-modal-header=\"\" modal-title={modalTitle}>
    +                         <div className=\"modal-body flex\">
    +                             {data.maxSeverity == 'ERROR' ? (
    +                                 <div className=\"alert alert-danger\">
    +                                     <h4>遇到错误</h4>
    +                                 </div>
    +                             ) : null}
    +                             {data.maxSeverity == 'WARNING' ? (
    +                                 <div className=\"alert alert-warning\">
    +                                     <h4>遇到警告</h4>
    +                                 </div>
    +                             ) : null}
    +                             {data.maxSeverity == 'SUCCESS' ? (
    +                                 <div className=\"alert alert-success\">
    +                                     <h4>成功</h4>
    +                                 </div>
    +                             ) : null}
    +                             {data.maxSeverity == 'INFO' ? (
    +                                 <div className=\"alert alert-info\">
    +                                     <h4>信息</h4>
    +                                 </div>
    +                             ) : null}
    + 
    +                             <ul className=\"raw-unstyled-ul\">
    +                                 {data.messages.map((message, index: number) => {
    +                                     return (
    +                                         <li key={`item-${index}`} className=\"row-fluid info-message-inline-display\">
    +                                             <div className=\"span2\">
    +                                                 <span
    +                                                     className={`${
    +                                                         DKUConstants.design.alertClasses[message.severity]
    +                                                     } severity`}>
    +                                                     {message.severity}
    +                                                 </span>
    +                                             </div>
    +                                             <div className=\"span10\">
    +                                                 {/*<span class=\"pull-right mtop10 smallgrey\" pull-right ng-show=\"message.code\">{{message.code}}</span>*/}
    +                                                 <h5>{message.title}</h5>
    +                                                 {message.message && !message.details ? (
    +                                                     <span>{message.message}</span>
    +                                                 ) : null}
    +                                                 {message.details ? (
    +                                                     <span className=\"message-details\">{message.details}</span>
    +                                                 ) : null}
    +                                             </div>
    +                                         </li>
    +                                     );
    +                                 })}
    +                             </ul>
    + 
    +                             {text && data.messages.length ? <hr /> : null}
    + 
    +                             {text ? <p dangerouslySetInnerHTML={{__html: 'text'}} /> : null}
    +                         </div>
    +                     </div>
    + 
    +                     <div className=\"modal-footer modal-footer-std-buttons\">
    +                         {/* Since there is no input with focus, button type='submit' is not enough, we need a global
    +             handler */}
    +                         {positive ? (
    +                             <div className=\"pull-right\" global-keydown=\"{'enter': 'confirm();' }\">
    +                                 <button
    +                                     type=\"button\"
    +                                     className=\"btn btn-default btn-lg\"
    +                                     onClick={() => {
    +                                         cancel();
    +                                     }}>
    +                                     取消
    +                                 </button>
    +                                 <button
    +                                     type=\"submit\"
    +                                     className=\"btn btn-lg btn-primary\"
    +                                     onClick={() => {
    +                                         confirm();
    +                                     }}>
    +                                     确定
    +                                 </button>
    +                             </div>
    +                         ) : null}
    +                         {!positive ? (
    +                             <div className=\"pull-right\">
    +                                 {/* Dangerous modals don't validate on enter*/}
    +                                 <button
    +                                     type=\"button\"
    +                                     className=\"btn btn-default btn-lg\"
    +                                     onClick={() => {
    +                                         cancel();
    +                                     }}>
    +                                     取消
    +                                 </button>
    +                                 <button
    +                                     type=\"button\"
    +                                     className=\"btn btn-danger btn-lg\"
    +                                     onClick={() => {
    +                                         confirm();
    +                                     }}>
    +                                     确定
    +                                 </button>
    +                             </div>
    +                         ) : null}
    +                     </div>
    +                 </div>
    +             </form>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;