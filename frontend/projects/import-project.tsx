import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 import-project-modal\" tab-model=\"modalTabState.active\" auto-size=\"false\">
    +             <div dku-modal-header-with-totem=\"\" modal-title=\"Import project\" modal-totem=\"icon-plus\">
    +                 <form className=\"dkuform-modal-horizontal dkuform-modal-wrapper\" name=\"importProjectForm\">
    +                     <div className=\"modal-body\">
    +                         {phase == 'READY_TO_UPLOAD' ? (
    +                             <div>
    +                                 <div className=\"control-group\">
    +                                     <label htmlFor=\"newProjectFile\" className=\"control-label\">
    +                                         源文件
    +                                     </label>
    +                                     <div className=\"controls\">
    +                                         <input
    +                                             id=\"newProjectFile\"
    +                                             type=\"file\"
    +                                             style=\"line-height: 10px; width: 250px;\"
    +                                             required={true}
    +                                             valid-file=\"\"
    +                                             value={importData.file}
    +                                             accept=\"application/zip\"
    +                                         />
    +                                     </div>
    +                                 </div>
    + 
    +                                 <div className=\"control-group\">
    +                                     <label htmlFor=\"advancedOptions\" className=\"control-label\">
    +                                         上传后显示高级选项
    +                                     </label>
    +                                     <div className=\"controls\">
    +                                         <input id=\"advancedOptions\" type=\"checkbox\" value={prepare.enabled} />
    +                                     </div>
    +                                 </div>
    +                             </div>
    +                         ) : null}
    + 
    +                         {phase == 'UPLOADING' ? (
    +                             <div>
    +                                 <ProgressBar
    +                                     neutral={uploadProgress}
    +                                     allow-empty=\"true\"
    +                                     className=\"progress-striped\"
    +                                     active=\"true\"
    +                                 />
    +                                 <p>正在上传 ...</p>
    +                             </div>
    +                         ) : null}
    + 
    +                         {phase == 'IMPORTING' ? (
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
    + 
    +                         {phase == 'READY_TO_IMPORT' ? (
    +                             <div>
    +                                 <h5>覆盖工程键</h5>
    +                                 <p style=\"color: gray\">项目标识用于引用项目之间的数据集. 创建项目后无法更改.</p>
    +                                 <div>
    +                                     <input
    +                                         type=\"text\"
    +                                         id=\"newProjectKey\"
    +                                         value={importSettings.targetProjectKey}
    +                                         name=\"projectKey\"
    +                                         pattern=\"/^\\w+$/\"
    +                                         autoComplete={off}
    +                                         placeholder={prepareResponse.originalProjectKey}
    +                                         style=\"vertical-align: top\"
    +                                         className=\"ng-dirty\"
    +                                     />
    + 
    +                                     {importProjectForm.projectKey.$error.unique &&
    +                                     !importProjectForm.projectKey.$error.pattern ? (
    +                                         <span style=\"margin-left: 20px;\">此项目标识已被使用</span>
    +                                     ) : null}
    + 
    +                                     {importProjectForm.projectKey.$error.pattern ? (
    +                                         <span style=\"margin-left: 20px;\">此项目标识无效</span>
    +                                     ) : null}
    +                                 </div>
    + 
    +                                 <h5>连接重映射</h5>
    +                                 <p style=\"color: gray\">
    +                                     如果遇到与连接相关的错误，请将此选项用于将导入存档中声明的连接名称重新映射到DSS实例中现有的连接.
    +                                 </p>
    + 
    +                                 <div
    +                                     style=\"display: inline-block; width: 100%;\"
    +                                     add-remove=\"\"
    +                                     value={importSettings.remapping.connections}>
    +                                     <ul className=\"raw-unstyled-ul\">
    +                                         {importSettings.remapping.connections.map((conn, index: number) => {
    +                                             return (
    +                                                 <li key={`item-${index}`}>
    +                                                     <div
    +                                                         className=\"remapping\"
    +                                                         style=\"margin: 5px 0\"
    +                                                         import-project-remapping-form=\"\">
    +                                                         <input
    +                                                             type=\"text\"
    +                                                             value={conn.source}
    +                                                             bs-typeahead=\"usedConnections\"
    +                                                             onFocus={(event: React.SyntheticEvent<HTMLElement>) => {
    +                                                                 inputFocus(event);
    +                                                             }}
    +                                                             data-min-length=\"0\"
    +                                                         />
    + 
    +                                                         <i
    +                                                             className=\"icon-long-arrow-right\"
    +                                                             style=\"font-size: 17px; margin: 0 10px; color: grey;\"
    +                                                         />
    + 
    +                                                         <select
    +                                                             value={target}
    +                                                             dku-bs-select=\"{liveSearch:true,size:'auto'}\"
    +                                                             ng-options=\"c as c.name group by c.type for c in prepareResponse.availableConnections | orderBy:connComparator(conn.source) track by c.name\"
    +                                                             onChange={() => {
    +                                                                 conn.target = target.name;
    +                                                             }}
    +                                                             ng-init=\"target = findConnection(prepareResponse.availableConnections, conn.target)\"
    +                                                         />
    + 
    +                                                         {conn.source &&
    +                                                         conn.target &&
    +                                                         findConnection(prepareResponse.usedConnections, conn.source)
    +                                                             .type &&
    +                                                         findConnection(prepareResponse.usedConnections, conn.source)
    +                                                             .type != target.type ? (
    +                                                             <span>
    +                                                                 <i
    +                                                                     className=\"icon-warning-sign\"
    +                                                                     toggle=\"tooltip\"
    +                                                                     title={`Type mismatch, expecting ${
    +                                                                         findConnection(
    +                                                                             prepareResponse.usedConnections,
    +                                                                             conn.source
    +                                                                         ).type
    +                                                                     }`}
    +                                                                     placement=\"right\"
    +                                                                     container=\"body\"
    +                                                                 />
    +                                                             </span>
    +                                                         ) : null}
    + 
    +                                                         <a
    +                                                             onClick={() => {
    +                                                                 remove($index);
    +                                                             }}
    +                                                             className=\"pull-right\"
    +                                                             style=\"color: grey\">
    +                                                             <i className=\"icon-trash \" />
    +                                                         </a>
    +                                                         <i className=\"icon-trash \" />
    +                                                     </div>
    +                                                     <i className=\"icon-trash \" />
    +                                                 </li>
    +                                             );
    +                                         })}
    +                                         <i className=\"icon-trash \" />
    +                                     </ul>
    +                                     <i className=\"icon-trash \">
    +                                         <div className=\"add-value\">
    +                                             <span
    +                                                 className=\"pull-right\"
    +                                                 onClick={() => {
    +                                                     refreshConnections();
    +                                                 }}
    +                                                 toggle=\"tooltip\"
    +                                                 title=\"刷新连接列表\"
    +                                                 placement=\"left\"
    +                                                 container=\"body\"
    +                                                 style=\"text-align: right\">
    +                                                 <i className=\"icon-refresh\" />
    +                                             </span>
    +                                             <i className=\"icon-refresh\">
    +                                                 <span
    +                                                     onClick={() => {
    +                                                         add({});
    +                                                     }}>
    +                                                     <i className=\"icon-plus-sign\"> 添加重新映射</i>
    +                                                 </span>
    +                                                 <i className=\"icon-plus-sign\" />
    +                                             </i>
    +                                         </div>
    +                                         <i className=\"icon-refresh\">
    +                                             <i className=\"icon-plus-sign\" />
    +                                         </i>
    +                                     </i>
    +                                 </div>
    +                                 <i className=\"icon-trash \">
    +                                     <i className=\"icon-refresh\">
    +                                         <i className=\"icon-plus-sign\">
    +                                             <h5>代码环境重新映射</h5>
    +                                             <p style=\"color: gray\">
    +                                                 如果您遇到与代码环境相关的错误，请将此选项用于将导入存档中声明的代码环境重新映射到DSS实例中现有的代码环境.
    +                                             </p>
    + 
    +                                             <div
    +                                                 style=\"display: inline-block; width: 100%;\"
    +                                                 add-remove=\"\"
    +                                                 value={importSettings.remapping.codeEnvs}>
    +                                                 <ul className=\"raw-unstyled-ul\">
    +                                                     {importSettings.remapping.codeEnvs.map((codeEnv, index: number) => {
    +                                                         return (
    +                                                             <li key={`item-${index}`}>
    +                                                                 <div
    +                                                                     className=\"remapping\"
    +                                                                     style=\"margin: 5px 0\"
    +                                                                     import-project-remapping-form=\"\">
    +                                                                     <input
    +                                                                         type=\"text\"
    +                                                                         value={codeEnv.source}
    +                                                                         bs-typeahead=\"usedCodeEnvs\"
    +                                                                         onFocus={(
    +                                                                             event: React.SyntheticEvent<HTMLElement>
    +                                                                         ) => {
    +                                                                             inputFocus(event);
    +                                                                         }}
    +                                                                         data-min-length=\"0\"
    +                                                                     />
    + 
    +                                                                     <i
    +                                                                         className=\"icon-long-arrow-right\"
    +                                                                         style=\"font-size: 17px; margin: 0 10px; color: grey;\"
    +                                                                     />
    + 
    +                                                                     <select
    +                                                                         value={target}
    +                                                                         dku-bs-select=\"{liveSearch:true,size:'auto'}\"
    +                                                                         ng-options=\"c as c.envName group by c.envLang for c in availableCodeEnvs | orderBy:codeEnvComparator(codeEnv.source) track by c.envName\"
    +                                                                         onChange={() => {
    +                                                                             codeEnv.target = target.builtin
    +                                                                                 ? ''
    +                                                                                 : target.envName;
    +                                                                         }}
    +                                                                         ng-init=\"target = findCodeEnv(availableCodeEnvs, codeEnv.target)\"
    +                                                                     />
    + 
    +                                                                     {codeEnv.source &&
    +                                                                     codeEnv.target &&
    +                                                                     findCodeEnv(
    +                                                                         prepareResponse.usedConnections,
    +                                                                         codeEnv.source
    +                                                                     ).envLang &&
    +                                                                     findCodeEnv(
    +                                                                         prepareResponse.usedConnections,
    +                                                                         codeEnv.source
    +                                                                     ).envLang != target.envLang ? (
    +                                                                         <span>
    +                                                                             <i
    +                                                                                 className=\"icon-warning-sign\"
    +                                                                                 toggle=\"tooltip\"
    +                                                                                 title={`Type mismatch, expecting ${
    +                                                                                     findCodeEnv(
    +                                                                                         prepareResponse.usedConnections,
    +                                                                                         codeEnv.source
    +                                                                                     ).envLang
    +                                                                                 }`}
    +                                                                                 placement=\"right\"
    +                                                                                 container=\"body\"
    +                                                                             />
    +                                                                         </span>
    +                                                                     ) : null}
    + 
    +                                                                     <a
    +                                                                         onClick={() => {
    +                                                                             remove($index);
    +                                                                         }}
    +                                                                         className=\"pull-right\"
    +                                                                         style=\"color: grey\">
    +                                                                         <i className=\"icon-trash \" />
    +                                                                     </a>
    +                                                                     <i className=\"icon-trash \" />
    +                                                                 </div>
    +                                                                 <i className=\"icon-trash \" />
    +                                                             </li>
    +                                                         );
    +                                                     })}
    +                                                     <i className=\"icon-trash \" />
    +                                                 </ul>
    +                                                 <i className=\"icon-trash \">
    +                                                     <div className=\"add-value\">
    +                                                         <span
    +                                                             className=\"pull-right\"
    +                                                             onClick={() => {
    +                                                                 refreshCodeEnvs();
    +                                                             }}
    +                                                             toggle=\"tooltip\"
    +                                                             title=\"刷新连接列表\"
    +                                                             placement=\"left\"
    +                                                             container=\"body\"
    +                                                             style=\"text-align: right\">
    +                                                             <i className=\"icon-refresh\" />
    +                                                         </span>
    +                                                         <i className=\"icon-refresh\">
    +                                                             <span
    +                                                                 onClick={() => {
    +                                                                     add({});
    +                                                                 }}>
    +                                                                 <i className=\"icon-plus-sign\"> 添加重新映射</i>
    +                                                             </span>
    +                                                             <i className=\"icon-plus-sign\" />
    +                                                         </i>
    +                                                     </div>
    +                                                     <i className=\"icon-refresh\">
    +                                                         <i className=\"icon-plus-sign\" />
    +                                                     </i>
    +                                                 </i>
    +                                             </div>
    +                                             <i className=\"icon-trash \">
    +                                                 <i className=\"icon-refresh\">
    +                                                     <i className=\"icon-plus-sign\">
    +                                                         {importResponse.messages.length > 0 ? (
    +                                                             <ul
    +                                                                 className=\"raw-unstyled-ul alert alert-error\"
    +                                                                 style=\"margin-top: 20px;\">
    +                                                                 <h4>遇到问题</h4>
    +                                                                 {importResponse.messages.map(
    +                                                                     (message, index: number) => {
    +                                                                         return (
    +                                                                             <li
    +                                                                                 key={`item-${index}`}
    +                                                                                 className=\"row-fluid\">
    +                                                                                 <div
    +                                                                                     className=\"span2\"
    +                                                                                     style=\"padding-top: 14px;\">
    +                                                                                     <span
    +                                                                                         className={`${
    +                                                                                             DKUConstants.design
    +                                                                                                 .alertClasses[
    +                                                                                                 message.severity
    +                                                                                             ]
    +                                                                                         } severity`}>
    +                                                                                         {message.severity}
    +                                                                                     </span>
    +                                                                                 </div>
    +                                                                                 <div className=\"span10\">
    +                                                                                     <h5>{message.title}</h5>
    +                                                                                     {message.message}
    +                                                                                 </div>
    +                                                                             </li>
    +                                                                         );
    +                                                                     }
    +                                                                 )}
    +                                                             </ul>
    +                                                         ) : null}
    +                                                     </i>
    +                                                 </i>
    +                                             </i>
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </div>
    +                         ) : null}
    +                         <i className=\"icon-refresh\">
    +                             <i className=\"icon-plus-sign\">
    +                                 <i className=\"icon-trash \">
    +                                     <i className=\"icon-refresh\">
    +                                         <i className=\"icon-plus-sign\">
    +                                             <div block-api-error=\"\" />
    + 
    +                                             <div className=\"modal-footer modal-footer-std-buttons\">
    +                                                 <button
    +                                                     type=\"button\"
    +                                                     className=\"btn btn-default\"
    +                                                     onClick={() => {
    +                                                         dismiss();
    +                                                     }}>
    +                                                     取消
    +                                                 </button>
    + 
    +                                                 {phase == 'READY_TO_IMPORT' ? (
    +                                                     <button
    +                                                         type=\"submit\"
    +                                                         className=\"btn btn-primary\"
    +                                                         onClick={() => {
    +                                                             attemptImport();
    +                                                         }}
    +                                                         disabled={importProjectForm.projectKey.$invalid}>
    +                                                         导入
    +                                                     </button>
    +                                                 ) : null}
    + 
    +                                                 {phase == 'READY_TO_UPLOAD' ? (
    +                                                     <button
    +                                                         type=\"submit\"
    +                                                         className=\"btn btn-primary\"
    +                                                         onClick={() => {
    +                                                             startImport();
    +                                                         }}
    +                                                         disabled={!importData.file}>
    +                                                         导入
    +                                                     </button>
    +                                                 ) : null}
    +                                             </div>
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </i>
    +                         </i>
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;