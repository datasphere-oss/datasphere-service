import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"metric-info tile-wrapper h100 vertical-flex\">
    +             <h4 className=\"noflex tile-header\" title={MetricsUtils.getMetricDisplayName(displayedMetric)}>
    +                 <div className=\"horizontal-flex\">
    +                     <span className=\"flex metric-name limited\">
    +                         {MetricsUtils.getMetricDisplayName(displayedMetric)}
    +                     </span>
    +                     <span
    +                         className=\"noflex metric-actions\"
    +                         ng-include=\"'/templates/metrics/compute-metric-probe.html'\"
    +                     />
    +                 </div>
    +             </h4>
    + 
    +             <div className=\"flex tile-body oya\">
    +                 <div
    +                     metric-last-value=\"\"
    +                     displayed-metric=\"displayedMetric\"
    +                     displayed-data=\"displayedData\"
    +                     className=\"fh\"
    +                 />
    +             </div>
    + 
    +             <div className=\"tile-footer noflex\">
    +                 <div className=\"date\">{date(lastValue.time, 'yyyy-MM-dd HH:mm')}</div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;