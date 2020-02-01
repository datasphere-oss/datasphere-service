import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return views.selected === 'Partitions' || views.selected === 'Table' ? (
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
    +                                 <div className=\"list-control-widget\">
    +                                     <button
    +                                         className=\"btn btn-default\"
    +                                         onClick={() => {
    +                                             refreshMetricsPartitions();
    +                                         }}
    +                                         disabled={refreshing}>
    +                                         刷新分区
    +                                     </button>
    +                                     {refreshing ? <i className=\"icon-refresh icon-spin\" style=\"color: gray\" /> : null}
    +                                 </div>
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
    +                         {displayedPartitionsRange && displayedPartitionsRange.to - displayedPartitionsRange.from ? (
    +                             <div style=\"margin: 10px 0 20px 0;\">
    +                                 <div
    +                                     time-range-brush=\"\"
    +                                     range=\"displayedPartitionsRange\"
    +                                     selected-range=\"selectedRange\"
    +                                     on-change=\"brushChanged()\"
    +                                 />
    +                             </div>
    +                         ) : null}
    +                     </div>
    + 
    +                     {displayedMetrics.metrics.length && !displayedPartitionsRange ? (
    +                         <div className=\"filter-box\" style=\"margin-left: 4px; margin-bottom: 10px;\">
    +                             <div className=\"std-list-search-box\" style=\"display: inline-block\">
    +                                 <span className=\"add-on\">
    +                                     <i className=\"icon-dku-search\" />
    +                                 </span>
    +                                 <input
    +                                     type=\"search\"
    +                                     value={uiState.partitionQuery}
    +                                     style=\"width: 210px;\"
    +                                     placeholder=\"Filter partitions...\"
    +                                 />
    +                             </div>
    +                         </div>
    +                     ) : null}
    +                 </div>
    + 
    +                 {views.selected != 'Table' && !displayedMetrics.metrics.length ? (
    +                     <div className=\"flex metrics-plots\">
    +                         <div className=\"fh metrics-wrapper\">
    +                             <div className=\"w80 h100\">
    +                                 {allComputedMetrics.metrics.length ? (
    +                                     <div className=\"centered-info\">
    +                                         <p>无指标显示</p>
    +                                         <p className=\"small\">
    +                                             <a
    +                                                 onClick={() => {
    +                                                     openDisplayedMetricsModal();
    +                                                 }}>
    +                                                 添加一些
    +                                             </a>
    +                                         </p>
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 {!allComputedMetrics.metrics.length ? (
    +                                     <div className=\"centered-info\">
    +                                         <p>无指标可用</p>
    +                                         {!hasNoMetricsSettings ? (
    +                                             <p className=\"small\">
    +                                                 启用指标 <NavLink to=\"^.settings\">在管理页</NavLink>
    +                                             </p>
    +                                         ) : null}
    +                                     </div>
    +                                 ) : null}
    +                             </div>
    +                         </div>
    +                     </div>
    +                 ) : null}
    + 
    +                 {views.selected === 'Table' ? (
    +                     <div className=\"flex metrics-plots\">
    +                         <div className=\"fh metrics-wrapper\" sort-table=\"\">
    +                             <div
    +                                 className=\"h100\"
    +                                 partition-table-data=\"\"
    +                                 split-width=\"\"
    +                                 column-count=\"2 + displayedMetrics.metrics.length\"
    +                                 dku-ui-state=\"uiState\">
    +                                 <div
    +                                     fat-table=\"\"
    +                                     className=\"h100 partitions-table\"
    +                                     headers=\"displayedTableColumns\"
    +                                     rows={displayedTableRows}
    +                                     column-widths=\"uiState.cellsWidth\"
    +                                     cell-template=\"/templates/metrics/partition-table-cell.html\"
    +                                     header-template=\"/templates/metrics/partition-table-header.html\"
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
    + 
    +                 {views.selected === 'Partitions' ? (
    +                     <div className=\"flex oa metrics-plots\">
    +                         <div className=\"fh metrics-wrapper\">
    +                             <div className=\"w80 h100\">
    +                                 <div>
    +                                     {displayedMetrics.metrics
    +                                         .onlyMetricsForPartition()
    +                                         .map((displayedMetric, index: number) => {
    +                                             return (
    +                                                 <div key={`item-${index}`} style=\"position: relative;\">
    +                                                     <div
    +                                                         partition-banner=\"\"
    +                                                         displayed-data=\"getDisplayedPartitionsData(displayedMetric)\"
    +                                                         displayed-metric=\"displayedMetric\"
    +                                                         displayed-partitions=\"filteredMetricsPartitions\"
    +                                                         displayed-partitions-range=\"selectedRange\"
    +                                                         compute-metric-for-all=\"computeMetricForAll\"
    +                                                     />
    +                                                 </div>
    +                                             );
    +                                         })}
    +                                 </div>
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