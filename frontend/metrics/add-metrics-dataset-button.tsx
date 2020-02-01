import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"pull-right\">
    +             <div className=\"computed-metrics-buttons\">
    +                 <div custom-element-popup=\"\" cep-position=\"align-right-bottom\" close-on-click=\"true\">
    +                     <i
    +                         className=\"icon-cog mainzone cursor-pointer\"
    +                         onClick={() => {
    +                             togglePopover();
    +                         }}
    +                     />
    +                     <ul className=\"popover custom-element-popup-popover dropdown-menu\">
    +                         {metricsScope != 'PROJECT' &&
    +                         allComputedMetrics &&
    +                         allComputedMetrics.notExistingViews &&
    +                         allComputedMetrics.notExistingViews.indexOf('METRICS_HISTORY') >= 0 ? (
    +                             <li>
    +                                 <a
    +                                     onClick={() => {
    +                                         addAllMetricsDatasetInFlow('METRICS_HISTORY', null, null);
    +                                     }}>
    +                                     从指标数据中创建一个数据集
    +                                 </a>
    +                             </li>
    +                         ) : null}
    +                         {metricsCallbacks.saveExternalMetricsValues ? (
    +                             <li>
    +                                 <a
    +                                     onClick={() => {
    +                                         addMetricValue();
    +                                     }}>
    +                                     添加一个指标值
    +                                 </a>
    +                             </li>
    +                         ) : null}
    +                         <li>
    +                             <a
    +                                 onClick={() => {
    +                                     clearAll();
    +                                 }}>
    +                                 清空指标值
    +                             </a>
    +                         </li>
    +                         {metricsCallbacks.isPartitioned() ? (
    +                             <li>
    +                                 <a
    +                                     onClick={() => {
    +                                         computeAll();
    +                                     }}>
    +                                     为所有的分区计算所有的指标
    +                                 </a>
    +                             </li>
    +                         ) : null}
    +                         {metricsCallbacks.isPartitioned() ? (
    +                             <li>
    +                                 <a
    +                                     onClick={() => {
    +                                         computeAllForPartition('ALL');
    +                                     }}>
    +                                     为整个数据集计算所有的指标
    +                                 </a>
    +                             </li>
    +                         ) : null}
    +                         {views.selected == 'Columns' ? (
    +                             <li>
    +                                 <a
    +                                     onClick={() => {
    +                                         exportColumnsTable();
    +                                     }}>
    +                                     导出表
    +                                 </a>
    +                             </li>
    +                         ) : null}
    +                         {views.selected == 'Table' ? (
    +                             <li>
    +                                 <a
    +                                     onClick={() => {
    +                                         exportPartitionsTable();
    +                                     }}>
    +                                     导出表
    +                                 </a>
    +                             </li>
    +                         ) : null}
    +                     </ul>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;