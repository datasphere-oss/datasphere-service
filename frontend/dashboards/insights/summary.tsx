import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"container-fluid summary-tab\" style=\"margin-top: 10px;\" ng-switch=\"insight\">
    +             <div className=\"row-fluid\" ng-switch-default=\"\">
    +                 <div className=\"span8\" scrollable-to-bottom=\"\">
    +                     <div
    +                         className=\"summary\"
    +                         editable-summary=\"\"
    +                         editable=\"canWriteProject()\"
    +                         object=\"insight\"
    +                         object-interest=\"interest\"
    +                         object-type=\"INSIGHT\"
    +                         get-tags=\"getTags\"
    +                         tag-color=\"tagColor\"
    +                     />
    +                 </div>
    +                 <div className=\"span4\">
    +                     <div
    +                         object-timeline-with-post=\"\"
    +                         object-type=\"'INSIGHT'\"
    +                         project-key=\"$stateParams.projectKey\"
    +                         object-id=\"insight.id\"
    +                         initial-timeline=\"timeline\"
    +                     />
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;