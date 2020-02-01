import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 dku-modal metrics modal-w800\" auto-size=\"false\">
    +             <DkuModalHeaderMinimum modal-title={MetricsUtils.getMetricDisplayName(displayedMetric)} />
    + 
    +             <div className=\"modal-body no-padding\" style=\"overflow: visible;\">
    +                 <div block-api-error=\"\">
    +                     <div style=\"margin: 0 20px 20px 20px;\">
    +                         {displayedMetricsRange.to - displayedMetricsRange.from ? (
    +                             <div
    +                                 time-range-brush=\"\"
    +                                 range=\"displayedMetricsRange\"
    +                                 selected-range=\"selectedRange\"
    +                                 on-change=\"brushChanged()\"
    +                             />
    +                         ) : null}
    +                     </div>
    + 
    +                     <div
    +                         style={`height: ${
    +                             getDisplayedData(displayedMetric).$isPlotted || getDisplayedData(displayedMetric).$isArray
    +                                 ? 350
    +                                 : 200
    +                         }px; position: relative`}>
    +                         <div
    +                             metric-history=\"\"
    +                             displayed-data=\"getDisplayedData(displayedMetric)\"
    +                             displayed-metric=\"displayedMetric\"
    +                             displayed-range=\"selectedRange\"
    +                             className=\"{'oa': !getDisplayedData(displayedMetric).doubles &amp;&amp; !getDisplayedData(displayedMetric).longs}\"
    +                         />
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;
