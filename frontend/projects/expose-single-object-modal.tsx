import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 new-dashboard-modal dku-modal expose-object-modal\" auto-size=\"false\">
    +             <div
    +                 dku-modal-header-with-totem=\"\"
    +                 modal-title={`共享 \"${objectDisplayName}\"`}
    +                 modal-totem=\"icon-dku-share\">
    +                 <form name=\"copyDashboardForm\" className=\"dkuform-modal-vertical dkuform-modal-wrapper\">
    +                     <div className=\"modal-body\">
    +                         <div block-api-error=\"\">
    +                             {projectSummary.canManageDashboardAuthorizations &&
    +                             !objectExposition.dashboardAllAuthorized ? (
    +                                 <div className=\"control-group\">
    +                                     <label className=\"control-label\">仅共享到仪表盘用户</label>
    +                                     <div className=\"controls\">
    +                                         <label>
    +                                             <input
    +                                                 type=\"checkbox\"
    +                                                 value={dashboardAuthorized}
    +                                                 onChange={() => {
    +                                                     onDashboardAuthorize();
    +                                                 }}
    +                                             />
    +                                             仅允许仪表盘用户读取仪表盘
    +                                         </label>
    +                                     </div>
    +                                 </div>
    +                             ) : null}
    + 
    +                             {projectSummary.canManageDashboardAuthorizations &&
    +                             !objectExposition.dashboardAllAuthorized ? (
    +                                 <hr style=\"margin-bottom: 0;\" />
    +                             ) : null}
    +                             <div className=\"control-group\" style=\"margin-bottom: 0;\">
    +                                 <label className=\"control-label\" style=\"margin-top: 20px; margin-bottom: 0;\">
    +                                     目标工程
    +                                 </label>
    +                             </div>
    + 
    +                             <div className=\"project-select oa full-width dku-border-bottom\">
    +                                 <ul className=\"raw-unstyled-ul\">
    +                                     {objectExposition.rules.map((rule, index: number) => {
    +                                         return (
    +                                             <li key={`item-${index}`} className=\"mx-textellipsis\">
    +                                                 <i className=\"icon-dkubird\" />
    +                                                 {rule.targetProjectDisplayName}
    +                                                 <i
    +                                                     className=\"pull-right cursor-pointer icon-trash\"
    +                                                     onClick={() => {
    +                                                         removeProject(rule.targetProject);
    +                                                     }}
    +                                                 />
    +                                             </li>
    +                                         );
    +                                     })}
    +                                     {!uiState.addingProject ? (
    +                                         <li>
    +                                             <a
    +                                                 className=\"dku-add-new\"
    +                                                 onClick={() => {
    +                                                     uiState.addingProject = true;
    +                                                 }}>
    +                                                 添加工程
    +                                             </a>
    +                                         </li>
    +                                     ) : null}
    +                                     {uiState.addingProject ? (
    +                                         <li>
    +                                             <div
    +                                                 object-picker=\"projectKey\"
    +                                                 no-live-update=\"true\"
    +                                                 type=\"PROJECT\"
    +                                                 object=\"newProject\"
    +                                                 className=\"dibvam\"
    +                                                 unusable=\"projectKeys\"
    +                                                 available-objects=\"available.projects\"
    +                                                 empty-message=\"选择一个工程...\"
    +                                             />
    +                                         </li>
    +                                     ) : null}
    +                                 </ul>
    +                             </div>
    +                         </div>
    + 
    +                         <div className=\"modal-footer modal-footer-std-buttons full-width\">
    +                             {currentProjectKey ? (
    +                                 <div className=\"pull-left\" style=\"font-size: 14px; margin: 0 10px;\">
    +                                     <a href={`/projects/${currentProjectKey}/security/exposed`}>转到工程元素设置</a>
    +                                 </div>
    +                             ) : null}
    +                             <button
    +                                 type=\"button\"
    +                                 className=\"btn btn-default\"
    +                                 onClick={() => {
    +                                     dismiss();
    +                                 }}>
    +                                 取消
    +                             </button>
    +                             <button
    +                                 type=\"submit\"
    +                                 className=\"btn btn-primary\"
    +                                 onClick={() => {
    +                                     save();
    +                                 }}>
    +                                 共享
    +                             </button>
    +                         </div>
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;