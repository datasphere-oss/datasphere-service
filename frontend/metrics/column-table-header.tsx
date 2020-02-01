import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"header-cell\">
    +             {cell.isColumn ? <span sort-col={cell.$sortIndex}>Column</span> : null}
    +             {!cell.isColumn && !cell.isActions ? (
    +                 <span sort-col={cell.$sortIndex}>
    +                     {MetricsUtils.getMetricName(cell)}
    +                     {/*         <span class=\"metric-actions\" ng-include=\"'/templates/metrics/compute-metric-probe.html'\" ng-init=\"displayedMetric = cell\"></span> */}
    +                 </span>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;