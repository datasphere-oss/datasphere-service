import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         appConfig.licensedFeatures.advancedMetricsChecksAllowed || appConfig.licensing.ceEntrepriseTrial ? (
    +             <div key=\"child-0\" className=\"metrics h100 vertical-flex\">
    +                 <div className=\"noflex w80\">
    +                     <div style=\"margin: 10px  4px;\">
    +                         <div>
    +                             <div className=\"pull-left controls\">
    +                                 <span>显示</span>
    + 
    +                                 <div className=\"list-customfilter-box view-selector\" style=\"display: inline-block\">
    +                                     <span className=\"add-on\">
    +                                         <i className=\"icon-eye-open\" />
    +                                     </span>
    +                                     <select
    +                                         dku-bs-select=\"{style:'dku-select-button'}\"
    +                                         value={uiState.listMode}
    +                                         ng-options=\"key as value for (key, value) in uiState.values\"
    +                                     />
    +                                 </div>
    + 
    +                                 <span>的</span>
    + 
    +                                 <div
    +                                     displayed-metrics-selector=\"\"
    +                                     choices=\"allComputedChecks\"
    +                                     selected={displayedChecks}
    +                                     refresh-choices=\"refreshAllComputedChecks\"
    +                                     on-close=\"saveMetricsNow\"
    +                                     type=\"checks\"
    +                                 />
    + 
    +                                 {metricsCallbacks.isPartitioned() ? <span>在</span> : null}
    + 
    +                                 {metricsCallbacks.isPartitioned() ? (
    +                                     <div className=\"list-customfilter-box\" style=\"display: inline-block\">
    +                                         <select
    +                                             dku-bs-select=\"{liveSearch:true,size:'auto', 'style':'dku-select-button'}\"
    +                                             value={metricsChecks.displayedState.partition}
    +                                             ng-options=\"p as (p === 'ALL' ? 'Whole dataset': p) for p in metricsPartitionsIds\"
    +                                         />
    +                                         <a
    +                                             onClick={() => {
    +                                                 refreshMetricsPartitions();
    +                                             }}>
    +                                             <span className=\"right-add-on\">
    +                                                 <i className=\"{'icon-refresh': true, 'icon-spin': refreshing}\" />
    +                                             </span>
    +                                         </a>
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 {canCompute ? (
    +                                     <div className=\"list-control-widget\">
    +                                         <button
    +                                             className=\"btn btn-default\"
    +                                             onClick={() => {
    +                                                 computeNow();
    +                                             }}>
    +                                             计算
    +                                         </button>
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 {lastComputeResult ? (
    +                                     <div
    +                                         contextual-menu=\"\"
    +                                         cep-position=\"align-right-top\"
    +                                         onClick={(event: React.SyntheticEvent<HTMLElement>) => {
    +                                             toggleContextualMenu(event);
    +                                         }}
    +                                         className=\"list-control-widget\"
    +                                         style=\"cursor:pointer; position:relative; display: inline-block;\">
    +                                         <span>
    +                                             {hasErrors ? (
    +                                                 <i className=\"icon-warning-sign\" style=\"color: #b94a48;\" />
    +                                             ) : null}
    +                                             最后运行结果 <i className=\"icon-arrow-right \" />
    +                                         </span>
    + 
    +                                         {contextualMenu ? (
    +                                             <div
    +                                                 className=\"contextualMenu last-metrics-run-results oa\"
    +                                                 style=\"z-index: 20;\">
    +                                                 <div>
    +                                                     开始 {date(lastComputeResult.startTime, 'yyyy/MM/dd HH:mm')},
    +                                                     finished {date(lastComputeResult.endTime, 'yyyy/MM/dd HH:mm')}
    +                                                 </div>
    + 
    +                                                 {lastComputeResult.runs ? (
    +                                                     <div>
    +                                                         <div>计算 {lastComputeResult.results.length} 检查值</div>
    +                                                         {lastComputeResult.runs.map((run, index: number) => {
    +                                                             return (
    +                                                                 <div key={`item-${index}`}>
    +                                                                     <div>Check : {run.name}</div>
    +                                                                     {run.error ? (
    +                                                                         <div className=\"alert alert-error\">
    +                                                                             <div>
    +                                                                                 <i className=\"icon-warning-sign\">
    +                                                                                     &nbsp;{run.error.clazz} :{' '}
    +                                                                                     <span className=\"preserve-eol\">
    +                                                                                         {run.error.message}
    +                                                                                     </span>
    +                                                                                     <div
    +                                                                                         onClick={() => {
    +                                                                                             run.expandError = !run.expandError;
    +                                                                                         }}>
    +                                                                                         <i className=\"icon-eye\" />&nbsp;堆栈跟踪
    +                                                                                     </div>
    +                                                                                 </i>
    +                                                                             </div>
    +                                                                             <i className=\"icon-warning-sign\">
    +                                                                                 {run.expandError ? (
    +                                                                                     <div className=\"error-info preserve-eol\">
    +                                                                                         {run.error.stack}
    +                                                                                     </div>
    +                                                                                 ) : null}
    +                                                                             </i>
    +                                                                         </div>
    +                                                                     ) : null}
    +                                                                     <i className=\"icon-warning-sign\">
    +                                                                         {run.logTail != null &&
    +                                                                         run.logTail.lines.length > 0 ? (
    +                                                                             <div className=\"alert alert-info\">
    +                                                                                 <div
    +                                                                                     onClick={() => {
    +                                                                                         run.expandLog = !run.expandLog;
    +                                                                                     }}>
    +                                                                                     <i className=\"icon-eye\" />&nbsp;日志
    +                                                                                 </div>
    +                                                                                 {run.expandLog ? (
    +                                                                                     <div className=\"error-info\">
    +                                                                                         {run.logTail.lines.map(
    +                                                                                             (line, index: number) => {
    +                                                                                                 return (
    +                                                                                                     <div
    +                                                                                                         key={`item-${index}`}>
    +                                                                                                         {line}
    +                                                                                                     </div>
    +                                                                                                 );
    +                                                                                             }
    +                                                                                         )}
    +                                                                                     </div>
    +                                                                                 ) : null}
    +                                                                             </div>
    +                                                                         ) : null}
    +                                                                     </i>
    +                                                                 </div>
    +                                                             );
    +                                                         })}
    +                                                         <i className=\"icon-warning-sign\" />
    +                                                     </div>
    +                                                 ) : null}
    +                                                 <i className=\"icon-warning-sign\">
    +                                                     {!lastComputeResult.runs ? (
    +                                                         <div>
    +                                                             <div>
    +                                                                 计算 {lastComputeResult.partitionsList.length}{' '}
    +                                                                 partitions in {lastComputeResult.allRuns.length} runs ({
    +                                                                     lastComputeResult.errorRuns.length
    +                                                                 }{' '}
    +                                                                 错误)
    +                                                             </div>
    +                                                             {lastComputeResult.errorRuns.map((run, index: number) => {
    +                                                                 return (
    +                                                                     <div key={`item-${index}`}>
    +                                                                         <div>检查 : {run.name}</div>
    +                                                                         {run.error ? (
    +                                                                             <div className=\"alert alert-error\">
    +                                                                                 <div>
    +                                                                                     <i className=\"icon-warning-sign\">
    +                                                                                         &nbsp;{run.error.clazz} :{' '}
    +                                                                                         <span className=\"preserve-eol\">
    +                                                                                             {run.error.message}
    +                                                                                         </span>
    +                                                                                         <div
    +                                                                                             onClick={() => {
    +                                                                                                 run.expandError = !run.expandError;
    +                                                                                             }}>
    +                                                                                             <i className=\"icon-eye\" />&nbsp;堆栈跟踪
    +                                                                                         </div>
    +                                                                                     </i>
    +                                                                                 </div>
    +                                                                                 <i className=\"icon-warning-sign\">
    +                                                                                     {run.expandError ? (
    +                                                                                         <div className=\"error-info preserve-eol\">
    +                                                                                             {run.error.stack}
    +                                                                                         </div>
    +                                                                                     ) : null}
    +                                                                                 </i>
    +                                                                             </div>
    +                                                                         ) : null}
    +                                                                         <i className=\"icon-warning-sign\">
    +                                                                             {run.logTail != null &&
    +                                                                             run.logTail.lines.length > 0 ? (
    +                                                                                 <div className=\"alert alert-info\">
    +                                                                                     <div
    +                                                                                         onClick={() => {
    +                                                                                             run.expandLog = !run.expandLog;
    +                                                                                         }}>
    +                                                                                         <i className=\"icon-eye\" />&nbsp;日志
    +                                                                                     </div>
    +                                                                                     {run.expandLog ? (
    +                                                                                         <div className=\"error-info\">
    +                                                                                             {run.logTail.lines.map(
    +                                                                                                 (
    +                                                                                                     line,
    +                                                                                                     index: number
    +                                                                                                 ) => {
    +                                                                                                     return (
    +                                                                                                         <div
    +                                                                                                             key={`item-${index}`}>
    +                                                                                                             {line}
    +                                                                                                         </div>
    +                                                                                                     );
    +                                                                                                 }
    +                                                                                             )}
    +                                                                                         </div>
    +                                                                                     ) : null}
    +                                                                                 </div>
    +                                                                             ) : null}
    +                                                                         </i>
    +                                                                     </div>
    +                                                                 );
    +                                                             })}
    +                                                             <i className=\"icon-warning-sign\" />
    +                                                         </div>
    +                                                     ) : null}
    +                                                     <i className=\"icon-warning-sign\" />
    +                                                 </i>
    +                                             </div>
    +                                         ) : null}
    +                                         <i className=\"icon-warning-sign\">
    +                                             <i className=\"icon-warning-sign\" />
    +                                         </i>
    +                                     </div>
    +                                 ) : null}
    +                                 <i className=\"icon-warning-sign\">
    +                                     <i className=\"icon-warning-sign\" />
    +                                 </i>
    +                             </div>
    +                             <i className=\"icon-warning-sign\">
    +                                 <i className=\"icon-warning-sign\">
    +                                     <div className=\"pull-right\">
    +                                         {metricsCallbacks.isPartitioned() ||
    +                                         (allComputedChecks &&
    +                                             allComputedChecks.notExistingViews &&
    +                                             allComputedChecks.notExistingViews.indexOf('CHECKS_HISTORY') >= 0) ? (
    +                                             <div className=\"computed-metrics-buttons\">
    +                                                 <div
    +                                                     custom-element-popup=\"\"
    +                                                     cep-position=\"align-right-bottom\"
    +                                                     close-on-click=\"true\">
    +                                                     <i
    +                                                         className=\"icon-cog mainzone cursor-pointer\"
    +                                                         onClick={() => {
    +                                                             togglePopover();
    +                                                         }}
    +                                                     />
    +                                                     <ul className=\"popover custom-element-popup-popover dropdown-menu\">
    +                                                         {metricsScope != 'PROJECT' &&
    +                                                         allComputedChecks &&
    +                                                         allComputedChecks.notExistingViews &&
    +                                                         allComputedChecks.notExistingViews.indexOf('CHECKS_HISTORY') >=
    +                                                             0 ? (
    +                                                             <li>
    +                                                                 <a
    +                                                                     onClick={() => {
    +                                                                         addAllChecksDatasetInFlow(
    +                                                                             'CHECKS_HISTORY',
    +                                                                             null,
    +                                                                             null
    +                                                                         );
    +                                                                     }}>
    +                                                                     从检查数据中创建数据集{' '}
    +                                                                 </a>
    +                                                             </li>
    +                                                         ) : null}
    +                                                         {metricsCallbacks.saveExternalChecksValues ? (
    +                                                             <li>
    +                                                                 <a
    +                                                                     onClick={() => {
    +                                                                         addCheckValue();
    +                                                                     }}>
    +                                                                     添加一个检查值{' '}
    +                                                                 </a>
    +                                                             </li>
    +                                                         ) : null}
    +                                                         {metricsCallbacks.isPartitioned() ? (
    +                                                             <li>
    +                                                                 <a
    +                                                                     onClick={() => {
    +                                                                         computeAll();
    +                                                                     }}>
    +                                                                     计算所有分区的所有检查{' '}
    +                                                                 </a>
    +                                                             </li>
    +                                                         ) : null}
    +                                                         {uiState.listMode == 'list' ? (
    +                                                             <li>
    +                                                                 <a
    +                                                                     onClick={() => {
    +                                                                         exportTable();
    +                                                                     }}>
    +                                                                     导出表
    +                                                                 </a>
    +                                                             </li>
    +                                                         ) : null}
    +                                                     </ul>
    +                                                 </div>
    +                                             </div>
    +                                         ) : null}
    +                                     </div>
    + 
    +                                     <div style=\"clear: both;\" />
    +                                 </i>
    +                             </i>
    +                         </div>
    +                         <i className=\"icon-warning-sign\">
    +                             <i className=\"icon-warning-sign\">
    +                                 {uiState.listMode === 'banner' && displayedChecks.checks.length ? (
    +                                     <div style=\"margin: 10px 0 20px 0;\">
    +                                         <div
    +                                             time-range-brush=\"\"
    +                                             range=\"displayedChecksRange\"
    +                                             selected-range=\"selectedRange\"
    +                                             on-change=\"brushChanged()\"
    +                                         />
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 {computing ? (
    +                                     <span>
    +                                         <i className=\"icon-refresh icon-spin\" />
    +                                     </span>
    +                                 ) : null}
    +                             </i>
    +                         </i>
    +                     </div>
    +                     <i className=\"icon-warning-sign\">
    +                         <i className=\"icon-warning-sign\" />
    +                     </i>
    +                 </div>
    +                 <i className=\"icon-warning-sign\">
    +                     <i className=\"icon-warning-sign\">
    +                         <div className=\"flex oa metrics-plots\">
    +                             <div className=\"fh\">
    +                                 <div className=\"w80 h100\">
    +                                     {!displayedChecks.checks.length && allComputedChecks.checks.length ? (
    +                                         <div className=\"centered-info\">
    +                                             <p>无检查显示</p>
    +                                             <p className=\"small\">
    +                                                 <a
    +                                                     onClick={() => {
    +                                                         openDisplayedMetricsModal();
    +                                                     }}>
    +                                                     添加
    +                                                 </a>
    +                                             </p>
    +                                         </div>
    +                                     ) : null}
    + 
    +                                     {!displayedChecks.checks.length && !allComputedChecks.checks.length ? (
    +                                         <div className=\"centered-info\">
    +                                             <p>无 {metricsScope == 'PROJECT' ? <span>工程级别的</span> : null} 检查</p>
    +                                             {metricsScope == 'PROJECT' ? (
    +                                                 <p className=\"small\">工程级别的指标通过Python代码创建.</p>
    +                                             ) : null}
    +                                             {/* saved models don't have the settings pages in the same place (relative to here) */}
    +                                             {!isInSavedModel && !hasNoMetricsSettings ? (
    +                                                 <p className=\"small\">
    +                                                     创建在{' '}
    +                                                     <NavLink to=\"^.settings({selectedTab:'checks'})\">管理界面</NavLink>
    +                                                 </p>
    +                                             ) : null}
    +                                             {isInSavedModel && !hasNoMetricsSettings ? (
    +                                                 <p className=\"small\">
    +                                                     创建在{' '}
    +                                                     <a
    +                                                         href={
    +                                                             $state.href(
    +                                                                 'projects.project.savedmodels.savedmodel.settings'
    +                                                             ) + '#status-checks'
    +                                                         }>
    +                                                         管理界面
    +                                                     </a>
    +                                                 </p>
    +                                             ) : null}
    +                                         </div>
    +                                     ) : null}
    + 
    +                                     {uiState.listMode === 'banner' && displayedChecks.checks.length ? (
    +                                         <div className=\"fh\">
    +                                             {displayedChecks.checks.map((displayedCheck, index: number) => {
    +                                                 return (
    +                                                     <div key={`item-${index}`}>
    +                                                         <div
    +                                                             check-banner=\"\"
    +                                                             displayed-data=\"getDisplayedData(displayedCheck)\"
    +                                                             displayed-check=\"displayedCheck\"
    +                                                             displayed-range=\"selectedRange\"
    +                                                         />
    +                                                     </div>
    +                                                 );
    +                                             })}
    +                                         </div>
    +                                     ) : null}
    +                                     {uiState.listMode === 'list' && displayedChecks.checks.length ? (
    +                                         <div className=\"last-checks-table h100\">
    +                                             <table className=\"table-small table-striped table-hover\">
    +                                                 <thead>
    +                                                     <tr>
    +                                                         <th style=\"width: 20%\">检查</th>
    +                                                         <th style=\"width: 30%\">信息</th>
    +                                                         <th style=\"width: 10%\">运行</th>
    +                                                         <th style=\"width: 10%\">最后运行</th>
    +                                                         <th style=\"width: 30%\">消息</th>
    +                                                         <th style=\"width: 10%\">状态</th>
    +                                                     </tr>
    +                                                 </thead>
    +                                                 <tbody>
    +                                                     {displayedChecksHistories.map((checkHistory, index: number) => {
    +                                                         return (
    +                                                             <tr key={`item-${index}`}>
    +                                                                 <td>{checkHistory.name}</td>
    +                                                                 <td className=\"info\">
    +                                                                     {getNiceInfo(checkHistory.check)}
    +                                                                 </td>
    +                                                                 <td>{checkHistory.values.length}</td>
    +                                                                 <td>
    +                                                                     {date(
    +                                                                         checkHistory.lastValue.time,
    +                                                                         'yyyy-MM-dd – HH:mm'
    +                                                                     )}
    +                                                                 </td>
    +                                                                 <td>{checkHistory.lastValue.message}</td>
    +                                                                 <td
    +                                                                     className={`outcome-${checkHistory.lastValue.outcome.toLowerCase()} outcome-cell`}
    +                                                                     ng-init=\"outcome = checkHistory.lastValue.outcome;\">
    +                                                                     <i />
    +                                                                     {/* 				<i ng-if=\"outcome == 'ERROR'\" class=\"icon-remove\"></i> */}
    +                                                                     {/* 				<i ng-if=\"outcome == 'OK'\" class=\"icon-check\"></i> */}
    +                                                                     {/* 				<i ng-if=\"outcome == 'WARNING'\" class=\"icon-danger\"></i> */}
    +                                                                     <span>{outcome}</span>
    +                                                                 </td>
    +                                                             </tr>
    +                                                         );
    +                                                     })}
    +                                                 </tbody>
    +                                             </table>
    +                                         </div>
    +                                     ) : null}
    +                                 </div>
    +                             </div>
    +                         </div>
    +                     </i>
    +                 </i>
    +             </div>
    +         ) : null,
    +         <i key=\"child-1\" className=\"icon-warning-sign\">
    +             <i className=\"icon-warning-sign\">
    +                 {!(appConfig.licensedFeatures.advancedMetricsChecksAllowed || appConfig.licensing.ceEntrepriseTrial) ? (
    +                     <FeatureLocked feature-name=\"'Automatic status checks'\" />
    +                 ) : null}
    +             </i>
    +         </i>
    +     ];
    + };
    + 
    + export default TestComponent;