import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div job-preview=\"\" className=\"page-top-padding job-preview h100 vertical-flex\">
    +             <div block-api-error=\"\">
    +                 {/* Header, common to all tabs */}
    +                 <div className=\"noflex row-fluid small-lr-padding\">
    +                     <div className=\"span5\">
    +                         <h4>
    +                             <i className=\"icon-dku-search\">任务预览</i>
    +                         </h4>
    +                         <i className=\"icon-dku-search\" />
    +                     </div>
    +                     <i className=\"icon-dku-search\">
    +                         <div className=\"span5\">
    +                             {disabledActivities.length == 0 ? (
    +                                 <button
    +                                     className=\"btn btn-success\"
    +                                     onClick={() => {
    +                                         validateRun();
    +                                     }}>
    +                                     <i className=\"icon-play\">&nbsp;运行</i>
    +                                 </button>
    +                             ) : null}
    +                             <i className=\"icon-play\">
    +                                 {disabledActivities.length > 0 ? (
    +                                     <button
    +                                         className=\"btn btn-success\"
    +                                         onClick={() => {
    +                                             validateRun();
    +                                         }}>
    +                                         <i className=\"icon-play\">&nbsp;运行</i>
    +                                     </button>
    +                                 ) : null}
    +                                 <i className=\"icon-play\">
    +                                     <button
    +                                         className=\"btn btn-danger\"
    +                                         onClick={() => {
    +                                             abortJob(jobDef.id);
    +                                         }}>
    +                                         放弃
    +                                     </button>
    +                                     {disabledActivities.length > 0 ? (
    +                                         <small>
    +                                             {disabledActivities.length} / {jobPreviewResultFlat.length} 活动禁用.{' '}
    +                                             <a
    +                                                 onClick={() => {
    +                                                     disabledActivities.splice(0, disabledActivities.length);
    +                                                 }}>
    +                                                 重新启用所有
    +                                             </a>
    +                                         </small>
    +                                     ) : null}
    +                                 </i>
    +                             </i>
    +                         </div>
    +                         <i className=\"icon-play\">
    +                             <i className=\"icon-play\" />
    +                         </i>
    +                     </i>
    +                 </div>
    +                 <i className=\"icon-dku-search\">
    +                     <i className=\"icon-play\">
    +                         <i className=\"icon-play\">
    +                             {/* Summary tab */}
    +                             {topNav.tab == 'summary' ? (
    +                                 <div className=\"flex\">
    +                                     <div className=\"fh oa small-lr-padding page-top-padding\">
    +                                         <div className=\"row-fluid\">
    +                                             <div className=\"span6\">
    +                                                 <h5>任务概要</h5>
    +                                                 <div
    +                                                     user-picture=\"jobStatus.initiator.login\"
    +                                                     size=\"32\"
    +                                                     style=\"float:left\"
    +                                                 />
    +                                                 <ul style=\"list-style: none; float: left\">
    +                                                     <li>任务创建于 {jobStatus.initiator.displayName}</li>
    +                                                     <li>
    +                                                         任务开始于{' '}
    +                                                         {date(jobStatus.baseStatus.jobStartTime, 'yyyy/MM/dd-HH:mm:ss')}
    +                                                     </li>
    +                                                     {jobStatus.baseStatus.jobEndTime ? (
    +                                                         <li>
    +                                                             任务结束于{' '}
    +                                                             {date(
    +                                                                 jobStatus.baseStatus.jobEndTime,
    +                                                                 'yyyy/MM/dd-HH:mm:ss'
    +                                                             )}
    +                                                         </li>
    +                                                     ) : null}
    +                                                 </ul>
    +                                             </div>
    +                                             <div className=\"span6\">
    +                                                 <h5>时间</h5>
    +                                                 <ul style=\"list-style: none\">
    +                                                     {jobStatus.baseStatus.resolveDuration &&
    +                                                     jobState != 'COMPUTING_DEPS' ? (
    +                                                         <li>
    +                                                             依赖计算在{' '}
    +                                                             {durationHHMMSS(jobStatus.baseStatus.resolveDuration)}
    +                                                         </li>
    +                                                     ) : null}
    +                                                 </ul>
    +                                             </div>
    +                                         </div>
    +                                         <div className=\"row-fluid\">
    +                                             <div className=\"span6\">
    +                                                 <h5>活动</h5>
    +                                                 <p>任务包含 {jobStatus.globalState.tota} 活动</p>
    +                                             </div>
    +                                         </div>
    +                                     </div>
    +                                 </div>
    +                             ) : null}
    + 
    +                             {/* Activities tab */}
    +                             {topNav.tab == 'activities' && jobState == 'WAITING_CONFIRMATION' ? (
    +                                 <div className=\"flex row-fluid job-preview\" job-preview-list=\"\">
    +                                     <div className=\"fh oa small-lr-padding\">
    +                                         <div className=\"h100 span5 offset0\">
    +                                             <div style=\"padding: 10px 0 0\" className=\"h100 vertical-flex activity-list\">
    +                                                 <div
    +                                                     className=\"noflex\"
    +                                                     include-no-scope=\"/templates/jobs/activity-table-sort-filter.html\">
    +                                                     <div className=\"flex\">
    +                                                         <div
    +                                                             fat-repeat=\"jobPreviewResultTable\"
    +                                                             as=\"activity\"
    +                                                             className=\"fh\"
    +                                                             row-height=\"35\">
    +                                                             <div
    +                                                                 className=\"{'selected':  activity == selectedActivity, 'disabled' : disabledActivities.indexOf(activity.activityId) >= 0}\"
    +                                                                 onClick={() => {
    +                                                                     selectActivity(activity);
    +                                                                 }}>
    +                                                                 {activity.activityType == 'RECIPE' ? (
    +                                                                     <div style=\"width: calc((100% - 10px)*0.4); display: inline-block;\">
    +                                                                         <i
    +                                                                             className={recipeTypeToIcon(
    +                                                                                 activity.recipeType
    +                                                                             )}>
    +                                                                             {activity.recipeName}
    +                                                                         </i>
    +                                                                     </div>
    +                                                                 ) : null}
    +                                                                 <i className={recipeTypeToIcon(activity.recipeType)}>
    +                                                                     {activity.activityType == 'SPARK_PIPELINE' ? (
    +                                                                         <div style=\"width: calc((100% - 10px)*0.4); display: inline-block;\">
    +                                                                             Spark 处理 ({
    +                                                                                 activity.pipelineRecipes.length
    +                                                                             }{' '}
    +                                                                             活动)
    +                                                                         </div>
    +                                                                     ) : null}
    + 
    +                                                                     <div style=\"width: calc((100% - 10px)*0.4); display: inline-block;\">
    +                                                                         {activity.targets[0].datasetName}
    +                                                                     </div>
    +                                                                     <div
    +                                                                         style=\"width: calc((100% - 10px)*0.2); display: inline-block;\"
    +                                                                         title=\"main partition\"
    +                                                                         visible-if=\"activity.mainPartition != 'N/A'\">
    +                                                                         {activity.mainPartition}
    +                                                                     </div>
    +                                                                 </i>
    +                                                             </div>
    +                                                             <i className={recipeTypeToIcon(activity.recipeType)} />
    +                                                         </div>
    +                                                         <i className={recipeTypeToIcon(activity.recipeType)} />
    +                                                     </div>
    +                                                     <i className={recipeTypeToIcon(activity.recipeType)} />
    +                                                 </div>
    +                                                 <i className={recipeTypeToIcon(activity.recipeType)} />
    +                                             </div>
    +                                             <i className={recipeTypeToIcon(activity.recipeType)}>
    +                                                 {selectedActivity ? (
    +                                                     <div
    +                                                         className=\"h100 span7 offset0\"
    +                                                         style=\"padding-left: 15px; border-sizing: border-box; padding-right: 15px;\">
    +                                                         <div className=\"h100 vertical-flex\">
    +                                                             <div
    +                                                                 className=\"noflex\"
    +                                                                 style=\"min-height: 26px;margin:10px 0\">
    +                                                                 <div className=\"actions pull-right\">
    +                                                                     {disabledActivities.indexOf(
    +                                                                         selectedActivity.activityId
    +                                                                     ) >= 0 ? (
    +                                                                         <div className=\"btn-group\">
    +                                                                             <button
    +                                                                                 className=\"btn btn-success\"
    +                                                                                 onClick={() => {
    +                                                                                     enableActivity(selectedActivity);
    +                                                                                 }}>
    +                                                                                 启用
    +                                                                             </button>
    +                                                                             <button
    +                                                                                 className=\"btn btn-success dropdown-toggle\"
    +                                                                                 data-toggle=\"dropdown\">
    +                                                                                 <span className=\"caret\" />
    +                                                                             </button>
    +                                                                             <ul className=\"dropdown-menu pull-right\">
    +                                                                                 <li>
    +                                                                                     <a
    +                                                                                         onClick={() => {
    +                                                                                             disableAllForRecipe(
    +                                                                                                 selectedActivity.recipeName
    +                                                                                             );
    +                                                                                         }}>
    +                                                                                         为此组件禁用所有活动
    +                                                                                     </a>
    +                                                                                 </li>
    +                                                                                 <li>
    +                                                                                     <a
    +                                                                                         onClick={() => {
    +                                                                                             enableAllForRecipe(
    +                                                                                                 selectedActivity.recipeName
    +                                                                                             );
    +                                                                                         }}>
    +                                                                                         为此组件启用所有活动
    +                                                                                     </a>
    +                                                                                 </li>
    +                                                                             </ul>
    +                                                                         </div>
    +                                                                     ) : null}
    +                                                                     {disabledActivities.indexOf(
    +                                                                         selectedActivity.activityId
    +                                                                     ) < 0 ? (
    +                                                                         <div className=\"btn-group\">
    +                                                                             <button
    +                                                                                 className=\"btn btn-danger\"
    +                                                                                 onClick={() => {
    +                                                                                     disableActivity(selectedActivity);
    +                                                                                 }}>
    +                                                                                 禁用
    +                                                                             </button>
    +                                                                             <button
    +                                                                                 className=\"btn btn-danger dropdown-toggle\"
    +                                                                                 data-toggle=\"dropdown\">
    +                                                                                 <span className=\"caret\" />
    +                                                                             </button>
    +                                                                             <ul className=\"dropdown-menu pull-right\">
    +                                                                                 <li>
    +                                                                                     <a
    +                                                                                         onClick={() => {
    +                                                                                             disableAllForRecipe(
    +                                                                                                 selectedActivity.recipeName
    +                                                                                             );
    +                                                                                         }}>
    +                                                                                         为此组件禁用所有活动
    +                                                                                     </a>
    +                                                                                 </li>
    +                                                                                 <li>
    +                                                                                     <a
    +                                                                                         onClick={() => {
    +                                                                                             enableAllForRecipe(
    +                                                                                                 selectedActivity.recipeName
    +                                                                                             );
    +                                                                                         }}>
    +                                                                                         为此组件启用所有活动
    +                                                                                     </a>
    +                                                                                 </li>
    +                                                                             </ul>
    +                                                                         </div>
    +                                                                     ) : null}
    +                                                                 </div>
    +                                                                 <h4>
    +                                                                     <i
    +                                                                         className={typeToIcon(
    +                                                                             selectedActivity.recipeType
    +                                                                         )}>
    +                                                                         {selectedActivity.recipeName}
    +                                                                         {selectedActivity.mainPartition ? (
    +                                                                             <span>
    +                                                                                 ({selectedActivity.mainPartition})
    +                                                                             </span>
    +                                                                         ) : null}
    +                                                                     </i>
    +                                                                 </h4>
    +                                                                 <i
    +                                                                     className={typeToIcon(selectedActivity.recipeType)}
    +                                                                 />
    +                                                             </div>
    +                                                             <i className={typeToIcon(selectedActivity.recipeType)}>
    +                                                                 {selectedActivity.requiredReason.type ? (
    +                                                                     <div className=\"noflex alert alert-info\">
    +                                                                         <span
    +                                                                             activity-required-reason=\"\"
    +                                                                             activity=\"selectedActivity\"
    +                                                                             sentence=\"true\"
    +                                                                             past=\"false\"
    +                                                                         />
    +                                                                     </div>
    +                                                                 ) : null}
    + 
    +                                                                 <div className=\"flex activity-details\">
    +                                                                     <div className=\"fh oa\">
    +                                                                         <div className=\"row-fluid\">
    +                                                                             <div className=\"span6\">
    +                                                                                 <h5>依赖</h5>
    +                                                                                 {selectedActivity.dependencies.length ==
    +                                                                                 0 ? (
    +                                                                                     <p>此活动不依赖其他活动.</p>
    +                                                                                 ) : null}
    +                                                                                 <ul>
    +                                                                                     {selectedActivity.dependencies
    +                                                                                         .sort('toString()')
    +                                                                                         .uniqueStrings()
    +                                                                                         .map(
    +                                                                                             (
    +                                                                                                 depends,
    +                                                                                                 index: number
    +                                                                                             ) => {
    +                                                                                                 return (
    +                                                                                                     <li
    +                                                                                                         key={`item-${index}`}
    +                                                                                                         className=\"mx-textellipsis\"
    +                                                                                                         ng-scope=\"\">
    +                                                                                                         <i
    +                                                                                                             className={typeToIcon(
    +                                                                                                                 depended.recipeType
    +                                                                                                             )}>
    +                                                                                                             <a
    +                                                                                                                 onClick={() => {
    +                                                                                                                     selectActivity(
    +                                                                                                                         depended
    +                                                                                                                     );
    +                                                                                                                 }}
    +                                                                                                                 title={
    +                                                                                                                     depends
    +                                                                                                                 }>
    +                                                                                                                 {
    +                                                                                                                     depends
    +                                                                                                                 }
    +                                                                                                             </a>
    +                                                                                                         </i>
    +                                                                                                     </li>
    +                                                                                                 );
    +                                                                                             }
    +                                                                                         )}
    +                                                                                     <i
    +                                                                                         className={typeToIcon(
    +                                                                                             depended.recipeType
    +                                                                                         )}
    +                                                                                     />
    +                                                                                 </ul>
    +                                                                                 <i
    +                                                                                     className={typeToIcon(
    +                                                                                         depended.recipeType
    +                                                                                     )}
    +                                                                                 />
    +                                                                             </div>
    +                                                                             <i
    +                                                                                 className={typeToIcon(
    +                                                                                     depended.recipeType
    +                                                                                 )}>
    +                                                                                 <div className=\"span6\">
    +                                                                                     <h5>需要通过</h5>
    +                                                                                     {!jobPreviewResult.reverseDeps[
    +                                                                                         selectedActivity.activityId
    +                                                                                     ] ? (
    +                                                                                         <p>无活动依赖此任务.</p>
    +                                                                                     ) : null}
    +                                                                                     <ul>
    +                                                                                         {jobPreviewResult.reverseDeps[
    +                                                                                             selectedActivity.activityId
    +                                                                                         ]
    +                                                                                             .sort('toString()')
    +                                                                                             .uniqueStrings()
    +                                                                                             .map(
    +                                                                                                 (
    +                                                                                                     depends,
    +                                                                                                     index: number
    +                                                                                                 ) => {
    +                                                                                                     return (
    +                                                                                                         <li
    +                                                                                                             key={`item-${index}`}
    +                                                                                                             className=\"mx-textellipsis\"
    +                                                                                                             ng-scope=\"\">
    +                                                                                                             <i
    +                                                                                                                 className={typeToIcon(
    +                                                                                                                     depended.recipeType
    +                                                                                                                 )}>
    +                                                                                                                 <a
    +                                                                                                                     onClick={() => {
    +                                                                                                                         selectActivity(
    +                                                                                                                             depended
    +                                                                                                                         );
    +                                                                                                                     }}
    +                                                                                                                     title={
    +                                                                                                                         depends
    +                                                                                                                     }>
    +                                                                                                                     {
    +                                                                                                                         depends
    +                                                                                                                     }
    +                                                                                                                 </a>
    +                                                                                                             </i>
    +                                                                                                         </li>
    +                                                                                                     );
    +                                                                                                 }
    +                                                                                             )}
    +                                                                                         <i
    +                                                                                             className={typeToIcon(
    +                                                                                                 depended.recipeType
    +                                                                                             )}
    +                                                                                         />
    +                                                                                     </ul>
    +                                                                                     <i
    +                                                                                         className={typeToIcon(
    +                                                                                             depended.recipeType
    +                                                                                         )}
    +                                                                                     />
    +                                                                                 </div>
    +                                                                                 <i
    +                                                                                     className={typeToIcon(
    +                                                                                         depended.recipeType
    +                                                                                     )}
    +                                                                                 />
    +                                                                             </i>
    +                                                                         </div>
    +                                                                         <i className={typeToIcon(depended.recipeType)}>
    +                                                                             <i
    +                                                                                 className={typeToIcon(
    +                                                                                     depended.recipeType
    +                                                                                 )}>
    +                                                                                 <div className=\"row-fluid\">
    +                                                                                     <div className=\"span6\">
    +                                                                                         <h5>输入</h5>
    +                                                                                         {selectedActivity.sources
    +                                                                                             .length == 0 ? (
    +                                                                                             <p>此活动无输入.</p>
    +                                                                                         ) : null}
    +                                                                                         <ul>
    +                                                                                             {selectedActivity.sources.map(
    +                                                                                                 (
    +                                                                                                     source,
    +                                                                                                     index: number
    +                                                                                                 ) => {
    +                                                                                                     return (
    +                                                                                                         <li
    +                                                                                                             key={`item-${index}`}>
    +                                                                                                             {
    +                                                                                                                 source.datasetName
    +                                                                                                             }
    +                                                                                                             <ul>
    +                                                                                                                 {partition !=
    +                                                                                                                 'NP'
    +                                                                                                                     ? source.partitionIds
    +                                                                                                                           .sort(
    +                                                                                                                               'toString()'
    +                                                                                                                           )
    +                                                                                                                           .map(
    +                                                                                                                               (
    +                                                                                                                                   partition,
    +                                                                                                                                   index: number
    +                                                                                                                               ) => {
    +                                                                                                                                   return (
    +                                                                                                                                       <li
    +                                                                                                                                           key={`item-${index}`}>
    +                                                                                                                                           {
    +                                                                                                                                               partition
    +                                                                                                                                           }
    +                                                                                                                                       </li>
    +                                                                                                                                   );
    +                                                                                                                               }
    +                                                                                                                           )
    +                                                                                                                     : null}
    +                                                                                                             </ul>
    +                                                                                                         </li>
    +                                                                                                     );
    +                                                                                                 }
    +                                                                                             )}
    +                                                                                         </ul>
    +                                                                                     </div>
    +                                                                                     <div className=\"span6\">
    +                                                                                         <h5>输出</h5>
    +                                                                                         {selectedActivity.targets
    +                                                                                             .length == 0 ? (
    +                                                                                             <p>此活动无输出.</p>
    +                                                                                         ) : null}
    +                                                                                         <ul>
    +                                                                                             {selectedActivity.targets.map(
    +                                                                                                 (
    +                                                                                                     target,
    +                                                                                                     index: number
    +                                                                                                 ) => {
    +                                                                                                     return (
    +                                                                                                         <li
    +                                                                                                             key={`item-${index}`}>
    +                                                                                                             {
    +                                                                                                                 target.datasetName
    +                                                                                                             }
    +                                                                                                             {target.partitionId !=
    +                                                                                                             'NP' ? (
    +                                                                                                                 <span>
    +                                                                                                                     ({
    +                                                                                                                         target.partitionId
    +                                                                                                                     })
    +                                                                                                                 </span>
    +                                                                                                             ) : null}
    +                                                                                                         </li>
    +                                                                                                     );
    +                                                                                                 }
    +                                                                                             )}
    +                                                                                         </ul>
    +                                                                                     </div>
    +                                                                                 </div>
    +                                                                             </i>
    +                                                                         </i>
    +                                                                     </div>
    +                                                                     <i className={typeToIcon(depended.recipeType)}>
    +                                                                         <i
    +                                                                             className={typeToIcon(depended.recipeType)}
    +                                                                         />
    +                                                                     </i>
    +                                                                 </div>
    +                                                                 <i className={typeToIcon(depended.recipeType)}>
    +                                                                     <i className={typeToIcon(depended.recipeType)} />
    +                                                                 </i>
    +                                                             </i>
    +                                                         </div>
    +                                                         <i className={typeToIcon(selectedActivity.recipeType)}>
    +                                                             <i className={typeToIcon(depended.recipeType)}>
    +                                                                 <i className={typeToIcon(depended.recipeType)} />
    +                                                             </i>
    +                                                         </i>
    +                                                     </div>
    +                                                 ) : null}
    +                                                 <i className={typeToIcon(selectedActivity.recipeType)}>
    +                                                     <i className={typeToIcon(depended.recipeType)}>
    +                                                         <i className={typeToIcon(depended.recipeType)} />
    +                                                     </i>
    +                                                 </i>
    +                                             </i>
    +                                         </div>
    +                                         <i className={recipeTypeToIcon(activity.recipeType)}>
    +                                             <i className={typeToIcon(selectedActivity.recipeType)}>
    +                                                 <i className={typeToIcon(depended.recipeType)}>
    +                                                     <i className={typeToIcon(depended.recipeType)} />
    +                                                 </i>
    +                                             </i>
    +                                         </i>
    +                                     </div>
    +                                     <i className={recipeTypeToIcon(activity.recipeType)}>
    +                                         <i className={typeToIcon(selectedActivity.recipeType)}>
    +                                             <i className={typeToIcon(depended.recipeType)}>
    +                                                 <i className={typeToIcon(depended.recipeType)}>
    +                                                     {/* Graph tab */}
    +                                                     {topNav.tab == 'graph' ? (
    +                                                         <div className=\"flex preview-graph\" flow-common=\"\">
    +                                                             <div className=\"fh oa\">
    +                                                                 <div
    +                                                                     job-preview-graph=\"\"
    +                                                                     style=\"margin-top: 1px #ccc solid\"
    +                                                                     className=\"h100\">
    +                                                                     <div className=\"h100 mainPane\">
    +                                                                         <FlowGraph id=\"flow-graph\" />
    +                                                                     </div>
    +                                                                     <div className=\"h100 rightPane\">
    +                                                                         <div className=\"content\">
    +                                                                             {!rightColumnItem ? (
    +                                                                                 <div style=\"margin: 60px auto; text-align: center; font-size: 1.2em; color: #999\">
    +                                                                                     为详情选择一个组件
    +                                                                                 </div>
    +                                                                             ) : null}
    +                                                                             {rightColumnItem && selectedItemData ? (
    +                                                                                 <div>
    +                                                                                     {selectedItemData.type ==
    +                                                                                     'SOURCE' ? (
    +                                                                                         <div>
    +                                                                                             <h4
    +                                                                                                 className=\"mx-textellipsis\"
    +                                                                                                 style=\"margin-top: 25px\">
    +                                                                                                 <i
    +                                                                                                     className={typeToIcon(
    +                                                                                                         selectedItemData
    +                                                                                                             .dataset
    +                                                                                                             .type
    +                                                                                                     )}>
    +                                                                                                     {
    +                                                                                                         selectedItemData
    +                                                                                                             .dataset
    +                                                                                                             .name
    +                                                                                                     }
    +                                                                                                 </i>
    +                                                                                             </h4>
    +                                                                                             <i
    +                                                                                                 className={typeToIcon(
    +                                                                                                     selectedItemData
    +                                                                                                         .dataset.type
    +                                                                                                 )}>
    +                                                                                                 <p>
    +                                                                                                     此数据集是当前任务的一个源
    +                                                                                                 </p>
    +                                                                                             </i>
    +                                                                                         </div>
    +                                                                                     ) : null}
    +                                                                                     <i
    +                                                                                         className={typeToIcon(
    +                                                                                             selectedItemData.dataset
    +                                                                                                 .type
    +                                                                                         )}>
    +                                                                                         {selectedItemData.type ==
    +                                                                                         'NOT_INVOLVED' ? (
    +                                                                                             <div>
    +                                                                                                 {/*<p>This element is not involved in this job</p>*/}
    +                                                                                             </div>
    +                                                                                         ) : null}
    +                                                                                         {selectedItemData.type ==
    +                                                                                         'RECIPE' ? (
    +                                                                                             <div>
    +                                                                                                 <h4
    +                                                                                                     className=\"mx-textellipsis\"
    +                                                                                                     style=\"margin-top: 25px\">
    +                                                                                                     <i
    +                                                                                                         className={typeToIcon(
    +                                                                                                             selectedItemData.recipeType
    +                                                                                                         )}>
    +                                                                                                         {
    +                                                                                                             selectedItemData.recipeName
    +                                                                                                         }
    +                                                                                                     </i>
    +                                                                                                 </h4>
    +                                                                                                 <i
    +                                                                                                     className={typeToIcon(
    +                                                                                                         selectedItemData.recipeType
    +                                                                                                     )}>
    +                                                                                                     <p>
    +                                                                                                         {
    +                                                                                                             selectedItemData
    +                                                                                                                 .activities
    +                                                                                                                 .length
    +                                                                                                         }{' '}
    +                                                                                                         活动对于此组件
    +                                                                                                     </p>
    +                                                                                                     <p>
    +                                                                                                         {nbDisabledForRecipe(
    +                                                                                                             selectedItemData.recipeName
    +                                                                                                         )}{' '}
    +                                                                                                         被禁用.
    +                                                                                                     </p>
    +                                                                                                     <ul>
    +                                                                                                         {selectedItemData.reasons.map(
    +                                                                                                             (
    +                                                                                                                 nb,
    +                                                                                                                 index: number
    +                                                                                                             ) => {
    +                                                                                                                 return (
    +                                                                                                                     <li
    +                                                                                                                         key={`item-${index}`}>
    +                                                                                                                         {
    +                                                                                                                             nb
    +                                                                                                                         }
    +                                                                                                                         <span include-no-scope=\"/templates/jobs/activity-required-reason-short.html\" />
    +                                                                                                                     </li>
    +                                                                                                                 );
    +                                                                                                             }
    +                                                                                                         )}
    +                                                                                                     </ul>
    + 
    +                                                                                                     <div>
    +                                                                                                         <button
    +                                                                                                             className=\"btn btn-default\"
    +                                                                                                             onClick={() => {
    +                                                                                                                 focusOnSelectedItem();
    +                                                                                                             }}>
    +                                                                                                             <i className=\"icon-dku-search\">
    +                                                                                                                 查看详情
    +                                                                                                             </i>
    +                                                                                                         </button>
    +                                                                                                         <i className=\"icon-dku-search\">
    +                                                                                                             <div className=\"btn-group\">
    +                                                                                                                 <button
    +                                                                                                                     className=\"btn btn-danger\"
    +                                                                                                                     onClick={() => {
    +                                                                                                                         disableAllForRecipe(
    +                                                                                                                             selectedItemData.recipeName
    +                                                                                                                         );
    +                                                                                                                     }}>
    +                                                                                                                     禁用所有
    +                                                                                                                 </button>
    +                                                                                                                 <button
    +                                                                                                                     className=\"btn btn-success\"
    +                                                                                                                     onClick={() => {
    +                                                                                                                         enableAllForRecipe(
    +                                                                                                                             selectedItemData.recipeName
    +                                                                                                                         );
    +                                                                                                                     }}>
    +                                                                                                                     启用所有
    +                                                                                                                 </button>
    +                                                                                                             </div>
    +                                                                                                         </i>
    +                                                                                                     </div>
    +                                                                                                     <i className=\"icon-dku-search\" />
    +                                                                                                 </i>
    +                                                                                             </div>
    +                                                                                         ) : null}
    +                                                                                         <i
    +                                                                                             className={typeToIcon(
    +                                                                                                 selectedItemData.recipeType
    +                                                                                             )}>
    +                                                                                             <i className=\"icon-dku-search\">
    +                                                                                                 {selectedItemData.type ==
    +                                                                                                 'TARGET' ? (
    +                                                                                                     <div>
    +                                                                                                         <h4
    +                                                                                                             className=\"mx-textellipsis\"
    +                                                                                                             style=\"margin-top: 25px\">
    +                                                                                                             <i
    +                                                                                                                 className={typeToIcon(
    +                                                                                                                     selectedItemData
    +                                                                                                                         .dataset
    +                                                                                                                         .type
    +                                                                                                                 )}>
    +                                                                                                                 {
    +                                                                                                                     selectedItemData
    +                                                                                                                         .dataset
    +                                                                                                                         .name
    +                                                                                                                 }
    +                                                                                                             </i>
    +                                                                                                         </h4>
    +                                                                                                         <i
    +                                                                                                             className={typeToIcon(
    +                                                                                                                 selectedItemData
    +                                                                                                                     .dataset
    +                                                                                                                     .type
    +                                                                                                             )}>
    +                                                                                                             {selectedItemData
    +                                                                                                                 .activities
    +                                                                                                                 .length ==
    +                                                                                                             1 ? (
    +                                                                                                                 <p>
    +                                                                                                                     此数据集将被构建
    +                                                                                                                 </p>
    +                                                                                                             ) : null}
    +                                                                                                             {selectedItemData
    +                                                                                                                 .activities
    +                                                                                                                 .length >
    +                                                                                                             1 ? (
    +                                                                                                                 <p>
    +                                                                                                                     {
    +                                                                                                                         selectedItemData
    +                                                                                                                             .activities
    +                                                                                                                             .length
    +                                                                                                                     }{' '}
    +                                                                                                                     数据集的分区将被构建
    +                                                                                                                 </p>
    +                                                                                                             ) : null}
    +                                                                                                             <ul>
    +                                                                                                                 {selectedItemData.reasons.map(
    +                                                                                                                     (
    +                                                                                                                         nb,
    +                                                                                                                         index: number
    +                                                                                                                     ) => {
    +                                                                                                                         return (
    +                                                                                                                             <li
    +                                                                                                                                 key={`item-${index}`}>
    +                                                                                                                                 {
    +                                                                                                                                     nb
    +                                                                                                                                 }
    +                                                                                                                                 <span include-no-scope=\"/templates/jobs/activity-required-reason-short.html\" />
    +                                                                                                                             </li>
    +                                                                                                                         );
    +                                                                                                                     }
    +                                                                                                                 )}
    +                                                                                                             </ul>
    +                                                                                                         </i>
    +                                                                                                     </div>
    +                                                                                                 ) : null}
    +                                                                                                 <i
    +                                                                                                     className={typeToIcon(
    +                                                                                                         selectedItemData
    +                                                                                                             .dataset
    +                                                                                                             .type
    +                                                                                                     )}
    +                                                                                                 />
    +                                                                                             </i>
    +                                                                                         </i>
    +                                                                                     </i>
    +                                                                                 </div>
    +                                                                             ) : null}
    +                                                                             <i
    +                                                                                 className={typeToIcon(
    +                                                                                     selectedItemData.dataset.type
    +                                                                                 )}>
    +                                                                                 <i
    +                                                                                     className={typeToIcon(
    +                                                                                         selectedItemData.recipeType
    +                                                                                     )}>
    +                                                                                     <i className=\"icon-dku-search\">
    +                                                                                         <i
    +                                                                                             className={typeToIcon(
    +                                                                                                 selectedItemData.dataset
    +                                                                                                     .type
    +                                                                                             )}
    +                                                                                         />
    +                                                                                     </i>
    +                                                                                 </i>
    +                                                                             </i>
    +                                                                         </div>
    +                                                                         <i
    +                                                                             className={typeToIcon(
    +                                                                                 selectedItemData.dataset.type
    +                                                                             )}>
    +                                                                             <i
    +                                                                                 className={typeToIcon(
    +                                                                                     selectedItemData.recipeType
    +                                                                                 )}>
    +                                                                                 <i className=\"icon-dku-search\">
    +                                                                                     <i
    +                                                                                         className={typeToIcon(
    +                                                                                             selectedItemData.dataset
    +                                                                                                 .type
    +                                                                                         )}
    +                                                                                     />
    +                                                                                 </i>
    +                                                                             </i>
    +                                                                         </i>
    +                                                                     </div>
    +                                                                     <i
    +                                                                         className={typeToIcon(
    +                                                                             selectedItemData.dataset.type
    +                                                                         )}>
    +                                                                         <i
    +                                                                             className={typeToIcon(
    +                                                                                 selectedItemData.recipeType
    +                                                                             )}>
    +                                                                             <i className=\"icon-dku-search\">
    +                                                                                 <i
    +                                                                                     className={typeToIcon(
    +                                                                                         selectedItemData.dataset.type
    +                                                                                     )}
    +                                                                                 />
    +                                                                             </i>
    +                                                                         </i>
    +                                                                     </i>
    +                                                                 </div>
    +                                                                 <i
    +                                                                     className={typeToIcon(
    +                                                                         selectedItemData.dataset.type
    +                                                                     )}>
    +                                                                     <i
    +                                                                         className={typeToIcon(
    +                                                                             selectedItemData.recipeType
    +                                                                         )}>
    +                                                                         <i className=\"icon-dku-search\">
    +                                                                             <i
    +                                                                                 className={typeToIcon(
    +                                                                                     selectedItemData.dataset.type
    +                                                                                 )}
    +                                                                             />
    +                                                                         </i>
    +                                                                     </i>
    +                                                                 </i>
    +                                                             </div>
    +                                                             <i className={typeToIcon(selectedItemData.dataset.type)}>
    +                                                                 <i className={typeToIcon(selectedItemData.recipeType)}>
    +                                                                     <i className=\"icon-dku-search\">
    +                                                                         <i
    +                                                                             className={typeToIcon(
    +                                                                                 selectedItemData.dataset.type
    +                                                                             )}
    +                                                                         />
    +                                                                     </i>
    +                                                                 </i>
    +                                                             </i>
    +                                                         </div>
    +                                                     ) : null}
    +                                                     <i className={typeToIcon(selectedItemData.dataset.type)}>
    +                                                         <i className={typeToIcon(selectedItemData.recipeType)}>
    +                                                             <i className=\"icon-dku-search\">
    +                                                                 <i
    +                                                                     className={typeToIcon(
    +                                                                         selectedItemData.dataset.type
    +                                                                     )}
    +                                                                 />
    +                                                             </i>
    +                                                         </i>
    +                                                     </i>
    +                                                 </i>
    +                                             </i>
    +                                         </i>
    +                                     </i>
    +                                 </div>
    +                             ) : null}
    +                             <i className={typeToIcon(selectedActivity.recipeType)}>
    +                                 <i className={typeToIcon(depended.recipeType)}>
    +                                     <i className={typeToIcon(selectedItemData.dataset.type)}>
    +                                         <i className={typeToIcon(selectedItemData.recipeType)}>
    +                                             <i className=\"icon-dku-search\">
    +                                                 <i className={typeToIcon(selectedItemData.dataset.type)} />
    +                                             </i>
    +                                         </i>
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