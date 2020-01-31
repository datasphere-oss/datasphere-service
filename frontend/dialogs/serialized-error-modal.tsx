import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\" auto-size=\"false\">
    +             {error.code ? (
    +                 <div>
    +                     <div dku-modal-header=\"\" modal-title={error.title}>
    +                         <form className=\"dkuform-modal-wrapper\">
    +                             <div className=\"modal-body\" style=\"padding-top: 0; padding-bottom: 0;\">
    +                                 <h5
    +                                     style=\"word-break: break-word; font-weight:400; margin-bottom: 20px\"
    +                                     className=\"summary-of-error text-error\">
    +                                     {error.detailedMessageHTML ? (
    +                                         <span dangerouslySetInnerHTML={{__html: 'error.detailedMessageHTML'}}>
    +                                             {!error.detailedMessageHTML ? (
    +                                                 <span>{detailedMessageOrMessage(error)}</span>
    +                                             ) : null}
    +                                         </span>
    +                                     ) : null}
    +                                 </h5>
    + 
    +                                 <div error-fixability=\"\" error=\"error\">
    +                                     <p>
    +                                         <a
    +                                             target=\"_blank\"
    +                                             rel=\"noopener noreferrer\"
    +                                             href={`${$root.versionDocRoot}troubleshooting/errors/${error.code}.html`}
    +                                             style=\"font-weight: 500\">
    +                                             有关DSS文档中此错误的更多信息
    +                                         </a>
    +                                     </p>
    + 
    +                                     <div style=\"font-size:11px; color: #666;\">
    +                                         <p style=\"margin-bottom: 0\">
    +                                             <strong>额外的技术细节</strong>
    +                                         </p>
    +                                         <ul style=\"margin-bottom: 0\">
    +                                             {error.httpCode > 0 ? <li>HTTP 代码: {error.httpCode}</li> : null}
    +                                             <li>错误代码: {error.code}</li>
    +                                             <li>错误类型: {error.errorType}</li>
    +                                         </ul>
    +                                     </div>
    +                                 </div>
    +                                 <div className=\"modal-footer modal-footer-std-buttons\">
    +                                     <div className=\"pull-right\">
    +                                         <button
    +                                             type=\"submit\"
    +                                             className=\"btn btn-default btn-lg\"
    +                                             onClick={() => {
    +                                                 dismiss();
    +                                             }}>
    +                                             OK
    +                                         </button>
    +                                     </div>
    +                                 </div>
    +                             </div>
    +                         </form>
    +                         {!error.code ? (
    +                             <div ng-switch=\"\" on=\"error.errorType\">
    +                                 <div ng-switch-when=\"com.dataiku.dip.exceptions.DSSIllegalArgumentException\">
    +                                     <div dku-modal-header=\"\" modal-title=\"一个内部错误出现\">
    +                                         <form className=\"dkuform-modal-wrapper\">
    +                                             <div className=\"modal-body\" style=\"padding-top: 0; padding-bottom: 0;\">
    +                                                 {$root.appConfig.admin ? <h5>请报告此类问题到技术支持</h5> : null}
    +                                                 {!$root.appConfig.admin ? <h5>请让管理员报告问题到技术支持</h5> : null}
    +                                                 <div style=\"font-size:0.9em; color: #666;\">
    +                                                     <p style=\"margin-bottom: 0\">技术细节如下:</p>
    +                                                     <ul style=\"margin-bottom: 0\">
    +                                                         <li>{detailedMessageOrMessage(error)}</li>
    +                                                         {error.httpCode > 0 ? (
    +                                                             <li>HTTP 代码: {error.httpCode}</li>
    +                                                         ) : null}
    +                                                         <li>错误类型:{error.errorType}</li>
    +                                                     </ul>
    +                                                 </div>
    +                                             </div>
    +                                             <div className=\"modal-footer modal-footer-std-buttons\">
    +                                                 <button
    +                                                     type=\"submit\"
    +                                                     className=\"btn btn-default btn-lg pull-right\"
    +                                                     onClick={() => {
    +                                                         dismiss();
    +                                                     }}>
    +                                                     OK
    +                                                 </button>
    +                                             </div>
    +                                         </form>
    +                                     </div>
    +                                     <div ng-switch-when=\"com.dataiku.dip.exceptions.DSSInternalErrorException\">
    +                                         <div dku-modal-header=\"\" modal-title=\"一个内部错误出现\">
    +                                             <form className=\"dkuform-modal-wrapper\">
    +                                                 <div className=\"modal-body\" style=\"padding-top: 0; padding-bottom: 0;\">
    +                                                     {$root.appConfig.admin ? <h5>请报告此类问题到技术支持</h5> : null}
    +                                                     {!$root.appConfig.admin ? (
    +                                                         <h5>请让管理员报告问题到技术支持</h5>
    +                                                     ) : null}
    +                                                     <div style=\"font-size:0.9em; color: #666;\">
    +                                                         <p style=\"margin-bottom: 0\">技术细节如下:</p>
    +                                                         <ul style=\"margin-bottom: 0\">
    +                                                             <li>{detailedMessageOrMessage(error)}</li>
    +                                                             {error.httpCode > 0 ? (
    +                                                                 <li>HTTP 代码: {error.httpCode}</li>
    +                                                             ) : null}
    +                                                             <li>错误类型:{error.errorType}</li>
    +                                                         </ul>
    +                                                     </div>
    +                                                 </div>
    +                                                 <div className=\"modal-footer modal-footer-std-buttons\">
    +                                                     <button
    +                                                         type=\"submit\"
    +                                                         className=\"btn btn-default btn-lg pull-right\"
    +                                                         onClick={() => {
    +                                                             dismiss();
    +                                                         }}>
    +                                                         OK
    +                                                     </button>
    +                                                 </div>
    +                                             </form>
    +                                         </div>
    + 
    +                                         <div ng-switch-default=\"\">
    +                                             <div dku-modal-header=\"\" modal-title=\"发生错误\">
    +                                                 <div className=\"modal-body\" style=\"padding-top: 0; padding-bottom: 0;\">
    +                                                     <h5
    +                                                         style=\"word-break: break-word; font-weight:400; margin-bottom: 20px\"
    +                                                         className=\"summary-of-error  text-error\">
    +                                                         {error.detailedMessageHTML ? (
    +                                                             <span
    +                                                                 dangerouslySetInnerHTML={{
    +                                                                     __html: 'error.detailedMessageHTML'
    +                                                                 }}>
    +                                                                 {!error.detailedMessageHTML ? (
    +                                                                     <span>{detailedMessageOrMessage(error)}</span>
    +                                                                 ) : null}
    +                                                             </span>
    +                                                         ) : null}
    +                                                     </h5>
    +                                                     {$root.appConfig.admin ? <p>日志可能包含额外信息</p> : null}
    +                                                     {!$root.appConfig.admin ? (
    +                                                         <p>你需要让管理员检查日志获取额外信息</p>
    +                                                     ) : null}
    +                                                     <div style=\"font-size:0.9em; color: #666;\">
    +                                                         <p style=\"margin-bottom: 0\">
    +                                                             <strong>额外的技术细节</strong>
    +                                                         </p>
    +                                                         <ul>
    +                                                             {error.httpCode > 0 ? (
    +                                                                 <li>HTTP 代码: {error.httpCode}</li>
    +                                                             ) : null}
    +                                                             <li>错误类型:{error.errorType}</li>
    +                                                             {error.logTail ? (
    +                                                                 <pre
    +                                                                     smart-log-tail=\"error.logTail\"
    +                                                                     style=\"max-height: 100px;\">
    +                                                                     {' '}
    +                                                                 </pre>
    +                                                             ) : null}
    +                                                         </ul>
    +                                                     </div>
    +                                                 </div>
    +                                                 <div className=\"modal-footer modal-footer-std-buttons\">
    +                                                     <button
    +                                                         type=\"submit\"
    +                                                         className=\"btn btn-default btn-lg pull-right\"
    +                                                         onClick={() => {
    +                                                             dismiss();
    +                                                         }}>
    +                                                         OK
    +                                                     </button>
    +                                                 </div>
    +                                             </div>
    +                                         </div>
    +                                     </div>
    +                                 </div>
    +                             </div>
    +                         ) : null}
    +                     </div>
    +                 </div>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;