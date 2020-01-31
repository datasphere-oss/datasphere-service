import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             className=\"{showLeftPane: showLeftPane, showRightPane: showRightPane}\"
    +             shaker-facets=\"\"
    +             shaker-explore-base=\"\"
    +             shaker-on-dataset=\"\"
    +             shaker-explore-insight=\"\"
    +             show-left-pane=\"false\"
    +             fixed-panes=\"\"
    +             ng-init=\"baseInit()\">
    +             <div className=\"leftPane\">
    +                 <div
    +                     className=\"handle\"
    +                     onClick={() => {
    +                         toggleLeftPane();
    +                     }}
    +                 />
    +                 <tabs>
    +                     <pane title=\"Sample settings\" subtitle=\"getSampleDesc()\">
    +                         <div include-no-scope=\"/templates/shaker/sampling-tab.html\" />
    + 
    +                         {shakerState.activeView == 'table' ? (
    +                             <div className=\"mainPane\" style=\"padding-bottom: 20px;\">
    +                                 {/* Main pane top bar */}
    +                                 {shaker ? (
    +                                     <div className=\"table-view-header\">
    +                                         {/* Big top bar */}
    +                                         <div className=\"shaker-summary-wrapper\">
    +                                             {/* Step summury */}
    +                                             <div include-no-scope=\"/templates/shaker/shaker-summary-stats.html\">
    +                                                 {/* Views Buttons */}
    +                                                 <div className=\"view-buttons-wrapper\">
    +                                                     {table.warnings.totalCount ? (
    +                                                         <a
    +                                                             style=\"font-size: 27px; vertical-align: middle; text-decoration: none\"
    +                                                             onClick={() => {
    +                                                                 showWarningsDetails();
    +                                                             }}
    +                                                             className=\"text-warning\">
    +                                                             <i
    +                                                                 className=\"icon-warning-sign\"
    +                                                                 title=\"当读取输入时发生警告\"
    +                                                             />
    +                                                         </a>
    +                                                     ) : null}
    +                                                     <i className=\"icon-warning-sign\" title=\"当读取输入时发生警告\">
    +                                                         <div
    +                                                             include-no-scope=\"/templates/shaker/display-mode-menu.html\"
    +                                                             className=\"dib\">
    + 
    +                                                             <div
    +                                                                 include-no-scope=\"/templates/shaker/shaker-views-buttons.html\"
    +                                                                 className=\"dib\"
    +                                                             />
    +                                                         </div>
    + 
    +                                                         {/* filters bar */}
    +                                                         <div
    +                                                             include-no-scope=\"/templates/shaker/filters-column.html\"
    +                                                             style=\"padding-right: 20px;\"
    +                                                         />
    +                                                     </i>
    +                                                 </div>
    +                                                 <i className=\"icon-warning-sign\" title=\"当读取输入时发生警告\">
    +                                                     {/* Main pane main zone */}
    +                                                     <div
    +                                                         className=\"table-view\"
    +                                                         style=\"flex: 1; display: flex; flex-direction: column; padding-right: 20px;\">
    +                                                         {shakerState.runError ? (
    +                                                             <div>
    +                                                                 {/* Hide error for managed-buildable-never-built-datasets
    +                 (because they need building, so we'll display the CTA) */}
    +                                                                 {!datasetFullInfo.dataset.managed &&
    +                                                                 datasetFullInfo.buildable &&
    +                                                                 datasetFullInfo.lastBuild === undefined ? (
    +                                                                     <div api-error-alert=\"shakerState.runError\" />
    +                                                                 ) : null}
    + 
    +                                                                 <div
    +                                                                     future-waiting=\"\"
    +                                                                     response=\"future\"
    +                                                                     dku-if=\"future &amp;&amp; !future.hasResult\"
    +                                                                 />
    + 
    +                                                                 {/* when the dataset is empty and 1) appears to not have been build and 2) there is no error */}
    +                                                                 {shakerState.initialRefreshDone && datasetFullInfo ? (
    +                                                                     <div>
    +                                                                         {!(
    +                                                                             shakerState.runError &&
    +                                                                             (!datasetFullInfo.dataset.managed ||
    +                                                                                 (datasetFullInfo.buildable &&
    +                                                                                     datasetFullInfo.lastBuild !==
    +                                                                                         undefined))
    +                                                                         ) ||
    +                                                                         table.initialRows > 0 ||
    +                                                                         !datasetFullInfo.buildable ||
    +                                                                         datasetFullInfo.lastBuild !== undefined ? (
    +                                                                             <div style=\"margin-top: 2em\">
    +                                                                                 {!datasetFullInfo.currentBuildState
    +                                                                                     .beingBuilt.length ||
    +                                                                                 datasetFullInfo.currentBuildState
    +                                                                                     .aboutToBeBuilt.length ? (
    +                                                                                     <div>
    +                                                                                         <h4 style=\"text-align: center; margin-top: 100px; font-weight: normal\">
    +                                                                                             <p>
    +                                                                                                 此数据集为空.
    +                                                                                                 你应该构建它
    +                                                                                             </p>
    +                                                                                             <a
    +                                                                                                 className=\"btn btn-lab\"
    +                                                                                                 href={datasetHref()}
    +                                                                                                 style=\"padding: 0 15px;\">
    +                                                                                                 <span>
    +                                                                                                     转到源数据集
    +                                                                                                 </span>
    +                                                                                             </a>
    +                                                                                         </h4>
    +                                                                                     </div>
    +                                                                                 ) : null}
    +                                                                                 {datasetFullInfo.currentBuildState
    +                                                                                     .beingBuilt.length ? (
    +                                                                                     <div>
    +                                                                                         <h4 style=\"text-align: center; margin-top: 100px; font-weight: normal\">
    +                                                                                             <p>正在构建此数据集</p>
    +                                                                                             <NavLink
    +                                                                                                 className=\"btn btn-default btn-large\"
    +                                                                                                 to=\"projects.project.jobs.job(datasetFullInfo.currentBuildState.beingBuilt[0])\">
    +                                                                                                 查看任务
    +                                                                                             </NavLink>
    +                                                                                         </h4>
    +                                                                                     </div>
    +                                                                                 ) : null}
    +                                                                                 {datasetFullInfo.currentBuildState
    +                                                                                     .aboutToBeBuilt.length &&
    +                                                                                 !datasetFullInfo.currentBuildState
    +                                                                                     .beingBuilt.length ? (
    +                                                                                     <div>
    +                                                                                         <h4 style=\"text-align: center; margin-top: 100px; font-weight: normal\">
    +                                                                                             <p>该数据集即将构建完毕</p>
    +                                                                                             <NavLink
    +                                                                                                 className=\"btn btn-default btn-large\"
    +                                                                                                 to=\"projects.project.jobs.job(datasetFullInfo.currentBuildState.aboutToBeBuilt[0])\">
    +                                                                                                 查看任务
    +                                                                                             </NavLink>
    +                                                                                         </h4>
    +                                                                                     </div>
    +                                                                                 ) : null}
    +                                                                             </div>
    +                                                                         ) : null}
    +                                                                     </div>
    +                                                                 ) : null}
    + 
    +                                                                 {!shakerState.runError &&
    +                                                                 (table.initialRows > 0 ||
    +                                                                     !datasetFullInfo.buildable ||
    +                                                                     datasetFullInfo.lastBuild !== undefined) ? (
    +                                                                     <div
    +                                                                         className={`positionTable shakerTable coloring-${
    +                                                                             table.coloringScheme
    +                                                                         }`}
    +                                                                         fattable=\"\"
    +                                                                         fattable-data=\"table\"
    +                                                                     />
    +                                                                 ) : null}
    + 
    +                                                                 {pendingRequests.length &&
    +                                                                 spinnerPosition == 'shaker' ? (
    +                                                                     <spinner />
    +                                                                 ) : null}
    +                                                             </div>
    +                                                         ) : null}
    +                                                     </div>
    + 
    +                                                     {/* the use of ng-if here makes it so that long calls are not triggered whenever the sample is refreshed if the column view is not shown (ex: multiColumnAnalysis ) */}
    +                                                     {shakerState.activeView == 'columns' ? (
    +                                                         <div className=\"mainPane h100\" analysis-columns=\"\">
    +                                                             <div
    +                                                                 className=\"h100\"
    +                                                                 include-no-scope=\"/templates/datasets/columns-view.html\"
    +                                                             />
    + 
    +                                                             <div quick-columns-view=\"\" className=\"rightPane\" />
    +                                                         </div>
    +                                                     ) : null}
    +                                                 </i>
    +                                             </div>
    +                                         </div>
    +                                     </div>
    +                                 ) : null}
    +                             </div>
    +                         ) : null}
    +                     </pane>
    +                 </tabs>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;