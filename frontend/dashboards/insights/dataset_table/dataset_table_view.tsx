import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             className=\"shaker fixedPanes shaker-read-only dataset-table-insight\"
    +             shaker-facets=\"\"
    +             shaker-explore-base=\"\"
    +             shaker-on-dataset=\"\"
    +             shaker-explore-insight=\"\"
    +             ng-init=\"baseInit()\">
    +             {shakerState.activeView == 'table' ? (
    +                 <div className=\"mainPane\" style=\"padding-right: 20px; padding-bottom: 20px;\">
    +                     {/* Main pane top bar */}
    +                     {shaker ? (
    +                         <div className=\"table-view-header\">
    +                             {/* filters bar */}
    +                             <div include-no-scope=\"/templates/shaker/filters-column.html\" />
    +                         </div>
    +                     ) : null}
    + 
    +                     {/* Main pane main zone */}
    +                     <div className=\"table-view\" style=\"flex: 1; display: flex; flex-direction: column;\">
    +                         {shakerState.runError ? (
    +                             <div>
    +                                 {/* Hide error for managed-buildable-never-built-datasets
    +                 (because they need building, so we'll display the CTA) */}
    +                                 {!datasetFullInfo.dataset.managed &&
    +                                 datasetFullInfo.buildable &&
    +                                 datasetFullInfo.lastBuild === undefined ? (
    +                                     <div api-error-alert=\"shakerState.runError\" />
    +                                 ) : null}
    + 
    +                                 <div future-waiting=\"\" response=\"future\" dku-if=\"future &amp;&amp; !future.hasResult\" />
    + 
    +                                 {/* when the dataset is empty and 1) appears to not have been build and 2) there is no error */}
    +                                 {shakerState.initialRefreshDone && datasetFullInfo ? (
    +                                     <div>
    +                                         {!(
    +                                             shakerState.runError &&
    +                                             (!datasetFullInfo.dataset.managed ||
    +                                                 (datasetFullInfo.buildable && datasetFullInfo.lastBuild !== undefined))
    +                                         ) ||
    +                                         table.initialRows > 0 ||
    +                                         !datasetFullInfo.buildable ||
    +                                         datasetFullInfo.lastBuild !== undefined ? (
    +                                             <div style=\"margin-top: 2em\">
    +                                                 {!datasetFullInfo.currentBuildState.beingBuilt.length ||
    +                                                 datasetFullInfo.currentBuildState.aboutToBeBuilt.length ? (
    +                                                     <div>
    +                                                         <h4 style=\"text-align: center; margin-top: 100px; font-weight: normal\">
    +                                                             <p>此数据集为空. 你应该建立它</p>
    +                                                             <a
    +                                                                 className=\"btn btn-lab\"
    +                                                                 href={datasetHref()}
    +                                                                 style=\"padding: 0 15px;\">
    +                                                                 <span>转到源数据集</span>
    +                                                             </a>
    +                                                         </h4>
    +                                                     </div>
    +                                                 ) : null}
    +                                                 {datasetFullInfo.currentBuildState.beingBuilt.length ? (
    +                                                     <div>
    +                                                         <h4 style=\"text-align: center; margin-top: 100px; font-weight: normal\">
    +                                                             <p>数据集正在被构建</p>
    +                                                             <NavLink
    +                                                                 className=\"btn btn-default btn-large\"
    +                                                                 to=\"projects.project.jobs.job(datasetFullInfo.currentBuildState.beingBuilt[0])\">
    +                                                                 查看任务
    +                                                             </NavLink>
    +                                                         </h4>
    +                                                     </div>
    +                                                 ) : null}
    +                                                 {datasetFullInfo.currentBuildState.aboutToBeBuilt.length &&
    +                                                 !datasetFullInfo.currentBuildState.beingBuilt.length ? (
    +                                                     <div>
    +                                                         <h4 style=\"text-align: center; margin-top: 100px; font-weight: normal\">
    +                                                             <p>数据集即将构建完毕</p>
    +                                                             <NavLink
    +                                                                 className=\"btn btn-default btn-large\"
    +                                                                 to=\"projects.project.jobs.job(datasetFullInfo.currentBuildState.aboutToBeBuilt[0])\">
    +                                                                 查看任务
    +                                                             </NavLink>
    +                                                         </h4>
    +                                                     </div>
    +                                                 ) : null}
    +                                             </div>
    +                                         ) : null}
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 {!shakerState.runError &&
    +                                 (table.initialRows > 0 ||
    +                                     !datasetFullInfo.buildable ||
    +                                     datasetFullInfo.lastBuild !== undefined) ? (
    +                                     <div
    +                                         className={`positionTable shakerTable coloring-${table.coloringScheme}`}
    +                                         style=\"width: 100%; max-width: 100%; overflow: hidden;\"
    +                                         fattable=\"\"
    +                                         fattable-data=\"table\"
    +                                     />
    +                                 ) : null}
    + 
    +                                 {pendingRequests.length && spinnerPosition == 'shaker' ? <spinner /> : null}
    +                             </div>
    +                         ) : null}
    +                     </div>
    + 
    +                     {/* the use of ng-if here makes it so that long calls are not triggered whenever the sample is refreshed if the column view is not shown (ex: multiColumnAnalysis ) */}
    +                     {shakerState.activeView == 'columns' ? (
    +                         <div className=\"mainPane h100\" analysis-columns=\"\">
    +                             <div className=\"h100\" include-no-scope=\"/templates/datasets/columns-view.html\" />
    +                         </div>
    +                     ) : null}
    +                 </div>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;