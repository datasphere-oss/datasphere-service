import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return mlTaskStatus.fullModelIds.length ? (
    +         <div className=\"model-snippets models-header\">
    +             <div style=\"display: inline-block\">
    +                 <div className=\"dropdown auto mass-action-btn\">
    +                     <input
    +                         type=\"checkbox\"
    +                         value={selection.all}
    +                         dku-indeterminate=\"selection.some\"
    +                         onChange={() => {
    +                             updateMassSelectionCheckbox();
    +                         }}
    +                     />
    +                     <a
    +                         data-toggle=\"dropdown\"
    +                         className=\"{disabled: selection.selectedObjects.length == 0,collapsed:selection.none}\">
    +                         {!selection.none ? <span>操作</span> : null}
    +                         <b className=\"caret\" />
    +                     </a>
    +                     <ul className=\"dropdown-menu\">
    +                         {canDeleteSelectedModels() ? (
    +                             <li>
    +                                 <a
    +                                     onClick={() => {
    +                                         deleteSelectedModels();
    +                                     }}>
    +                                     <i className=\"icon-trash\">删除</i>
    +                                 </a>
    +                                 <i className=\"icon-trash\" />
    +                             </li>
    +                         ) : null}
    +                         <i className=\"icon-trash\">
    +                             {canStarSelectedModels() ? (
    +                                 <li>
    +                                     {!allStarredModels() ? (
    +                                         <a
    +                                             onClick={() => {
    +                                                 starSelectedModels(true);
    +                                             }}>
    +                                             <i className=\"icon-star\">标星</i>
    +                                         </a>
    +                                     ) : null}
    +                                     <i className=\"icon-star\">
    +                                         {allStarredModels() ? (
    +                                             <a
    +                                                 onClick={() => {
    +                                                     starSelectedModels(false);
    +                                                 }}>
    +                                                 <i className=\"icon-star\">不标星</i>
    +                                             </a>
    +                                         ) : null}
    +                                         <i className=\"icon-star\" />
    +                                     </i>
    +                                 </li>
    +                             ) : null}
    +                             <i className=\"icon-star\">
    +                                 <i className=\"icon-star\">
    +                                     {canCreateEnsemble() ? (
    +                                         <li>
    +                                             <a
    +                                                 onClick={() => {
    +                                                     createEnsemble();
    +                                                 }}>
    +                                                 <i className=\"icon-group\" />创建全套模型
    +                                             </a>
    +                                         </li>
    +                                     ) : null}
    +                                 </i>
    +                             </i>
    +                         </i>
    +                     </ul>
    +                     <i className=\"icon-trash\">
    +                         <i className=\"icon-star\">
    +                             <i className=\"icon-star\" />
    +                         </i>
    +                     </i>
    +                 </div>
    +                 <i className=\"icon-trash\">
    +                     <i className=\"icon-star\">
    +                         <i className=\"icon-star\" />
    +                     </i>
    +                 </i>
    +             </div>
    +             <i className=\"icon-trash\">
    +                 <i className=\"icon-star\">
    +                     <i className=\"icon-star\">
    +                         {/* search */}
    +                         <div className=\"std-list-search-box list-control-widget\">
    +                             <span className=\"add-on\">
    +                                 <i className=\"icon-dku-search\" />
    +                             </span>
    +                             <input
    +                                 type=\"search\"
    +                                 value={selection.filterQuery.userQuery}
    +                                 placeholder=\"Search...\"
    +                                 style=\"width: 130px\"
    +                             />
    +                         </div>
    + 
    +                         {/* filters */}
    +                         <div
    +                             custom-element-popup=\"\"
    +                             style=\"display: inline-block; margin-left: 20px\"
    +                             className=\"list-customfilter-box\">
    +                             <span className=\"add-on\">
    +                                 <i className=\"icon-filter\" />
    +                             </span>
    +                             <div className=\"mainzone\" full-click=\"\" style=\"display: inline-block\">
    +                                 <a
    +                                     main-click=\"\"
    +                                     onClick={() => {
    +                                         togglePopover();
    +                                     }}
    +                                     className=\"btn dku-select-button\"
    +                                     style=\"display: inline-block; width: 70px; text-align: left;\">
    +                                     <span className=\"pull-right caret\"> 过滤器 </span>
    +                                 </a>
    +                             </div>
    +                             <div className=\"popover dropdown-menu custom-element-popup-popover models-list-filter-popover\">
    +                                 <div include-no-scope=\"/templates/ml/advanced-models-filter.html\" />
    +                             </div>
    +                         </div>
    + 
    +                         {/* metrics */}
    +                         {uiState.viewMode !== 'table' ? (
    +                             <div
    +                                 className=\"metric-box list-customfilter-box list-control-widget\"
    +                                 style=\"display: inline-block; margin-left: 20px\">
    +                                 <span className=\"add-on\">
    +                                     <i className=\"icon-trophy\" />
    +                                 </span>
    +                                 <select
    +                                     dku-bs-select=\"{'style': 'dku-select-button', titlePrefix:'Metric'}\"
    +                                     value={uiState.currentMetric}
    +                                     ng-options=\"m[0] as m[1] for m in possibleMetrics\">
    +                                     {/* sorting  */}
    +                                 </select>
    +                                 <option value=\"algorithm\">算法</option>
    +                                 <option value=\"sessionDate\">训练日期</option>
    +                                 <option value=\"mainMetric\">指标</option>
    +                                 <option value=\"userMeta.name\">名称</option>
    +                             </div>
    +                         ) : null}
    + 
    +                         {/* refresh */}
    +                         <a
    +                             className=\"refresh-button link-std\"
    +                             onClick={() => {
    +                                 refreshStatus();
    +                             }}>
    +                             <i className=\"icon-refresh\" />
    +                         </a>
    +                         <i className=\"icon-refresh\">
    +                             {pendingRequests.length ? (
    +                                 <i className=\"icon-spin icon-spinner\">
    +                                     {/* switch */}
    +                                     {mlTaskStatus.fullModelIds.length ? (
    +                                         <div className=\"dib btn-switchgroup pull-right\">
    +                                             <button
    +                                                 className=\"{active:uiState.viewMode==='sessions'}\"
    +                                                 onClick={() => {
    +                                                     uiState.viewMode = 'sessions';
    +                                                 }}>
    +                                                 <i className=\"icon-list\"> 会话</i>
    +                                             </button>
    +                                             <i className=\"icon-list\">
    +                                                 <button
    +                                                     className=\"{active:uiState.viewMode==='models'}\"
    +                                                     onClick={() => {
    +                                                         uiState.viewMode = 'models';
    +                                                     }}
    +                                                     disabled={!mlTaskStatus.fullModelIds.length}>
    +                                                     <i className=\"icon-th\"> 模型</i>
    +                                                 </button>
    +                                                 <i className=\"icon-th\">
    +                                                     <button
    +                                                         className=\"{active:uiState.viewMode==='table'}\"
    +                                                         onClick={() => {
    +                                                             uiState.viewMode = 'table';
    +                                                         }}
    +                                                         disabled={!mlTaskStatus.fullModelIds.length}>
    +                                                         <i className=\"icon-table\"> 表</i>
    +                                                     </button>
    +                                                     <i className=\"icon-table\" />
    +                                                 </i>
    +                                             </i>
    +                                         </div>
    +                                     ) : null}
    +                                     <i className=\"icon-list\">
    +                                         <i className=\"icon-th\">
    +                                             <i className=\"icon-table\" />
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             ) : null}
    +                         </i>
    +                     </i>
    +                 </i>
    +             </i>
    +         </div>
    +     ) : null;
    + };
    + 
    + export default TestComponent;