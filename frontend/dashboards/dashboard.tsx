import * as React from 'react';
     
     export interface TestComponentProps {
         [key: string]: any;
     }
     
     const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
         return (
             <div
                 className=\"object-nav analysis-top-tabs horizontal-flex\"
                 global-keydown=\"{'ctrl-s meta-s':'saveDashboard()'}\">
                 <div std-object-breadcrumb=\"\" className=\"flex oh\">
                     <div className=\"tabs noflex\">
                         <a
                             className=\"{enabled: topNav.tab == 'summary'}\"
                             fw500-width=\"\"
                             href={$state.href('projects.project.dashboards.dashboard.summary')}>
                             概要
                         </a>
                         <a
                             className=\"{enabled: topNav.tab == 'view'}\"
                             fw500-width=\"\"
                             href={$state.href('projects.project.dashboards.dashboard.view')}>
                             查看
                         </a>
                         {canEditDashboard(dashboard) ? (
                             <a
                                 className=\"{enabled: topNav.tab == 'edit'}\"
                                 fw500-width=\"\"
                                 href={$state.href('projects.project.dashboards.dashboard.edit')}>
                                 编辑
                             </a>
                         ) : null}
     
                         <div className=\"otherLinks\">
                             {dashboard ? (
                                 <div discussions-button=\"\">
                                     {topNav.tab == 'edit' && canEditDashboard(dashboard) ? (
                                         <span>
                                             <span
                                                 object-save-button=\"\"
                                                 save=\"saveDashboard\"
                                                 can-save=\"true\"
                                                 can-write-override=\"true\"
                                                 is-dirty=\"isDirty()\"
                                                 object-id=\"dashboard.id\"
                                                 object-type=\"DASHBOARD\"
                                             />
     
                                             {canModerateDashboards() || canEditDashboard(dashboard) ? (
                                                 <div className=\"dropdown\" style=\"margin-left: 5px\">
                                                     <button
                                                         className=\"btn btn-small btn-action manual-caret dropdown-toggle\"
                                                         data-toggle=\"dropdown\"
                                                         id=\"qa_dashboard_actions-dropdown\">
                                                         操作 <i className=\"icon-caret-down\" />
                                                     </button>
                                                     <i className=\"icon-caret-down\">
                                                         <ul className=\"dropdown-menu pull-right text-left\">
                                                             {canModerateDashboards() ? (
                                                                 <li>
                                                                     <a
                                                                         onClick={() => {
                                                                             toggleDashboardListed(dashboard);
                                                                         }}>
                                                                         {dashboard.listed ? 'Make private' : 'Make public'}
                                                                     </a>
                                                                 </li>
                                                             ) : null}
                                                             {canWriteDashboards() ? (
                                                                 <li>
                                                                     <a
                                                                         onClick={() => {
                                                                             saveAndCopy();
                                                                         }}>
                                                                         拷贝
                                                                     </a>
                                                                 </li>
                                                             ) : null}
                                                             {appConfig.dashboardExportsEnabled ? (
                                                                 <li>
                                                                     <a
                                                                         onClick={() => {
                                                                             exportDashboard(
                                                                                 dashboard,
                                                                                 false,
                                                                                 true,
                                                                                 isDirty()
                                                                             );
                                                                         }}>
                                                                         导出
                                                                     </a>
                                                                 </li>
                                                             ) : null}
                                                             {canEditDashboard(dashboard) ? (
                                                                 <li className=\"dku-border-top\">
                                                                     <a
                                                                         onClick={() => {
                                                                             GlobalProjectActions.deleteTaggableObject(
                                                                                 this,
                                                                                 'DASHBOARD',
                                                                                 dashboard.id,
                                                                                 dashboard.name
                                                                             );
                                                                         }}
                                                                         id=\"qa_dashboard_delete-modal-button\">
                                                                         <span className=\"text-error\">
                                                                             <i className=\"icon-trash\">&nbsp;删除</i>
                                                                         </span>
                                                                         <i className=\"icon-trash\" />
                                                                     </a>
                                                                     <i className=\"icon-trash\" />
                                                                 </li>
                                                             ) : null}
                                                             <i className=\"icon-trash\" />
                                                         </ul>
                                                         <i className=\"icon-trash\" />
                                                     </i>
                                                 </div>
                                             ) : null}
                                             <i className=\"icon-caret-down\">
                                                 <i className=\"icon-trash\" />
                                             </i>
                                         </span>
                                     ) : null}
                                 </div>
                             ) : null}
                             <i className=\"icon-caret-down\">
                                 <i className=\"icon-trash\" />
                             </i>
                         </div>
                         <i className=\"icon-caret-down\">
                             <i className=\"icon-trash\" />
                         </i>
                     </div>
                     <i className=\"icon-caret-down\">
                         <i className=\"icon-trash\">
                             {!dashboard ? (
                                 <div className=\"dss-page\">
                                     <div block-api-error=\"\" />
     
                                     {dashboard ? <div ui-view=\"\" className=\"dss-page\" /> : null}
                                 </div>
                             ) : null}
                         </i>
                     </i>
                 </div>
             </div>
         );
     };
     
     export default TestComponent;