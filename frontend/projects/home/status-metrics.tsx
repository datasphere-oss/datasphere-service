import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"h100 vertical-flex\" ng-controller=\"MetricsViewController\">
    +             <div className=\"flex\">
    +                 <div className=\"fh\">
    +                     {views.selected === 'Last value' || views.selected === 'History' ? (
    +                         <div
    +                             className=\"h100 page-top-padding\"
    +                             display-metrics=\"\"
    +                             metrics-scope=\"PROJECT\"
    +                             can-compute=\"false\"
    +                         />
    +                     ) : null}
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;