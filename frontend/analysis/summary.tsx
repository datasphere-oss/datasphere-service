import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div block-api-error=\"\">
    +             {analysisCoreParams ? (
    +                 <div className=\"container-fluid h100 summary-tab page-top-padding fh\">
    +                     <div className=\"row-fluid h100\">
    +                         <div className=\"span9 h100 oa\" style=\"padding-right: 5px\">
    +                             <div
    +                                 className=\"summary\"
    +                                 editable-summary=\"\"
    +                                 editable=\"canWriteProject()\"
    +                                 object=\"analysisCoreParams\"
    +                                 object-interest=\"objectInterest\"
    +                                 object-type=\"SQL_NOTEBOOK\"
    +                                 get-tags=\"getTags\"
    +                             />
    +                         </div>
    +                         <div className=\"span3 h100\">
    +                             <div
    +                                 object-timeline-with-post=\"\"
    +                                 object-type=\"'ANALYSIS'\"
    +                                 project-key=\"$stateParams.projectKey\"
    +                                 object-id=\"analysisCoreParams.id\"
    +                                 initial-timeline=\"objectTimeline\"
    +                                 className=\"h100\"
    +                             />
    +                         </div>
    +                     </div>
    +                 </div>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;