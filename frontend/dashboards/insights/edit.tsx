import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return insight ? (
    +         <div ng-switch=\"insight.type\" className={`h100 insight-edit ${insight.type}`}>
    +             <div ng-switch-when=\"dataset_table\" dataset-table-insight-edit=\"\">
    +                 <div ng-switch-when=\"chart\" chart-insight-edit=\"\" insight=\"insight\" className=\"h100\">
    +                     <div ng-switch-when=\"jupyter\" jupyter-insight-edit=\"\" insight=\"insight\" className=\"h100\">
    +                         <div ng-switch-when=\"report\" report-insight-edit=\"\" insight=\"insight\" className=\"h100\">
    +                             <div ng-switch-when=\"runnable-button\" runnable-button-insight-edit=\"\">
    +                                 {/* No edit tab */}
    +                                 <div ng-switch-default=\"\" insight-edit-go-to-view=\"\" />
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