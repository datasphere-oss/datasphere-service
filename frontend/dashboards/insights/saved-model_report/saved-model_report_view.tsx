import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"fh vertical-flex model-summary-page\">
    +             <div block-api-error=\"\">
    +                 <div ng-switch=\"\" on=\"insight.$savedModel.miniTask.taskType\">
    +                     <div ng-switch-when=\"PREDICTION\">
    +                         <div include-no-scope=\"/templates/ml/prediction-model/prediction-model-report.html\" />
    +                         <div ng-switch-when=\"CLUSTERING\">
    +                             <div include-no-scope=\"/templates/ml/clustering-model/clustering-model-report.html\" />
    +                         </div>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;