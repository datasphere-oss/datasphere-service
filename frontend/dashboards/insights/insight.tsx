import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             className=\"object-nav analysis-top-tabs horizontal-flex\"
    +             navigator-object=\"\"
    +             navigator-disabled=\"insight.type != 'runnable-button'\"
    +             global-keydown=\"{'ctrl-s meta-s':'saveInsight()'}\">
    +             <div std-object-breadcrumb=\"\" className=\"flex oh\">
    +                 <div className=\"tabs noflex\">
    +                     <a
    +                         fw500-width=\"\"
    +                         className=\"{enabled: topNav.tab == 'summary'}\"
    +                         href={$state.href('projects.project.dashboards.insights.insight.summary')}>
    +                         概要
    +                     </a>
    +                     <a
    +                         fw500-width=\"\"
    +                         className=\"{enabled: topNav.tab == 'view'}\"
    +                         href={$state.href('projects.project.dashboards.insights.insight.view')}>
    +                         查看
    +                     </a>
    +                     {canEditInsight(insight) && hasEditTab(insight) ? (
    +                         <a
    +                             fw500-width=\"\"
    +                             className=\"{enabled: topNav.tab == 'edit'}\"
    +                             href={$state.href('projects.project.dashboards.insights.insight.edit')}>
    +                             编辑
    +                         </a>
    +                     ) : null}
    + 
    +                     <div className=\"otherLinks\">
    +                         {insight ? (
    +                             <div discussions-button=\"\">
    +                                 {originDashboardStateParams ? (
    +                                     <span>
    +                                         <button
    +                                             className=\"btn btn-info\"
    +                                             onClick={() => {
    +                                                 backToDashboard();
    +                                             }}>
    +                                             <i className=\"icon-arrow-left\">&nbsp;&nbsp; 回到仪表板</i>
    +                                         </button>
    +                                         <i className=\"icon-arrow-left\" />
    +                                     </span>
    +                                 ) : null}
    +                                 <i className=\"icon-arrow-left\">
    +                                     {projectSummary.canReadProjectContent &&
    +                                     insight &&
    +                                     DashboardUtils.getInsightSourceType(insight) ? (
    +                                         <a
    +                                             className=\"btn btn-lab\"
    +                                             href={$root.StateUtils.href.dssObject(
    +                                                 DashboardUtils.getInsightSourceType(insight),
    +                                                 DashboardUtils.getInsightHandler(insight.type).getSourceId(insight),
    +                                                 insight.projectKey
    +                                             )}
    +                                             style=\"padding: 0 15px;\">
    +                                             <span>
    +                                                 到源{' '}
    +                                                 {lowercase(niceConst(DashboardUtils.getInsightSourceType(insight)))}
    +                                             </span>
    +                                         </a>
    +                                     ) : null}
    + 
    +                                     {topNav.tab == 'edit' && canEditInsight(insight) && hasEditTab(insight) ? (
    +                                         <span>
    +                                             <span
    +                                                 object-save-button=\"\"
    +                                                 save=\"saveInsight\"
    +                                                 can-write-override=\"true\"
    +                                                 can-save=\"true\"
    +                                                 is-dirty=\"isDirty()\"
    +                                                 object-id=\"insight.id\"
    +                                                 object-type=\"INSIGHT\"
    +                                             />
    + 
    +                                             {canWriteDashboards() ||
    +                                             canModerateDashboards() ||
    +                                             isProjectAnalystRO() ||
    +                                             insight.type == 'report' ||
    +                                             (insight.type == 'dataset_table' && canExportDatasetsData()) ? (
    +                                                 <div className=\"dropdown\" style=\"margin-left: 5px\">
    +                                                     <button
    +                                                         className=\"btn btn-action manual-caret dropdown-toggle\"
    +                                                         data-toggle=\"dropdown\"
    +                                                         href=\"\">
    +                                                         Actions <i className=\"icon-caret-down\" />
    +                                                     </button>
    +                                                     <i className=\"icon-caret-down\">
    +                                                         <ul className=\"dropdown-menu pull-right text-left\">
    +                                                             {canWriteDashboards() ? (
    +                                                                 <li>
    +                                                                     <a
    +                                                                         onClick={() => {
    +                                                                             copy(insight);
    +                                                                         }}>
    +                                                                         <i className=\"icon-copy\">拷贝</i>
    +                                                                     </a>
    +                                                                     <i className=\"icon-copy\" />
    +                                                                 </li>
    +                                                             ) : null}
    +                                                             <i className=\"icon-copy\">
    +                                                                 {canWriteDashboards() ? (
    +                                                                     <li>
    +                                                                         <a
    +                                                                             onClick={() => {
    +                                                                                 mutiPin(insight);
    +                                                                             }}
    +                                                                             id=\"qa_insight_publish-modal-button\">
    +                                                                             <i className=\"icon-dku-publish\">Publish</i>
    +                                                                         </a>
    +                                                                         <i className=\"icon-dku-publish\" />
    +                                                                     </li>
    +                                                                 ) : null}
    +                                                                 <i className=\"icon-dku-publish\">
    +                                                                     {canModerateDashboards() ? (
    +                                                                         <li>
    +                                                                             {insight.listed ? (
    +                                                                                 <a
    +                                                                                     onClick={() => {
    +                                                                                         toggleInsightListed(insight);
    +                                                                                     }}>
    +                                                                                     <i className=\"icon-dku-promote-empty\">
    +                                                                                         私有化
    +                                                                                     </i>
    +                                                                                 </a>
    +                                                                             ) : null}
    +                                                                             <i className=\"icon-dku-promote-empty\">
    +                                                                                 {!insight.listed ? (
    +                                                                                     <a
    +                                                                                         onClick={() => {
    +                                                                                             toggleInsightListed(
    +                                                                                                 insight
    +                                                                                             );
    +                                                                                         }}>
    +                                                                                         <i className=\"icon-dku-promote\">
    +                                                                                             公有化
    +                                                                                         </i>
    +                                                                                     </a>
    +                                                                                 ) : null}
    +                                                                                 <i className=\"icon-dku-promote\" />
    +                                                                             </i>
    +                                                                         </li>
    +                                                                     ) : null}
    +                                                                     <i className=\"icon-dku-promote-empty\">
    +                                                                         <i className=\"icon-dku-promote\">
    +                                                                             {insight.type == 'dataset_table' &&
    +                                                                             canExportDatasetsData() ? (
    +                                                                                 <li>
    +                                                                                     <a
    +                                                                                         ng-inject=\"SmartId\"
    +                                                                                         onClick={() => {
    +                                                                                             exportDataset(
    +                                                                                                 SmartId.resolve(
    +                                                                                                     insight.params
    +                                                                                                         .datasetSmartName
    +                                                                                                 ).projectKey,
    +                                                                                                 SmartId.resolve(
    +                                                                                                     insight.params
    +                                                                                                         .datasetSmartName
    +                                                                                                 ).id
    +                                                                                             );
    +                                                                                         }}>
    +                                                                                         <i className=\"icon-download\">
    +                                                                                             导出数据
    +                                                                                         </i>
    +                                                                                     </a>
    +                                                                                     <i className=\"icon-download\" />
    +                                                                                 </li>
    +                                                                             ) : null}
    +                                                                             <i className=\"icon-download\">
    +                                                                                 {insight.type == 'report' ? (
    +                                                                                     <li>
    +                                                                                         <a
    +                                                                                             onClick={() => {
    +                                                                                                 downloadRMarkdownReportInsight(
    +                                                                                                     insight
    +                                                                                                 );
    +                                                                                             }}>
    +                                                                                             <i className=\"icon-download\">
    +                                                                                                 下载
    +                                                                                             </i>
    +                                                                                         </a>
    +                                                                                         <i className=\"icon-download\" />
    +                                                                                     </li>
    +                                                                                 ) : null}
    +                                                                                 <i className=\"icon-download\">
    +                                                                                     {canEditInsight(insight) ? (
    +                                                                                         <li className=\"divider\" />
    +                                                                                     ) : null}
    + 
    +                                                                                     {canEditInsight(insight) ? (
    +                                                                                         <li>
    +                                                                                             <a
    +                                                                                                 onClick={() => {
    +                                                                                                     GlobalProjectActions.deleteTaggableObject(
    +                                                                                                         this,
    +                                                                                                         'INSIGHT',
    +                                                                                                         insight.id,
    +                                                                                                         insight.name
    +                                                                                                     );
    +                                                                                                 }}>
    +                                                                                                 <span className=\"text-error\">
    +                                                                                                     <i className=\"icon-trash\">
    +                                                                                                         &nbsp;删除
    +                                                                                                     </i>
    +                                                                                                 </span>
    +                                                                                                 <i className=\"icon-trash\" />
    +                                                                                             </a>
    +                                                                                             <i className=\"icon-trash\" />
    +                                                                                         </li>
    +                                                                                     ) : null}
    +                                                                                     <i className=\"icon-trash\" />
    +                                                                                 </i>
    +                                                                             </i>
    +                                                                         </i>
    +                                                                     </i>
    +                                                                 </i>
    +                                                             </i>
    +                                                         </ul>
    +                                                         <i className=\"icon-dku-publish\">
    +                                                             <i className=\"icon-dku-promote-empty\">
    +                                                                 <i className=\"icon-dku-promote\">
    +                                                                     <i className=\"icon-download\">
    +                                                                         <i className=\"icon-download\">
    +                                                                             <i className=\"icon-trash\" />
    +                                                                         </i>
    +                                                                     </i>
    +                                                                 </i>
    +                                                             </i>
    +                                                         </i>
    +                                                     </i>
    +                                                 </div>
    +                                             ) : null}
    +                                             <i className=\"icon-dku-publish\">
    +                                                 <i className=\"icon-dku-promote-empty\">
    +                                                     <i className=\"icon-dku-promote\">
    +                                                         <i className=\"icon-download\">
    +                                                             <i className=\"icon-download\">
    +                                                                 <i className=\"icon-trash\" />
    +                                                             </i>
    +                                                         </i>
    +                                                     </i>
    +                                                 </i>
    +                                             </i>
    +                                         </span>
    +                                     ) : null}
    +                                 </i>
    +                             </div>
    +                         ) : null}
    +                         <i className=\"icon-dku-publish\">
    +                             <i className=\"icon-dku-promote-empty\">
    +                                 <i className=\"icon-dku-promote\">
    +                                     <i className=\"icon-download\">
    +                                         <i className=\"icon-download\">
    +                                             <i className=\"icon-trash\" />
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </i>
    +                         </i>
    +                     </div>
    +                     <i className=\"icon-dku-publish\">
    +                         <i className=\"icon-dku-promote-empty\">
    +                             <i className=\"icon-dku-promote\">
    +                                 <i className=\"icon-download\">
    +                                     <i className=\"icon-download\">
    +                                         <i className=\"icon-trash\" />
    +                                     </i>
    +                                 </i>
    +                             </i>
    +                         </i>
    +                     </i>
    +                 </div>
    +                 <i className=\"icon-dku-publish\">
    +                     <i className=\"icon-dku-promote-empty\">
    +                         <i className=\"icon-dku-promote\">
    +                             <i className=\"icon-download\">
    +                                 <i className=\"icon-download\">
    +                                     <i className=\"icon-trash\">
    +                                         <div className=\"dss-page\">
    +                                             <div block-api-error=\"\">
    +                                                 <div ui-view=\"\" className=\"h100\" style=\"position: relative;\" />
    +                                             </div>
    +                                         </div>
    +                                     </i>
    +                                 </i>
    +                             </i>
    +                         </i>
    +                     </i>
    +                 </i>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;