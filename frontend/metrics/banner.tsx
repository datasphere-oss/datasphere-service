import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className={`banner ${displayedData.$isPlotted ? 'chart' : 'table'}`}>
    +             <table style=\"width: 100%; table-layout: auto;\">
    +                 <tbody>
    +                     <tr>
    +                         <td className=\"info\" style={`left: ${adjustForScroll.left}px`}>
    +                             <div className=\"tile-wrapper h100 metric-info vertical-flex\">
    +                                 <h4
    +                                     className=\"tile-header noflex\"
    +                                     title={MetricsUtils.getMetricDisplayName(displayedMetric)}>
    +                                     <div className=\"horizontal-flex\">
    +                                         <span className=\"flex metric-name limited\">
    +                                             {MetricsUtils.getMetricDisplayName(displayedMetric)}
    +                                         </span>
    +                                         <span
    +                                             className=\"noflex metric-actions\"
    +                                             ng-include=\"'/templates/metrics/compute-metric-probe.html'\"
    +                                         />
    +                                     </div>
    +                                 </h4>
    + 
    +                                 <div className=\"tile-body flex\">
    +                                     <div
    +                                         metric-last-value=\"\"
    +                                         displayed-metric=\"displayedMetric\"
    +                                         displayed-data=\"displayedData\"
    +                                     />
    +                                 </div>
    + 
    +                                 <div className=\"tile-footer noflex\">
    +                                     <div className=\"date\">{date(lastValue.time, 'yyyy-MM-dd HH:mm')}</div>
    +                                 </div>
    +                             </div>
    +                         </td>
    + 
    +                         {displayedData ? (
    +                             <td className=\"card\" style=\"width: 100%; position: relative;\">
    +                                 <div
    +                                     metric-history=\"\"
    +                                     displayed-data=\"displayedData\"
    +                                     displayed-metric=\"displayedMetric\"
    +                                     displayed-range=\"displayedRange\"
    +                                     tooltips-container=\".metrics-wrapper\"
    +                                     className=\"history fh h100\"
    +                                 />
    +                             </td>
    +                         ) : null}
    +                     </tr>
    +                 </tbody>
    +             </table>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;