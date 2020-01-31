import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"h100 vertical-flex\" ng-controller=\"ChecksViewController\">
    +             <div block-api-error=\"\">
    +                 <div className=\"flex\">
    +                     <div className=\"fh\">
    +                         <div
    +                             className=\"h100 page-top-padding\"
    +                             display-checks=\"\"
    +                             metrics-scope=\"PROJECT\"
    +                             can-compute=\"false\"
    +                         />
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;