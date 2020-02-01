import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"header-cell\">
    +             {cell.isPartition ? <span sort-col={cell.$sortIndex}>分区</span> : null}
    +             {!cell.isPartition && !cell.isActions ? (
    +                 <span sort-col={cell.$sortIndex}>
    +                     {MetricsUtils.getMetricDisplayName(cell)}
    +                     <span
    +                         className=\"metric-actions\"
    +                         ng-include=\"'/templates/metrics/compute-metric-probe.html'\"
    +                         ng-init=\"displayedMetric = cell\"
    +                     />
    +                 </span>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;