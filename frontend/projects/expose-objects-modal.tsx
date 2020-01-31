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
    +                 modal-title={`Share ${selectedItems.length} ${niceTaggableType(itemsType)}${
    +                     selectedItems.length < 2 ? '' : 's'
    +                 }`}
    +                 modal-totem=\"icon-dku-share\">
    +                 <form name=\"theForm\" className=\"dkuform-modal-vertical dkuform-modal-wrapper\">
    +                     <div className=\"modal-body\">
    +                         <div block-api-error=\"\">
    +                             {projectSummary.canManageDashboardAuthorizations &&
    +                             !objectExposition.dashboardAllAuthorized ? (
    +                                 <div className=\"control-group\">
    +                                     <label className=\"control-label\">仪表盘</label>
    +                                     {currentExpositions.dashboard.length == selectedItems.length ? (
    +                                         <span
    +                                             className=\"btn btn-default active pull-right\"
    +                                             style=\"margin-right: 20px; cursor: default;\">
    +                                             所有
    +                                         </span>
    +                                     ) : null}
    +                                     {currentExpositions.dashboard.length != selectedItems.length ? (
    +                                         <a
    +                                             className=\"{active: settings.dashboard}\"
    +                                             onClick={() => {
    +                                                 settings.dashboard = !settings.dashboard;
    +                                             }}
    +                                             style=\"margin-right: 20px\">
    +                                             所有
    +                                         </a>
    +                                     ) : null}
    +                                     {!settings.dashboard ? (
    +                                         <span className=\"exposed-count\">
    +                                             {currentExpositions.dashboard.length} / {selectedItems.length}
    +                                         </span>
    +                                     ) : null}
    +                                     {settings.dashboard ? (
    +                                         <span
    +                                             className=\"exposed-count exposed-count-updated\"
    +                                             style=\"font-weight: bold; color: #81c241;\">
    +                                             {selectedItems.length} / {selectedItems.length}
    +                                         </span>
    +                                     ) : null}
    +                                     仅读取仪表盘的用户可见
    +                                 </div>
    +                             ) : null}
    + 
    +                             <hr style=\"margin-bottom: 0;\" />
    + 
    +                             {!currentTargetProjectKeys.length ? (
    +                                 <div>
    +                                     <label className=\"control-label\" style=\"margin-top: 20px; margin-bottom: 0;\">
    +                                         目标工程
    +                                     </label>
    +                                 </div>
    +                             ) : null}
    + 
    +                             <div className=\"project-select oa full-width dku-border-bottom\">
    +                                 <ul className=\"raw-unstyled-ul\">
    +                                     {currentExpositions.projects.map((project, index: number) => {
    +                                         return (
    +                                             <li key={`item-${index}`} className=\"mx-textellipsis\">
    +                                                 <i className=\"icon-dkubird\" />
    +                                                 {project.projectDisplayName}
    +                                                 <span className=\"pull-right\">
    +                                                     {!oldTargetProjects[projectKey].exposeAll ? (
    +                                                         <span className=\"exposed-count\">
    +                                                             {project.items.length} / {selectedItems.length} 暴露
    +                                                         </span>
    +                                                     ) : null}
    +                                                     {oldTargetProjects[projectKey].exposeAll ? (
    +                                                         <span
    +                                                             className=\"exposed-count exposed-count-updated\"
    +                                                             style=\"font-weight: bold; color: #81c241;\">
    +                                                             {selectedItems.length} / {selectedItems.length} 暴露
    +                                                         </span>
    +                                                     ) : null}
    + 
    +                                                     {project.items.length == selectedItems.length ? (
    +                                                         <span
    +                                                             className=\"btn btn-default active\"
    +                                                             style=\"margin-left: 15px; cursor: default;\">
    +                                                             所有
    +                                                         </span>
    +                                                     ) : null}
    +                                                     {project.items.length != selectedItems.length ? (
    +                                                         <a
    +                                                             className=\"{active: oldTargetProjects[projectKey].exposeAll}\"
    +                                                             onClick={() => {
    +                                                                 oldTargetProjects[
    +                                                                     projectKey
    +                                                                 ].exposeAll = !oldTargetProjects[projectKey].exposeAll;
    +                                                             }}
    +                                                             style=\"margin-left: 15px\">
    +                                                             所有
    +                                                         </a>
    +                                                     ) : null}
    +                                                 </span>
    +                                             </li>
    +                                         );
    +                                     })}
    + 
    +                                     {newTargetProjects.map((project, index: number) => {
    +                                         return (
    +                                             <li key={`item-${index}`} className=\"mx-textellipsis\">
    +                                                 <i className=\"icon-dkubird\" />
    +                                                 {project.label}
    +                                                 <span
    +                                                     className=\"new-target-project\"
    +                                                     style=\"font-weight: 500; margin-left: 20px; color: #81c241;\">
    +                                                     新建
    +                                                 </span>
    +                                                 <span className=\"pull-right\">
    +                                                     <span
    +                                                         className=\"exposed-count exposed-count-updated\"
    +                                                         style=\"font-weight: bold; color: #81c241;\">
    +                                                         {selectedItems.length} / {selectedItems.length}
    +                                                     </span>
    +                                                     <a
    +                                                         className=\"link-std\"
    +                                                         onClick={() => {
    +                                                             removeProject(project.projectKey);
    +                                                         }}
    +                                                         style=\"margin-left: 60px\">
    +                                                         <i className=\"icon-trash\" />
    +                                                     </a>
    +                                                     <i className=\"icon-trash\" />
    +                                                 </span>
    +                                                 <i className=\"icon-trash\" />
    +                                             </li>
    +                                         );
    +                                     })}
    +                                     <i className=\"icon-trash\">
    +                                         {!uiState.addingProject ? (
    +                                             <li>
    +                                                 <a
    +                                                     className=\"dku-add-new\"
    +                                                     onClick={() => {
    +                                                         uiState.addingProject = true;
    +                                                     }}>
    +                                                     添加工程
    +                                                 </a>
    +                                             </li>
    +                                         ) : null}
    + 
    +                                         {uiState.addingProject ? (
    +                                             <li>
    +                                                 <div
    +                                                     object-picker=\"projectKey\"
    +                                                     no-live-update=\"true\"
    +                                                     type=\"PROJECT\"
    +                                                     object=\"newProject\"
    +                                                     className=\"dibvam\"
    +                                                     unusable=\"allTargetProjects\"
    +                                                     available-objects=\"available.projects\"
    +                                                     empty-message=\"选择一个工程...\"
    +                                                 />
    +                                             </li>
    +                                         ) : null}
    +                                     </i>
    +                                 </ul>
    +                                 <i className=\"icon-trash\" />
    +                             </div>
    +                             <i className=\"icon-trash\" />
    +                         </div>
    +                         <i className=\"icon-trash\">
    +                             <div className=\"modal-footer modal-footer-std-buttons full-width\">
    +                                 <div className=\"pull-left\" style=\"font-size: 14px; margin: 0 10px;\">
    +                                     <a href={`/projects/${$stateParams.projectKey}/security/exposed`}>
    +                                         转到工程元素设置
    +                                     </a>
    +                                 </div>
    +                                 <button
    +                                     type=\"button\"
    +                                     className=\"btn btn-default\"
    +                                     onClick={() => {
    +                                         dismiss();
    +                                     }}>
    +                                     取消
    +                                 </button>
    +                                 <button
    +                                     type=\"submit\"
    +                                     className=\"btn btn-primary\"
    +                                     onClick={() => {
    +                                         save();
    +                                     }}>
    +                                     共享
    +                                 </button>
    +                             </div>
    +                         </i>
    +                     </div>
    +                 </form>
    +                 <i className=\"icon-trash\" />
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;