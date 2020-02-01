import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return views.selected === 'Last value' || views.selected === 'History' ? (
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
    +                         {views.selected === 'History' &&
    +                         displayedMetrics.metrics.length &&
    +                         displayedMetricsRange.to - displayedMetricsRange.from ? (
    +                             <div style=\"margin: 10px 0 20px 0;\">
    +                                 <div
    +                                     time-range-brush=\"\"
    +                                     range=\"displayedMetricsRange\"
    +                                     selected-range=\"selectedRange\"
    +                                     on-change=\"brushChanged()\"
    +                                 />
    +                             </div>
    +                         ) : null}
    + 
    +                         {computing ? (
    +                             <span>
    +                                 <i className=\"icon-refresh icon-spin\" />
    +                             </span>
    +                         ) : null}
    +                     </div>
    +                 </div>
    + 
    +                 <div className=\"flex oa metrics-plots\">
    +                     <div className=\"fh metrics-wrapper\">
    +                         <div className=\"w80 h100\">
    +                             <div className=\"fh h100\">
    +                                 {!displayedMetrics.metrics.length && allComputedMetrics.metrics.length ? (
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
    +                                 {!displayedMetrics.metrics.length && !allComputedMetrics.metrics.length ? (
    +                                     <div className=\"centered-info\">
    +                                         <p>无 {metricsScope == 'PROJECT' ? <span>工程级别</span> : null} 指标可用</p>
    +                                         {!hasNoMetricsSettings ? (
    +                                             <p className=\"small\">
    +                                                 启用指标 <NavLink to=\"^.settings\">在管理页</NavLink>
    +                                             </p>
    +                                         ) : null}
    +                                         {metricsScope == 'PROJECT' ? (
    +                                             <p className=\"small\">工程级别的指标通过Python代码创建.</p>
    +                                         ) : null}
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 {displayedMetrics.metrics.map((displayedMetric, index: number) => {
    +                                     return (
    +                                         <div
    +                                             key={`item-${index}`}
    +                                             style={`display: ${
    +                                                 views.selected === 'History' ? 'block' : 'inline-block'
    +                                             }`}>
    +                                             {views.selected === 'History' ? (
    +                                                 <div
    +                                                     metric-banner=\"\"
    +                                                     displayed-data=\"getDisplayedData(displayedMetric)\"
    +                                                     displayed-metric=\"displayedMetric\"
    +                                                     displayed-range=\"selectedRange\"
    +                                                     compute-metric-for-selected=\"computeMetricForSelected\"
    +                                                     compute-metric-for-object=\"computeMetricForObject\"
    +                                                     create-and-pin-insight=\"createAndPinInsight\"
    +                                                 />
    +                                             ) : null}
    +                                             {views.selected === 'Last value' ? (
    +                                                 <div
    +                                                     metric-tile=\"\"
    +                                                     displayed-data=\"getDisplayedData(displayedMetric)\"
    +                                                     displayed-metric=\"displayedMetric\"
    +                                                     compute-metric-for-selected=\"computeMetricForSelected\"
    +                                                     compute-metric-for-object=\"computeMetricForObject\"
    +                                                     create-and-pin-insight=\"createAndPinInsight\"
    +                                                     className=\"tile\"
    +                                                     onClick={() => {
    +                                                         openMetricChartModal(displayedMetric);
    +                                                     }}
    +                                                 />
    +                                             ) : null}
    +                                         </div>
    +                                     );
    +                                 })}
    +                             </div>
    +                         </div>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     ) : null;
    + };
    + 
    + export default TestComponent;