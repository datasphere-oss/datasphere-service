import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return views.selected === 'Columns' ? (
    +         <div className=\"metrics h100 vertical-flex\">
    +             <div block-api-error=\"\" className=\"noflex\">
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
    +                                         value={views.selected}
    +                                         ng-options=\"p.id as p.name for p in views.values\"
    +                                     />
    +                                 </div>
    + 
    +                                 <span>的</span>
    + 
    +                                 <div
    +                                     displayed-metrics-selector=\"\"
    +                                     choices=\"allComputedMetrics\"
    +                                     selected={displayedMetrics}
    +                                     refresh-choices=\"refreshAllComputedMetrics\"
    +                                     on-close=\"saveMetricsNow\"
    +                                     type=\"metrics\"
    +                                 />
    + 
    +                                 {metricsCallbacks.isPartitioned() ? <span>上</span> : null}
    + 
    +                                 {metricsCallbacks.isPartitioned() ? (
    +                                     <div className=\"list-customfilter-box\" style=\"display: inline-block\">
    +                                         <select
    +                                             dku-bs-select=\"{liveSearch:true,size:'auto', 'style':'dku-select-button'}\"
    +                                             value={metrics.displayedState.partition}
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
    +                                 <div
    +                                     ng-include=\"\"
    +                                     src=\"'/templates/metrics/last-metrics-run-display.html'\"
    +                                     style=\"display: inline-block;\"
    +                                 />
    +                             </div>
    + 
    +                             <div ng-include=\"\" src=\"'/templates/metrics/add-metrics-dataset-button.html'\" />
    + 
    +                             <div style=\"clear: both;\" />
    +                         </div>
    + 
    +                         {computing ? (
    +                             <span>
    +                                 <i className=\"icon-refresh icon-spin\" />
    +                             </span>
    +                         ) : null}
    +                     </div>
    + 
    +                     {displayedMetrics.metrics.length ? (
    +                         <div className=\"filter-box\" style=\"margin-left: 4px; margin-bottom: 10px;\">
    +                             <div className=\"std-list-search-box\" style=\"display: inline-block\">
    +                                 <span className=\"add-on\">
    +                                     <i className=\"icon-dku-search\" />
    +                                 </span>
    +                                 <input
    +                                     type=\"search\"
    +                                     value={uiState.columnQuery}
    +                                     style=\"width: 210px;\"
    +                                     placeholder=\"Filter columns...\"
    +                                 />
    +                             </div>
    + 
    +                             <div
    +                                 displayed-columns-selector=\"\"
    +                                 dataset-schema=\"datasetSchema\"
    +                                 selected={metrics.displayedState.columns}
    +                                 refresh-choices=\"refreshAllComputedMetrics\"
    +                                 on-close=\"saveMetricsNow\"
    +                             />
    +                         </div>
    +                     ) : null}
    +                 </div>
    + 
    +                 {views.selected === 'Columns' ? (
    +                     <div className=\"flex metrics-plots\">
    +                         <div className=\"fh metrics-wrapper\" sort-table=\"\">
    +                             <div
    +                                 className=\"h100\"
    +                                 column-table-data=\"\"
    +                                 split-width=\"\"
    +                                 column-count=\"2 + displayedMetricByColumnData.length\"
    +                                 dku-ui-state=\"uiState\">
    +                                 <div
    +                                     fat-table=\"\"
    +                                     className=\"h100 columns-table\"
    +                                     headers=\"displayedTableColumns\"
    +                                     rows={displayedTableRows}
    +                                     column-widths=\"uiState.cellsWidth\"
    +                                     cell-template=\"/templates/metrics/column-table-cell.html\"
    +                                     header-template=\"/templates/metrics/column-table-header.html\"
    +                                     header-height=\"45\"
    +                                     row-height=\"35\"
    +                                     as=\"cell\"
    +                                     digest-child-only=\"true\"
    +                                     row-index-as=\"rowIndex\"
    +                                 />
    +                             </div>
    +                         </div>
    +                     </div>
    +                 ) : null}
    +             </div>
    +         </div>
    +     ) : null;
    + };
    + 
    + export default TestComponent;